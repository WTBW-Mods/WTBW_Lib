package com.wtbw.lib.tile.util.energy;

import com.wtbw.lib.util.NBTHelper;
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
    capacity = NBTHelper.getInt(nbt, "capacity");
    energy = NBTHelper.getInt(nbt, "energy");
    maxExtract = NBTHelper.getInt(nbt, "maxExtract");
    maxReceive = NBTHelper.getInt(nbt, "maxInsert");
  }
}
