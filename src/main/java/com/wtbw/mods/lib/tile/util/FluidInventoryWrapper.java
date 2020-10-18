package com.wtbw.mods.lib.tile.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/*
  @author: Naxanria
*/
public class FluidInventoryWrapper extends InventoryWrapper implements IFluidInventory
{
  public final IFluidTank[] tank;
  
  public FluidInventoryWrapper(ItemStackHandler handler, IFluidTank... tank)
  {
    super(handler);
    
    this.tank = tank;
  }
  
  public FluidInventoryWrapper(List<ItemStack> stacks, IFluidTank... tank)
  {
    super(stacks);
  
    this.tank = tank;
  }
  
  public FluidInventoryWrapper(ItemStack stack, IFluidTank... tank)
  {
    super(stack);
  
    this.tank = tank;
  }
  
  @Override
  public int getSizeFluidInventory()
  {
    return tank.length;
  }
  
  @Override
  public FluidStack getFluidInTank(int tank)
  {
    return this.tank[tank].getFluid();
  }
}
