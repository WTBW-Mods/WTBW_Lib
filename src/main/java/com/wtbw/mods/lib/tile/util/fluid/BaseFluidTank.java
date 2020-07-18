package com.wtbw.mods.lib.tile.util.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/*
  @author: Naxanria
*/
public class BaseFluidTank implements IFluidTank, INBTSerializable<CompoundNBT>
{
  protected FluidStack stored = FluidStack.EMPTY;
  
  private int capacity;
  private ITag<Fluid> acceptedTag = null;
  
  
  public BaseFluidTank(int capacity)
  {
    this.capacity = capacity;
  }
  
  public ITag<Fluid> getAcceptedTag()
  {
    return acceptedTag;
  }
  
  public BaseFluidTank setAcceptedTag(ResourceLocation acceptedTag)
  {
    this.acceptedTag = FluidTags.getCollection().get(acceptedTag);
    return this;
  }
  
  public boolean acceptsTags()
  {
    return acceptedTag != null;
  }
  
  public boolean acceptsFluidTag(FluidStack other)
  {
    return acceptedTag != null && acceptedTag.func_230236_b_().contains(other.getFluid());
  }
  
  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public FluidStack getFluid()
  {
    return stored;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getFluidAmount()
  {
    return stored.getAmount();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getCapacity()
  {
    return capacity;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFluidValid(FluidStack stack)
  {
    return isFluidEqual(stack) || acceptsFluidTag(stack);
  }
  
  /**
   * @return The amount of not filled fluid
   */
  public int getRemainingCapacity()
  {
    return capacity - getFluidAmount();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int fill(FluidStack resource, IFluidHandler.FluidAction action)
  {
    if (resource.isEmpty())
    {
      return 0;
    }
    
    int remaining = capacity - getFluidAmount();
    
    int amount = Math.min(resource.getAmount(), remaining);
    
    if (isEmpty() || isFluidValid(resource))
    {
      if (action.execute())
      {
        if (isEmpty())
        {
          stored = new FluidStack(resource, amount);
        }
        else
        {
          stored.setAmount(amount);
          onContentsChanged();
        }
      }
      
      return amount;
    }
    
    return 0;
  }
  
  protected void onContentsChanged()
  {
  
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action)
  {
    return drain(new FluidStack(stored, maxDrain), action);
  }
  
  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action)
  {
    if (!isEmpty() && isFluidEqual(resource))
    {
      FluidStack drained = new FluidStack(stored, Math.min(getFluidAmount(), resource.getAmount()));
      if (!drained.isEmpty() && action.execute())
      {
        stored.setAmount(getFluidAmount() - drained.getAmount());
      }
      
      return drained;
    }
    
    return FluidStack.EMPTY;
  }
  
  private boolean isEmpty()
  {
    return stored.isEmpty();
  }
  
  public boolean isFluidEqual(FluidStack other)
  {
    return stored.isFluidEqual(other);
  }
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public CompoundNBT serializeNBT()
  {
    CompoundNBT nbt = new CompoundNBT();
    if (!isEmpty())
    {
      nbt.put("stored", stored.writeToNBT(new CompoundNBT()));
    }
    nbt.putInt("capacity", capacity);
    return nbt;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void deserializeNBT(CompoundNBT nbt)
  {
    capacity = nbt.getInt("capacity");
    if (nbt.contains("stored"))
    {
      stored = FluidStack.loadFluidStackFromNBT(nbt.getCompound("stored"));
    }
  }
  
  public static List<ITag<Fluid>> getSharingTags(FluidStack a, FluidStack b)
  {
    List<ITag<Fluid>> shared = new ArrayList<>();
    FluidTags.getCollection().getOwningTags(a.getFluid()).stream()
      .filter(rl -> FluidTags.getCollection().getOwningTags(b.getFluid()).contains(rl))
      .forEach(rl -> shared.add(FluidTags.getCollection().get(rl)
      ));
    
    return shared;
  }
  
  public static boolean sharesTags(FluidStack a, FluidStack b)
  {
    return getSharingTags(a, b).size() > 0;
  }
}
