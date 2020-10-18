package com.wtbw.mods.lib.tile.util;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/*
  @author: Naxanria
*/
public interface IFluidInventory extends IInventory
{
  int getSizeFluidInventory();
  
  FluidStack getFluidInTank(int tank);
}
