package com.wtbw.mods.lib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
  @author: Naxanria
*/
public class ItemStackChanceMap extends ChanceMap<ItemStack>
{
  public List<ItemStack> getRoll()
  {
    return getRoll(false);
  }
  
  public List<ItemStack> getMaxRoll()
  {
    return getRoll(true);
  }
  
  private List<ItemStack> getRoll(boolean maxResult)
  {
    Map<ItemStack, Integer> chancedMap = maxResult ? getMaxResultMap() : getChancedMap();
    
    List<ItemStack> output = new ArrayList<>();
  
    for (Map.Entry<ItemStack, Integer> entry : chancedMap.entrySet())
    {
      int count = entry.getValue();
      
      while (count > 0)
      {
        ItemStack stack = entry.getKey().copy();
        if (count > stack.getMaxStackSize())
        {
          stack.setCount(stack.getMaxStackSize());
          count -= stack.getMaxStackSize();
        }
        else
        {
          stack.setCount(count);
        }
        
        output.add(stack);
      }
    }
    
    return output;
  }
  
  public static ItemStackChanceMap read(PacketBuffer buffer)
  {
    boolean asCount = buffer.readBoolean();
    int size = buffer.readInt();
  
    ItemStackChanceMap map = new ItemStackChanceMap();
    map.setAttemptsAsCount(asCount);
    
    for (int i = 0; i < size; i++)
    {
      ItemStack stack = buffer.readItemStack();
      float chance = buffer.readFloat();
      int attempts = buffer.readInt();
      
      map.add(chance, attempts, stack);
    }
    
    return map;
  }
  
  public static PacketBuffer write(PacketBuffer buffer, final ItemStackChanceMap map)
  {
    buffer.writeBoolean(map.attemptsAsCount);
    buffer.writeInt(map.entries.size());
    for (Entry<ItemStack> entry : map.entries)
    {
      buffer.writeItemStack(entry.value);
      buffer.writeFloat(entry.chance);
      buffer.writeInt(entry.attempts);
    }
    
    return buffer;
  }
}
