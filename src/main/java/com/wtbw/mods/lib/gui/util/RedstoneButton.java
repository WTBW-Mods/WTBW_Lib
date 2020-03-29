package com.wtbw.mods.lib.gui.util;

import com.wtbw.mods.lib.WTBWLib;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.gui.util.sprite.SpriteMap;
import com.wtbw.mods.lib.tile.util.IRedstoneControlled;
import com.wtbw.mods.lib.tile.util.RedstoneMode;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public class RedstoneButton<TE extends TileEntity & IRedstoneControlled> extends Button implements ITooltipProvider
{
  public static final ResourceLocation ICONS = new ResourceLocation(WTBWLib.MODID, "textures/gui/redstone_buttons.png");
  private final static SpriteMap SPRITE_MAP = new SpriteMap(64, ICONS);
  private final static Sprite SPRITE_BACKGROUND = SPRITE_MAP.getSprite(16, 0, 24, 26);
  private final static Sprite SPRITE_IGNORE = SPRITE_MAP.getSprite(0, getTextureYOffset(RedstoneMode.IGNORE), 16);
  private final static Sprite SPRITE_ON = SPRITE_MAP.getSprite(0, getTextureYOffset(RedstoneMode.ON), 16);
  private final static Sprite SPRITE_OFF = SPRITE_MAP.getSprite(0, getTextureYOffset(RedstoneMode.OFF), 16);
  private final static Sprite SPRITE_PULSE = SPRITE_MAP.getSprite(0, getTextureYOffset(RedstoneMode.PULSE), 16);
  protected final static String LANG_KEY = "wtbw.redstone_control";
  
  private final IRedstoneControlled control;
  private final TE tile;
  
  private boolean drawBackdrop = true;

  public RedstoneButton(int xPos, int yPos, TE tile)
  {
    super(xPos, yPos, 24, 26, "", null);
  
    this.control = tile;
    this.tile = tile;
  }
  
  public boolean isDrawBackdrop()
  {
    return drawBackdrop;
  }
  
  public RedstoneButton<TE> setDrawBackdrop(boolean drawBackdrop)
  {
    this.drawBackdrop = drawBackdrop;
    if (drawBackdrop)
    {
      width = 24;
      height = 26;
    }
    else
    {
      width = 18;
      height = 18;
    }
    
    return this;
  }
  
  @Override
  public boolean isHover(int mouseX, int mouseY)
  {
    return isHovered();
  }
  
  @Override
  public List<String> getTooltip()
  {
    List<String> tooltip = new ArrayList<>();
    tooltip.add(TextComponentBuilder.createTranslated(LANG_KEY).bold().build().getFormattedText());
    tooltip.add(I18n.format(LANG_KEY + "." + control.getRedstoneMode().toString().toLowerCase()));
    return tooltip;
  }
  
  @Override
  public void onPress()
  {
    GuiUtil.sendButton(control.getControl().getButtonId(getNextMode()), tile.getPos(), ClickType.LEFT);
  }
  
  @Override
  public void renderButton(int mouseX, int mouseY, float partial)
  {
//    GuiUtil.renderButton(x, y, width, height, isHovered, active);
    
    Sprite sprite;
    switch (control.getRedstoneMode())
    {
      default:
      case IGNORE:
        sprite = SPRITE_IGNORE;
        break;
      case ON:
        sprite = SPRITE_ON;
        break;
      case OFF:
        sprite = SPRITE_OFF;
        break;
      case PULSE:
        sprite = SPRITE_PULSE;
        break;
    }
    
    if (drawBackdrop)
    {
      SPRITE_BACKGROUND.render(x, y);
      sprite.render(x + 5, y + 5);
    }
    else
    {
      sprite.render(x + 1, y + 1);
    }
    

//    this.renderBg(Minecraft.getInstance(), mouseX, mouseY);
  }
  
  private RedstoneMode getNextMode()
  {
    RedstoneMode mode = control.getRedstoneMode();
    RedstoneMode[] redstoneModes = control.availableModes();
    int index = (Utilities.getIndex(redstoneModes, mode) + 1) % redstoneModes.length;
    return redstoneModes[index];
  }
  
  public static int getTextureYOffset(RedstoneMode mode)
  {
    switch (mode)
    {
      default:
      case IGNORE:
        return 48;
      case ON:
        return 0;
      case OFF:
        return 16;
      case PULSE:
        return 32;
    }
  }
}
