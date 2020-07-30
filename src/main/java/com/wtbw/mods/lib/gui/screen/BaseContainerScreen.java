package com.wtbw.mods.lib.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.container.BaseTileContainer;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.ITooltipProvider;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
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
import net.minecraft.util.text.ITextProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  
  private Map<Slot, Sprite> slotBackgrounds = new HashMap<>();

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

  @Override // Screen.render
  public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
  {
    renderBackground(stack);
    //renderBackground();
//    super.render(mouseX, mouseY, partialTicks);
    super.render(stack, mouseX, mouseY, partialTicks);
    renderTooltip(stack, mouseX, mouseY);
  }
  
  protected <S extends Slot> S setBackground(S slot, Sprite background)
  {
    slotBackgrounds.put(slot, background);
    
    return slot;
  }
  
  @Override
  public void tick() // tick
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
  
  protected void renderTooltip(MatrixStack stack, int mouseX, int mouseY)
  {
    if (getMinecraft().player.inventory.getItemStack().isEmpty())
    {
      if (this.hoveredSlot != null && this.hoveredSlot.getHasStack())
      {
        this.renderTooltip(stack, this.hoveredSlot.getStack(), mouseX, mouseY);
      }
      else
      {
//        int i = (width - this.xSize) / 2;
//        int j = (height - this.ySize) / 2;

        for (ITooltipProvider provider : tooltipProviders)
        {
          if (provider.isHover(mouseX, mouseY))
          {
            renderTooltip(stack, getAsTextProperties(provider.getTooltip()), mouseX, mouseY);
            break;
          }
        }
      }
    }
  }
  
//  protected int getWidth()
//  {
//    return field_230708_k_;
//  }
//
//  protected int getHeight()
//  {
//    return field_230709_l_;
//  }
  
  protected ITextProperties getAsTextProperties(List<String> strings)
  {
    List<ITextProperties> properties = new ArrayList<>();
    for (String str : strings)
    {
      properties.add(ITextProperties.func_240652_a_(str));
    }
    
    return ITextProperties.func_240654_a_(properties);
  }
  
  @Override //init
  protected void init()
  {
    super.init();
    
    tooltipProviders.clear();
  }
  
  @Override // addButton
  protected <T extends Widget> T addButton(T widget)
  {
    if (widget instanceof ITooltipProvider)
    {
      tooltipProviders.add((ITooltipProvider) widget);
    }
    return super.addButton(widget);
  }
  
  protected void defaultGui(MatrixStack stack)
  {
    defaultGuiBackground(stack);
    renderTitle(stack);
    renderInventoryText(stack);
    renderSlotsBackground(stack);
  }
  
  protected void defaultGuiBackground(MatrixStack stack)
  {
    defaultGuiBackground(stack, guiLeft, guiTop);
  }
  
  protected void defaultGuiBackground(MatrixStack stack, int x, int y)
  {
    GuiUtil.renderGui(stack, x, y);
  }
  
  protected void defaultGuiBackground(MatrixStack stack, int x, int y, int width, int height)
  {
    GuiUtil.renderGui(stack, x, y, width, height);
  }
  
  protected void renderSlotsBackground(MatrixStack stack)
  {
    renderSlotsBackground(stack, true);
  }
  
  protected void renderSlotsBackground(MatrixStack stack, boolean inventory)
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
          GuiUtil.renderInventoryBackground(stack, x, y);
          break;
        }
      }
  
      GuiUtil.SLOT_SPRITE.render(stack, x, y);
      if (slotBackgrounds.containsKey(slot))
      {
        slotBackgrounds.get(slot).render(stack, x, y);
      }
    }
  }
  
  protected void renderTitle(MatrixStack stack)
  {
    renderTitle(stack, guiLeft + 8, guiTop + 6);
  }
  
  protected void renderTitle(MatrixStack stack, int x, int y)
  {
    if (title != null)
    {
      String title = this.title.getUnformattedComponentText();
      font.drawString(stack, title, x, y, 0xff404040);
    }
  }
  
  protected void renderInventoryText(MatrixStack stack)
  {
    renderInventoryText(stack, guiLeft + 8, guiTop + 73);
  }
  
  protected void renderInventoryText(MatrixStack stack, int x, int y)
  {
    font.drawString(stack, playerInventory.getDisplayName().getUnformattedComponentText(), x, y, 0xff404040);
//    font.drawString(playerInventory.getDisplayName().getFormattedText(), x, y, 0xff404040);
  }
  
  protected EnergyBar getDefaultBar(BaseEnergyStorage storage)
  {
    return new EnergyBar(storage, guiLeft + 10, guiTop + 16);
  }
  
  protected void drawStringNoShadow(MatrixStack stack, String string, int x, int y, int color)
  {
    font.drawString(stack, string, x, y, color);
  }
  
  protected void drawRect(MatrixStack stack, int x, int y, int width, int height, int color)
  {
    GuiUtil.drawRect(stack, x, y, width, height, color);
  }
}
