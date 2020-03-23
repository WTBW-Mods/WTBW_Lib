package com.wtbw.mods.lib.item.util;

import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Constants;

/*
  @author: Naxanria
*/
public class BaseItemEnergyStorage extends BaseEnergyStorage
{
  private ItemStack stack;
  
  public BaseItemEnergyStorage(ItemStack stack, int capacity)
  {
    this(stack, capacity, capacity, capacity, 0);
  }
  
  public BaseItemEnergyStorage(ItemStack stack, int capacity, int maxTransfer)
  {
    this(stack, capacity, maxTransfer, maxTransfer, 0);
  }
  
  public BaseItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract)
  {
    this(stack, capacity, maxReceive, maxExtract, 0);
  }
  
  public BaseItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract, int energy)
  {
    super(capacity, maxReceive, maxExtract, energy);
    this.stack = stack;
    
    if (stack.hasTag() && stack.getTag().contains("storage", Constants.NBT.TAG_COMPOUND))
    {
      deserializeNBT(stack.getTag().getCompound("storage"));
    }
    else
    {
      serializeToStack();
    }
    
  }
  
  @Override
  public BaseEnergyStorage setEnergy(int amount)
  {
    super.setEnergy(amount);
    serializeToStack();
    
    return this;
  }
  
  @Override
  public BaseEnergyStorage setCapacity(int capacity)
  {
    super.setCapacity(capacity);
    serializeToStack();
    
    return this;
  }
  
  @Override
  public int insertInternal(int amount, boolean simulate)
  {
    int i = super.insertInternal(amount, simulate);
    if (!simulate)
    {
      serializeToStack();
    }
    return i;
  }
  
  @Override
  public int extractInternal(int amount, boolean simulate)
  {
    int i = super.extractInternal(amount, simulate);
    if (!simulate)
    {
      serializeToStack();
    }
    return i;
  }
  
  @Override
  public void setExtract(int extract)
  {
    super.setExtract(extract);
    serializeToStack();
  }
  
  @Override
  public void setReceive(int receive)
  {
    super.setReceive(receive);
    serializeToStack();
  }
  
  @Override
  public int receiveEnergy(int maxReceive, boolean simulate)
  {
    int i = super.receiveEnergy(maxReceive, simulate);
    if (!simulate)
    {
      serializeToStack();
    }
    return i;
  }
  
  @Override
  public int extractEnergy(int maxExtract, boolean simulate)
  {
    int i = super.extractEnergy(maxExtract, simulate);
    if (!simulate)
    {
      serializeToStack();
    }
    return i;
  }
  
  private void serializeToStack()
  {
    stack.getOrCreateTag().put("storage", serializeNBT());
  }
}
