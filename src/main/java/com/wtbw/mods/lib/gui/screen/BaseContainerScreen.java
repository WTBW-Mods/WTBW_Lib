package com.wtbw.mods.lib.gui.screen;

import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.ITooltipProvider;
import com.wtbw.mods.lib.network.RequestGuiUpdatePacket;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public abstract class BaseContainerScreen<C extends Container> extends ContainerScreen<C>
{
  public static final int COLOR_WHITE = 0xffffffff;
  public static final int COLOR_GRAY = 0xff888888;
  public static final int COLOR_GREEN = 0xff00ff00;
  public static final int COLOR_RED = 0xffff0000;
  public static final int COLOR_BLACK = 0xff000000;
  
  private List<ITooltipProvider> tooltipProviders = new ArrayList<>();
  protected int ticks = 0;
  
  protected final TileEntity tileEntity;
  protected boolean requestGuiUpdates;
  

  public BaseContainerScreen(C container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    
    if (container instanceof BaseTileContainer)
    {
      tileEntity = ((BaseTileContainer) container).tileEntity;
    }
    else
    {
      tileEntity = null;
    }
    
    requestGuiUpdates = tileEntity instanceof IGuiUpdateHandler;
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
  
  @Override
  public void tick()
  {
    if (requestGuiUpdates)
    {
      //todo: make config option?
//      if (ticks % 1 == 0)
//      {
        RequestGuiUpdatePacket.request(tileEntity.getPos());
//      }
    }
    ticks++;
    super.tick();
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
  
  protected EnergyBar getDefaultBar(BaseEnergyStorage storage)
  {
    return new EnergyBar(storage, guiLeft + 10, guiTop + 16);
  }
  
  protected void drawStringNoShadow(String string, int x, int y, int color)
  {
    font.drawString(string, x, y, color);
  }
  
  protected void drawRect(int x, int y, int width, int height, int color)
  {
    GuiUtil.drawRect(x, y, width, height, color);
  }
}
