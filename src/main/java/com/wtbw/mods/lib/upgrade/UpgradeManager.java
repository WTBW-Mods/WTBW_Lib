package com.wtbw.mods.lib.upgrade;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  @author: Naxanria
*/
public class UpgradeManager implements INBTSerializable<CompoundNBT>
{
  private int maxUpgradeSlots = 10;
  private int maxUpgradePoints;
  private int currentCost = 0;
  
  private List<ItemStack> upgradeInventory;
  
  private Map<ModifierType, List<Float>> modifiers = new HashMap<>();
  
  public UpgradeManager()
  {
    this(10);
  }
  
  public UpgradeManager(int maxUpgradePoints)
  {
    this.maxUpgradePoints = maxUpgradePoints;
    
    upgradeInventory = new ArrayList<>();
  }
  
  public int getMaxUpgradeSlots()
  {
    return maxUpgradeSlots;
  }
  
  public int getMaxUpgradePoints()
  {
    return maxUpgradePoints;
  }
  
  public int getCurrentCost()
  {
    return currentCost;
  }
  
  public List<ItemStack> getUpgradeInventory()
  {
    return upgradeInventory;
  }
  
  public boolean hasModifier(ModifierType type)
  {
    return modifiers.containsKey(type);
  }
  
  public int getModifierCount(ModifierType type)
  {
    return hasModifier(type) ? modifiers.get(type).size() : 0;
  }
  
  public float getModifiedValue(ModifierType type)
  {
    List<Float> floats = modifiers.get(type);
    if (floats != null)
    {
      float total = 1;
      for (Float aFloat : floats)
      {
        total *= aFloat;
      }
      
      return total;
    }
    else
    {
      return Float.MIN_VALUE;
    }
  }
  
  public boolean addUpgrade(ItemStack stack)
  {
    if (upgradeInventory.size() < maxUpgradeSlots)
    {
      Item item = stack.getItem();
      if (item instanceof IUpgradeProvider)
      {
        IUpgradeProvider provider = (IUpgradeProvider) item;
        if (currentCost + provider.upgradeCost() <= maxUpgradePoints)
        {
          Map<ModifierType, Float> mods = provider.modifierMap();
          for (Map.Entry<ModifierType, Float> modEntry : mods.entrySet())
          {
            addModifier(modEntry.getKey(), modEntry.getValue());
          }
          
          currentCost += provider.upgradeCost();
          
          stack = stack.copy();
          stack.setCount(1);
          upgradeInventory.add(stack);
          return true;
        }
      }
    }
    
    return false;
  }
  
  public ItemStack removeUpgrade(int index)
  {
    ItemStack stack = upgradeInventory.get(index);
    upgradeInventory.remove(index);
    return stack;
  }
  
  private void addModifier(ModifierType key, Float value)
  {
    List<Float> values;
    if (!modifiers.containsKey(key))
    {
      values = new ArrayList<>();
      modifiers.put(key, values);
    }
    else
    {
      values = modifiers.get(key);
    }
    
    values.add(value);
  }
  
  public void recalculate()
  {
    modifiers.clear();
    currentCost = 0;
    
    for (int i = 0; i < upgradeInventory.size(); i++)
    {
      ItemStack stack = upgradeInventory.get(i);
      
      if (stack == null || !(stack.getItem() instanceof IUpgradeProvider))
      {
        upgradeInventory.remove(i--);
        continue;
      }
  
      IUpgradeProvider provider = (IUpgradeProvider) stack.getItem();
      currentCost += provider.upgradeCost();
      
      provider.modifierMap().forEach(this::addModifier);
    }
  }
  
  @Override
  public CompoundNBT serializeNBT()
  {
    CompoundNBT nbt = new CompoundNBT();
  
    ListNBT list = new ListNBT();
    for (ItemStack stack : upgradeInventory)
    {
      list.add(stack.serializeNBT());
    }
    
    nbt.put("upgrades", list);
    
    nbt.putInt("maxPoints", maxUpgradePoints);
    nbt.putInt("currPoints", currentCost);
    nbt.putInt("maxSlots", maxUpgradeSlots);
    
    return nbt;
  }
  
  @Override
  public void deserializeNBT(CompoundNBT nbt)
  {
    ListNBT list = nbt.getList("upgrades", Constants.NBT.TAG_COMPOUND);
    upgradeInventory.clear();
    list.forEach(inbt ->
    {
      CompoundNBT compound = (CompoundNBT) inbt;
      ItemStack stack = new ItemStack(Items.AIR);
      stack.deserializeNBT(compound);
    });
    
    maxUpgradePoints = nbt.getInt("maxPoints");
    currentCost = nbt.getInt("currPoints");
    maxUpgradeSlots = nbt.getInt("maxSlots");
    
    if (nbt.contains("recalc"))
    {
      recalculate();
    }
  }
}
