package com.wtbw.mods.lib.gui.screen;

import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.ITooltipProvider;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public abstract class BaseContainerScreen<C extends Container> extends ContainerScreen<C>
{
  private List<ITooltipProvider> tooltipProviders = new ArrayList<>();

  public BaseContainerScreen(C container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
  }

  protected void addTooltipProvider(ITooltipProvider provider)
  {
    tooltipProviders.add(provider);
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {
    renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    renderTooltip(mouseX, mouseY);
  }

  protected void renderTooltip(int mouseX, int mouseY)
  {
    if (this.minecraft.player.inventory.getItemStack().isEmpty())
    {
      if (this.hoveredSlot != null && this.hoveredSlot.getHasStack())
      {
        this.renderTooltip(this.hoveredSlot.getStack(), mouseX, mouseY);
      }
      else
      {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        for (ITooltipProvider provider : tooltipProviders)
        {
          if (provider.isHover(mouseX, mouseY))
          {
            renderTooltip(provider.getTooltip(), mouseX, mouseY);
            break;
          }
        }
      }
    }
  }
  
  @Override
  protected void init()
  {
    super.init();
    
    tooltipProviders.clear();
  }
  
  @Override
  protected <T extends Widget> T addButton(T widget)
  {
    if (widget instanceof ITooltipProvider)
    {
      tooltipProviders.add((ITooltipProvider) widget);
    }
    return super.addButton(widget);
  }
  
  protected void defaultGui()
  {
    defaultGuiBackground();
    renderTitle();
    renderInventoryText();
    renderSlotsBackground();
  }
  
  protected void defaultGuiBackground()
  {
    defaultGuiBackground(guiLeft, guiTop);
  }
  
  protected void defaultGuiBackground(int x, int y)
  {
    GuiUtil.renderGui(x, y);
  }
  
  protected void defaultGuiBackground(int x, int y, int width, int height)
  {
    GuiUtil.renderGui(x, y, width, height);
  }
  
  protected void renderSlotsBackground()
  {
    renderSlotsBackground(true);
  }
  
  protected void renderSlotsBackground(boolean inventory)
  {
    for (int i = 0; i < container.inventorySlots.size(); i++)
    {
      Slot slot = container.inventorySlots.get(i);
      int x = slot.xPos + guiLeft - 1;
      int y = slot.yPos + guiTop - 1;
      if (inventory)
      {
        if (i == container.inventorySlots.size() - 36)
        {
          GuiUtil.renderInventoryBackground(x, y);
          break;
        }
      }
      
      GuiUtil.renderSlotBackground(x, y);
    }
  }
  
  protected void renderTitle()
  {
    renderTitle(guiLeft + 8, guiTop + 6);
  }
  
  protected void renderTitle(int x, int y)
  {
    if (title != null)
    {
      String title = this.title.getUnformattedComponentText();
      font.drawString(title, x, y, 0xff404040);
    }
  }
  
  protected void renderInventoryText()
  {
    renderInventoryText(guiLeft + 8, guiTop + 73);
  }
  
  protected void renderInventoryText(int x, int y)
  {
    font.drawString(playerInventory.getDisplayName().getFormattedText(), x, y, 0xff404040);
  }
  
}
