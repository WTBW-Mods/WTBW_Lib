package com.wtbw.mods.lib.gui.util.slot;

import com.wtbw.mods.lib.upgrade.UpgradeManager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/*
  @author: Naxanria
*/
public class UpgradeSlot extends BaseSlot
{
  public final UpgradeManager manager;
  
  public UpgradeSlot(UpgradeManager manager, int index, int xPosition, int yPosition)
  {
    super(manager.getUpgradeInventory(), index, xPosition, yPosition);
    
    this.manager = manager;
  }
  
  @Override
  public boolean isItemValid(@Nonnull ItemStack stack)
  {
    return !stack.isEmpty() && manager.isValidUpgrade(stack);
  }
  
  @Override
  public void onSlotChange(@Nonnull ItemStack a, @Nonnull ItemStack b)
  {
    manager.recalculate();
  }
  
  @Override
  public void onSlotChanged()
  {
    super.onSlotChanged();
    manager.recalculate();
  }
}
