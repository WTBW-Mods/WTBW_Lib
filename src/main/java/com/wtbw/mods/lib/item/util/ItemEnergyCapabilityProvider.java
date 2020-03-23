package com.wtbw.mods.lib.item.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class ItemEnergyCapabilityProvider implements ICapabilityProvider
{
  private ItemStack stack;
  private final int capacity;
  private final int extract;
  private final int insert;
  private BaseItemEnergyStorage storage;
  private LazyOptional<BaseItemEnergyStorage> storageCap = LazyOptional.of(this::getStorage);
  
  public ItemEnergyCapabilityProvider(ItemStack stack, int capacity, int insert, int extract)
  {
    this.stack = stack;
    this.capacity = capacity;
    this.extract = extract;
    this.insert = insert;
  }
  
  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
  {
    return cap == CapabilityEnergy.ENERGY ? storageCap.cast() : LazyOptional.empty();
  }
  
  @Nonnull
  public BaseItemEnergyStorage getStorage()
  {
    if (storage == null)
    {
      storage = new BaseItemEnergyStorage(stack, capacity, insert, extract);
    }
    
    return storage;
  }
}
