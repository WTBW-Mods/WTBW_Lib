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
import com.wtbw.mods.lib.util.Cache;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextComponent;

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
  
  protected MatrixStack currentStack = null;
  
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
  public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks)
  {
    currentStack = stack;
    
    func_230446_a_(stack);
    //renderBackground();
//    super.render(mouseX, mouseY, partialTicks);
    super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
    renderTooltip(stack, mouseX, mouseY);
  }
  
  protected <S extends Slot> S setBackground(S slot, Sprite background)
  {
    slotBackgrounds.put(slot, background);
    
    return slot;
  }
  
  @Override
  public void func_231023_e_() // tick
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
    super.func_231023_e_();
  }
  
  protected void renderTooltip(MatrixStack stack, int mouseX, int mouseY)
  {
    if (getMinecraft().player.inventory.getItemStack().isEmpty())
    {
      if (this.hoveredSlot != null && this.hoveredSlot.getHasStack())
      {
        this.func_230457_a_(stack, this.hoveredSlot.getStack(), mouseX, mouseY);
      }
      else
      {
        int i = (getWidth() - this.xSize) / 2;
        int j = (getHeight() - this.ySize) / 2;

        for (ITooltipProvider provider : tooltipProviders)
        {
          if (provider.isHover(mouseX, mouseY))
          {
            func_238652_a_(stack, getAsTextProperties(provider.getTooltip()), mouseX, mouseY);
            break;
          }
        }
      }
    }
  }
  
  protected int getWidth()
  {
    return field_230708_k_;
  }
  
  protected int getHeight()
  {
    return field_230709_l_;
  }
  
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
  protected void func_231160_c_()
  {
    super.func_231160_c_();
    
    tooltipProviders.clear();
  }
  
  @Override // addButton
  protected <T extends Widget> T func_230480_a_(T widget)
  {
    if (widget instanceof ITooltipProvider)
    {
      tooltipProviders.add((ITooltipProvider) widget);
    }
    return super.func_230480_a_(widget);
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
  
      GuiUtil.SLOT_SPRITE.render(x, y);
      if (slotBackgrounds.containsKey(slot))
      {
        slotBackgrounds.get(slot).render(x, y);
      }
    }
  }
  
  protected void renderTitle()
  {
    renderTitle(guiLeft + 8, guiTop + 6);
  }
  
  protected void renderTitle(int x, int y)
  {
    if (field_230704_d_ != null)
    {
      String title = this.field_230704_d_.getUnformattedComponentText();
      field_230712_o_.func_238405_a_(currentStack, title, x, y, 0xff404040);
    }
  }
  
  protected void renderInventoryText()
  {
    renderInventoryText(guiLeft + 8, guiTop + 73);
  }
  
  protected void renderInventoryText(int x, int y)
  {
    field_230712_o_.func_238405_a_(currentStack, playerInventory.getDisplayName().getUnformattedComponentText(), x, y, 0xff404040);
//    font.drawString(playerInventory.getDisplayName().getFormattedText(), x, y, 0xff404040);
  }
  
  protected EnergyBar getDefaultBar(BaseEnergyStorage storage)
  {
    return new EnergyBar(storage, guiLeft + 10, guiTop + 16);
  }
  
  protected void drawStringNoShadow(String string, int x, int y, int color)
  {
    field_230712_o_.func_238405_a_(currentStack, string, x, y, color);
  }
  
  protected void drawRect(int x, int y, int width, int height, int color)
  {
    GuiUtil.drawRect(x, y, width, height, color);
  }
}
