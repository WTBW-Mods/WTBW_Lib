package com.wtbw.mods.lib.util.rand;

import com.wtbw.mods.lib.util.BiValue;
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
    Map<ItemStack, Integer> chancedMap = maxResult ? getMaxRolls() : getRolls();
    
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
          count = 0;
        }
        
        output.add(stack);
      }
    }
    
    return output;
  }
  
  public List<BiValue<ItemStack, Float>> getItemChances()
  {
    List<BiValue<ItemStack, Float>> chanceList = new ArrayList<>();
    BiValue<List<BiValue<ItemStack, Integer>>, List<Float>> itemChances = getChances();
    List<BiValue<ItemStack, Integer>> items = itemChances.a;
    List<Float> chances = itemChances.b;
  
    for (int i = 0; i < items.size(); i++)
    {
      ItemStack stack = getItemStack(items.get(i));
      Float chance = chances.get(i);
      chanceList.add(new BiValue<>(stack, chance));
    }
    
    return chanceList;
  }
  
  private ItemStack getItemStack(BiValue<ItemStack, Integer> biValue)
  {
    ItemStack copy = biValue.a.copy();
    copy.setCount(biValue.b);
    
    return copy;
  }
  
  public static ItemStackChanceMap read(PacketBuffer buffer)
  {
    boolean asCount = buffer.readBoolean();
    int mapSize = buffer.readInt();
  
    ItemStackChanceMap map = new ItemStackChanceMap();
    map.setAttemptsAsCount(asCount);
    
    for (int i = 0; i < mapSize; i++)
    {
      ItemStack stack = buffer.readItemStack();
      int listSize = buffer.readInt();
      for (int j = 0; j < listSize; j++)
      {
        float chance = buffer.readFloat();
        int attempts = buffer.readInt();
  
        map.add(chance, attempts, stack);
      }
    }
    
    return map;
  }
  
  public static PacketBuffer write(PacketBuffer buffer, final ItemStackChanceMap map)
  {
    buffer.writeBoolean(map.attemptsAsCount);
    buffer.writeInt(map.chanceMap.size());
    map.chanceMap.forEach(
      (stack, biValues) ->
      {
        buffer.writeItemStack(stack);
        buffer.writeInt(biValues.size());
        
        biValues.forEach(
        (biValue) ->
        {
          buffer.writeFloat(biValue.a);
          buffer.writeInt(biValue.b);
        });
      });
    
    return buffer;
  }
}
