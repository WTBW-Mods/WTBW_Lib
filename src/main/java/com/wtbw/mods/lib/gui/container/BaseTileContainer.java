package com.wtbw.mods.lib.gui.container;

import com.wtbw.mods.lib.gui.util.InputSlot;
import com.wtbw.mods.lib.util.nbt.NBTManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public abstract class BaseTileContainer<TE extends TileEntity> extends Container
{
  public final TE tileEntity;
  public final PlayerInventory playerInventory;
  protected final World world;
  
  protected int INVENTORY_START = 0;
  
  public BaseTileContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(type, id);
    
    tileEntity = (TE) world.getTileEntity(pos);
    this.playerInventory = playerInventory;
    
    this.world = world;
  }
  
  protected int addSlotRangeVertical(IItemHandler handler, int index, int x, int y, int amount, int dy)
  {
    return addSlotRange(handler, index, x, y, amount, 0, dy);
  }
  
  protected int addSlotRangeHorizontal(IItemHandler handler, int index, int x, int y, int amount, int dx)
  {
    return addSlotRange(handler, index, x, y, amount, dx, 0);
  }
  
  protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx, int dy)
  {
    for (int i = 0; i < amount; i++)
    {
      addSlot(handler, index, x, y);
      index++;
      x += dx;
      y += dy;
    }
    
    return index;
  }
  
  protected void addSlot(IItemHandler handler, int index, int x, int y)
  {
    addSlot(new SlotItemHandler(handler, index, x, y));
  }
  
  protected void addInputSlot(ItemStackHandler handler, int index, int x, int y)
  {
    addSlot(new InputSlot(handler, index, x, y));
  }
  
  protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int verAmount, int dx, int dy)
  {
    for (int i = 0; i < verAmount; i++)
    {
      index = addSlotRangeHorizontal(handler, index, x, y, horAmount, dx);
      y += dy;
    }
    
    return index;
  }
  
  protected void layoutPlayerInventorySlots()
  {
    layoutPlayerInventorySlots(9, 84);
  }
  
  protected void layoutPlayerInventorySlots(int leftCol, int topRow)
  {
    INVENTORY_START = inventorySlots.size();
    
    InvWrapper wrapper = new InvWrapper(playerInventory);
    addSlotBox(wrapper, 9, leftCol, topRow, 9, 3, 18, 18);
    
    topRow += 58;
    addSlotRangeHorizontal(wrapper, 0, leftCol, topRow, 9, 18);
  }
  
  protected IWorldPosCallable getWorldPos()
  {
    if (tileEntity == null)
    {
      return IWorldPosCallable.DUMMY;
    }
    
    return IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());
  }
  
  protected boolean canInteractWith(PlayerEntity entity, Block block)
  {
    return isWithinUsableDistance(getWorldPos(), entity, block);
  }
  
  @Override
  public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
  {
    ItemStack stack = ItemStack.EMPTY;
    Slot slot = inventorySlots.get(index);
  
    if (slot != null && slot.getHasStack())
    {
      ItemStack itemStack = slot.getStack();
      stack = itemStack.copy();

      if (index < INVENTORY_START)
      {
        // insert in player inventory
        if (!mergeItemStack(itemStack, INVENTORY_START, inventorySlots.size(), true))
        {
          return ItemStack.EMPTY;
        }
      }
      // insert into chest
      else if (!mergeItemStack(itemStack, 0, INVENTORY_START, false))
      {
        return ItemStack.EMPTY;
      }
    
      if (stack.isEmpty())
      {
        slot.putStack(ItemStack.EMPTY);
      }
      else
      {
        slot.onSlotChanged();
      }
    }
  
    return stack;
  }
  
  @Override
  public boolean canInteractWith(PlayerEntity playerIn)
  {
    return true;
  }
  
  protected void track(NBTManager manager)
  {
    manager.referenceHolders().forEach(iIntReferenceHolder -> trackInt(iIntReferenceHolder.cast()));
    manager.arrayHolders().forEach(iIntArrayHolder -> trackIntArray(iIntArrayHolder.cast()));
  }
}
