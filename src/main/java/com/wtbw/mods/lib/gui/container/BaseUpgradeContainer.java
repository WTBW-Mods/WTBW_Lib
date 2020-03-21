package com.wtbw.mods.lib.gui.container;

import com.wtbw.mods.lib.gui.util.slot.UpgradeSlot;
import com.wtbw.mods.lib.upgrade.IUpgradeable;
import com.wtbw.mods.lib.upgrade.UpgradeManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public abstract class BaseUpgradeContainer<TE extends TileEntity & IUpgradeable> extends BaseTileContainer<TE>
{
  protected final UpgradeManager upgradeManager;
  protected final int upgradeSlotsSize;
  
  public BaseUpgradeContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInventory)
  {
    super(type, id, world, pos, playerInventory);
    
    upgradeManager = tileEntity.getUpgradeManager();
    upgradeSlotsSize = upgradeManager.getMaxUpgradeSlots();
    addUpgradeSlots(-15, 31);
    
  }
  
  protected void addUpgradeSlots(int x, int y)
  {
    for (int i = 0; i < upgradeSlotsSize; i++)
    {
      addSlot(new UpgradeSlot(upgradeManager, i, x, y + 18 * i));
    }
  }
  
  public void enableUpgradeSlots(boolean enable)
  {
    enableSlots(0, upgradeSlotsSize, enable);
  }
  
}
