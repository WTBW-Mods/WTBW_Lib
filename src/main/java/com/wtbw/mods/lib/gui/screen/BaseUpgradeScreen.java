package com.wtbw.mods.lib.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.container.BaseUpgradeContainer;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.upgrade.IUpgradeable;
import com.wtbw.mods.lib.upgrade.UpgradeManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

/*
  @author: Naxanria
*/
public abstract class BaseUpgradeScreen<C extends BaseUpgradeContainer<? extends IUpgradeable>> extends BaseContainerScreen<C>
{
  protected final UpgradeManager upgradeManager;
  public BaseUpgradeScreen(C container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    
    upgradeManager = ((IUpgradeable) tileEntity).getUpgradeManager();
  }
  
  @Override // drawGuiContainerBackgroundLayer
  protected final void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)
  {
    GuiUtil.renderGui(stack, guiLeft - 22, guiTop + 26, 26, upgradeManager.getMaxUpgradeSlots() * 18 + 10);
    defaultGui(stack);
    drawGuiBackgroundLayer(stack, partialTicks, mouseX, mouseY);
  }
  
  protected abstract void drawGuiBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY);
}
