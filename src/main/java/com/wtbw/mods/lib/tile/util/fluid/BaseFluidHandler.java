package com.wtbw.mods.lib.tile.util.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public class BaseFluidHandler implements IFluidHandler
{
  private final List<BaseFluidTank> tanks = new ArrayList<>();
  
  public BaseFluidTank getTank(int tank)
  {
    return tanks.get(tank);
  }
  
  @Override
  public int getTanks()
  {
    return tanks.size();
  }
  
  @Nonnull
  @Override
  public FluidStack getFluidInTank(int tank)
  {
    return getTank(tank).getFluid();
  }
  
  @Override
  public int getTankCapacity(int tank)
  {
    return getTank(tank).getCapacity();
  }
  
  @Override
  public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
  {
    return getTank(tank).isFluidValid(stack);
  }
  
  @Override
  public int fill(FluidStack resource, FluidAction action)
  {
    int amount = resource.getAmount();
    FluidStack fillResource = new FluidStack(resource, amount);
    
    for (int i = 0; i < getTanks(); i++)
    {
      BaseFluidTank tank = getTank(i);
      if (tank.getRemainingCapacity() > 0 && tank.isFluidValid(resource))
      {
        amount = tank.fill(fillResource, action);
        fillResource.setAmount(amount);
        if (fillResource.isEmpty())
        {
          break;
        }
      }
    }
    return resource.getAmount() - amount;
  }
  
  @Nonnull
  @Override
  public FluidStack drain(FluidStack resource, FluidAction action)
  {
    return null;
  }
  
  @Nonnull
  @Override
  public FluidStack drain(int maxDrain, FluidAction action)
  {
    return null;
  }
}
