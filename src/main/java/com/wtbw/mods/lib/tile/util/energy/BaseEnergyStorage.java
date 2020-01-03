package com.wtbw.mods.lib.tile.util.energy;

import com.wtbw.mods.lib.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

/*
  @author: Naxanria
*/
public class BaseEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>
{
  public BaseEnergyStorage(int capacity)
  {
    super(capacity);
  }
  
  public BaseEnergyStorage(int capacity, int maxTransfer)
  {
    super(capacity, maxTransfer);
  }
  
  public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract)
  {
    super(capacity, maxReceive, maxExtract);
  }
  
  public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
  {
    super(capacity, maxReceive, maxExtract, energy);
  }
  
  public BaseEnergyStorage setEnergy(int amount)
  {
    energy = MathHelper.clamp(amount, 0, capacity);
    return this;
  }
  
  public BaseEnergyStorage setCapacity(int capacity)
  {
    this.capacity = capacity;
    energy = MathHelper.clamp(energy, 0, capacity);
    return this;
  }
  
  public int getMaxExtract()
  {
    return maxExtract;
  }
  
  public int getMaxInsert()
  {
    return maxReceive;
  }
  
  /**
   * Inserts energy, bypassing canReceive() and maxReceive
   * @param amount the amount to insert
   * @param simulate to simulate or not
   * @return the amount inserted
   */
  public int insertInternal(int amount, boolean simulate)
  {
    int nEnergy = MathHelper.clamp(energy + amount, 0, capacity);
    int diff = nEnergy - energy;
    if (!simulate)
    {
      energy = nEnergy;
    }
    
    return diff;
  }
  
  /**
   * Extracts energy, bypassing canInsert() and maxInsert
   * @param amount the amount to extract
   * @param simulate to simulate or not
   * @return the amount extracted
   */
  public int extractInternal(int amount, boolean simulate)
  {
    return -insertInternal(-amount, simulate);
  }
  
  @Override
  public CompoundNBT serializeNBT()
  {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("capacity", capacity);
    nbt.putInt("energy", energy);
    nbt.putInt("maxExtract", maxExtract);
    nbt.putInt("maxInsert", maxReceive);
    
    return nbt;
  }
  
  @Override
  public void deserializeNBT(CompoundNBT nbt)
  {
    if (nbt.isEmpty())
    {
      return;
    }
    
    capacity = NBTHelper.getInt(nbt, "capacity");
    energy = NBTHelper.getInt(nbt, "energy");
    maxExtract = NBTHelper.getInt(nbt, "maxExtract");
    maxReceive = NBTHelper.getInt(nbt, "maxInsert");
  }
  
  public float getPercentageFilled()
  {
    return energy / (float) capacity;
  }
  
  public int getComparatorStrength()
  {
    float p = getPercentageFilled();
    int c = (int) (p * 15);
    if (c == 0)
    {
      return (energy > 0) ? 1 : 0;
    }
    
    if (c == 15)
    {
      return (energy == capacity) ? 15 : 14;
    }
    
    return c;
  }
}
