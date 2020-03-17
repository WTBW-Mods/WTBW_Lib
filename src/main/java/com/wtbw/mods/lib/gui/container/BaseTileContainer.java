package com.wtbw.mods.lib.gui.container;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public abstract class BaseTileContainer<TE extends TileEntity> extends BaseContainer
{
  public final TE tileEntity;
  protected final World world;
  
  public BaseTileContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(type, id, playerInventory);
    
    tileEntity = (TE) world.getTileEntity(pos);
    
    this.world = world;
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
}
