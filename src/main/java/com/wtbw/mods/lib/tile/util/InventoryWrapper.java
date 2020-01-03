package com.wtbw.mods.lib.tile.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/*
  @author: Naxanria
*/
public class InventoryWrapper implements IInventory
{
  public final ItemStackHandler handler;
  
  public InventoryWrapper(ItemStackHandler handler)
  {
    this.handler = handler;
  }
  
  @Override
  public int getSizeInventory()
  {
    return handler.getSlots();
  }
  
  @Override
  public boolean isEmpty()
  {
    for (int i = 0; i < getSizeInventory(); i++)
    {
      if (!handler.getStackInSlot(i).isEmpty())
      {
        return false;
      }
    }
    
    return true;
  }
  
  @Override
  public ItemStack getStackInSlot(int index)
  {
    return handler.getStackInSlot(index);
  }
  
  @Override
  public ItemStack decrStackSize(int index, int count)
  {
    return handler.extractItem(index, count, false);
  }
  
  @Override
  public ItemStack removeStackFromSlot(int index)
  {
    ItemStack stack = getStackInSlot(index);
    handler.setStackInSlot(index, ItemStack.EMPTY);
    return stack;
  }
  
  @Override
  public void setInventorySlotContents(int index, ItemStack stack)
  {
    handler.setStackInSlot(index, stack);
  }
  
  @Override
  public void markDirty()
  { }
  
  @Override
  public boolean isUsableByPlayer(PlayerEntity player)
  {
    return true;
  }
  
  @Override
  public void clear()
  {
    for (int i = 0; i < handler.getSlots(); i++)
    {
      handler.setStackInSlot(i, ItemStack.EMPTY);
    }
  }
}
