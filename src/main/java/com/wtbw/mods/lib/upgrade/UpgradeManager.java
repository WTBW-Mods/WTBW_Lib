package com.wtbw.mods.lib.upgrade;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/*
  @author: Naxanria
*/
public class UpgradeManager implements INBTSerializable<CompoundNBT>
{
  protected Function<ItemStack, Boolean> filter = stack -> true;
  
  private int maxUpgradeSlots = 5;
  private int insertedCount = 0;
  private int maxUpgradePoints;
  private int currentCost = 0;
  
  private ItemStackHandler upgradeInventory;
  
  private Map<ModifierType, List<Float>> modifiers = new HashMap<>();
  
  public UpgradeManager()
  {
    this(5, 10);
  }
  
  public UpgradeManager(int maxUpgradeSlots)
  {
    this(maxUpgradeSlots, 10);
  }
  
  public UpgradeManager(int maxUpgradeSlots, int maxUpgradePoints)
  {
    this.maxUpgradePoints = maxUpgradePoints;
    this.maxUpgradeSlots = maxUpgradeSlots;
    
    upgradeInventory = new ItemStackHandler(maxUpgradeSlots)
    {
      @Override
      public int getSlotLimit(int slot)
      {
        return 1;
      }
    };
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
  
  public ItemStackHandler getUpgradeInventory()
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
  
  public Function<ItemStack, Boolean> getFilter()
  {
    return filter;
  }
  
  public UpgradeManager setFilter(Filter filter)
  {
    this.filter = stack ->
    {
      Map<ModifierType, Float> mods = ((IUpgradeProvider) stack.getItem()).modifierMap();
      for (Map.Entry<ModifierType, Float> entry : mods.entrySet())
      {
        if (!filter.isValid(entry.getKey()))
        {
          return false;
        }
      }
      
      return true;
    };
    
    return this;
  }
  
  public UpgradeManager setFilter(ModifierType... types)
  {
    setFilter(new Filter(types));
    return this;
  }
  
  public UpgradeManager setFilter(Function<ItemStack, Boolean> filter)
  {
    this.filter = filter;
    return this;
  }
  
  public float getModifiedValue(ModifierType type)
  {
    List<Float> floats = modifiers.get(type);
    if (floats != null)
    {
      float total = type.isAdditive() ? 0 : 1;
      for (Float aFloat : floats)
      {
        if (type.isAdditive())
        {
          total += aFloat;
        }
        else
        {
          total *= aFloat;
        }
      }
      
      return total;
    }
    else
    {
      return Float.MIN_VALUE;
    }
  }
  
  public float getValueOrDefault(ModifierType type)
  {
    return getValueOrDefault(type, 1);
  }
  
  public float getValueOrDefault(ModifierType type, float defaultValue)
  {
    if (hasModifier(type))
    {
      return getModifiedValue(type);
    }
    
    return defaultValue;
  }
  
  public boolean addUpgrade(ItemStack stack)
  {
    if (insertedCount < maxUpgradeSlots)
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
          insert(stack);
          insertedCount++;
          return true;
        }
      }
    }
    
    return false;
  }
  
  private void insert(ItemStack stack)
  {
    for (int i = 0; i < upgradeInventory.getSlots(); i++)
    {
      if (upgradeInventory.getStackInSlot(i) == ItemStack.EMPTY)
      {
        upgradeInventory.insertItem(i, stack, false);
        break;
      }
    }
  }
  
  private void remove(int index)
  {
    upgradeInventory.setStackInSlot(index, new ItemStack(Items.AIR));
    insertedCount--;
  }
  
  public ItemStack removeUpgrade(int index)
  {
    ItemStack stack = upgradeInventory.getStackInSlot(index);
    remove(index);
    
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
    
    for (int i = 0; i < upgradeInventory.getSlots(); i++)
    {
      ItemStack stack = upgradeInventory.getStackInSlot(i);
      
      if (!(stack.getItem() instanceof IUpgradeProvider))
      {
        remove(i);
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
    
    nbt.put("upgrades", upgradeInventory.serializeNBT());
    
    nbt.putInt("maxPoints", maxUpgradePoints);
    nbt.putInt("currPoints", currentCost);
    nbt.putInt("maxSlots", maxUpgradeSlots);
    
    return nbt;
  }
  
  @Override
  public void deserializeNBT(CompoundNBT nbt)
  {
    upgradeInventory.deserializeNBT(nbt.getCompound("upgrades"));
    
    maxUpgradePoints = nbt.getInt("maxPoints");
    currentCost = nbt.getInt("currPoints");
    maxUpgradeSlots = nbt.getInt("maxSlots");
    
    if (nbt.contains("recalc"))
    {
      recalculate();
    }
  }
  
  public boolean isValidUpgrade(ItemStack stack)
  {
    Item item = stack.getItem();
    if (item instanceof IUpgradeProvider)
    {
      return filter.apply(stack);
    }
    
    return false;
  }
  
  public static class Filter
  {
    private List<ModifierType> validModifiers = new ArrayList<>();
  
    public Filter(ModifierType[] types)
    {
      for (ModifierType type : types)
      {
        if (!validModifiers.contains(type))
        {
          validModifiers.add(type);
        }
      }
    }
    
    public boolean isValid(ModifierType type)
    {
      return validModifiers.contains(type);
    }
  
    public static Filter create(ModifierType... types)
    {
      return new Filter(types);
    }
  }
}
