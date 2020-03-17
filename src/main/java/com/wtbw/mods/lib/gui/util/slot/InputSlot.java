package com.wtbw.mods.lib.gui.util.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/*
  @author: Naxanria
*/
public class InputSlot extends BaseSlot
{
  private final ItemStackHandler handler;
  
  public InputSlot(ItemStackHandler handler, int index, int xPosition, int yPosition)
  {
    super(handler, index, xPosition, yPosition);
    this.handler = handler;
  }
  
  @Override
  public void putStack(@Nonnull ItemStack stack)
  {
    super.putStack(stack);
  }
  
  @Override
  public boolean canTakeStack(PlayerEntity playerIn)
  {
    return true;
  }
  
  @Nonnull
  @Override
  public ItemStack decrStackSize(int amount)
  {
    int slotNumber = getSlotIndex();
    ItemStack stackInSlot = handler.getStackInSlot(slotNumber);
    if (amount > stackInSlot.getCount())
    {
      handler.setStackInSlot(slotNumber, ItemStack.EMPTY);
      return stackInSlot;
    }
    
    ItemStack toReturn = stackInSlot.copy();
    toReturn.setCount(amount);
    stackInSlot.setCount(stackInSlot.getCount() - amount);
    handler.setStackInSlot(slotNumber, stackInSlot.getCount() == 0 ? ItemStack.EMPTY : stackInSlot);
    
    return toReturn;
  }
}
