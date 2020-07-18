package com.wtbw.mods.lib.network;

import com.google.common.collect.ImmutableMap;
import com.wtbw.mods.lib.WTBWLib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.StateHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Optional;

/*
  @author: Naxanria
*/
public class BufferHelper
{
  public static Vector3d readVector3d(PacketBuffer buffer)
  {
    return new Vector3d
    (
      buffer.readDouble(),
      buffer.readDouble(),
      buffer.readDouble()
    );
  }
  
  public static PacketBuffer writeVector3d(PacketBuffer buffer, Vector3d Vector3d)
  {
    buffer.writeDouble(Vector3d.x);
    buffer.writeDouble(Vector3d.y);
    buffer.writeDouble(Vector3d.z);
    
    return buffer;
  }
  
  public static BlockState readState(PacketBuffer buffer)
  {
    String id = buffer.readString();
    int size = buffer.readInt();
  
    Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(id));
    BlockState state = block.getDefaultState();
    StateContainer<Block, BlockState> stateContainer = block.getStateContainer();
    for (int i = 0; i < size; i++)
    {
      String p = buffer.readString();
      Property<?> property = stateContainer.getProperty(p);
      String v = buffer.readString();
      if (property != null)
      {
        state = setValueHelper(state, property, v);
      }
      else
      {
        WTBWLib.LOGGER.warn("Unknown property {} for {}", p, block);
      }
    }
  
    return state;
  }
  
  private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S state, Property<T> property, String valueName)
  {
    Optional<T> optional = property.parseValue(valueName);
    if (optional.isPresent())
    {
      return (S)(state.with(property, (T)(optional.get())));
    }
    else
    {
      WTBWLib.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", property.getName(), valueName, state.toString());
      return state;
    }
  }
  
  public static PacketBuffer writeState(PacketBuffer buffer, BlockState state)
  {
    String id = Registry.BLOCK.getKey(state.getBlock()).toString();
    ImmutableMap<Property<?>, Comparable<?>> values = state.getValues();
    int size = values.size();
    buffer.writeString(id);
    buffer.writeInt(size);
  
    for(Map.Entry<Property<?>, Comparable<?>> entry : values.entrySet())
    {
      Property<?> property = entry.getKey();
      String name = property.getName();
      String value = getName(property, entry.getValue());
      buffer.writeString(name);
      buffer.writeString(value);
    }
    
    return buffer;
  }
  
  public static <T extends Comparable<T>> String getName(Property<T> property, Comparable<?> value) {
    return property.getName((T)value);
  }
}
