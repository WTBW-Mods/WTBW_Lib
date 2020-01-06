package com.wtbw.mods.lib.gui.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wtbw.mods.lib.gui.util.sprite.NineSliceSprite;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.gui.util.sprite.SpriteMap;
import com.wtbw.mods.lib.network.ButtonClickedPacket;
import com.wtbw.mods.lib.network.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/*
  @author: Naxanria
*/
public class GuiUtil extends AbstractGui
{
  public static final ResourceLocation WIDGETS = Widget.WIDGETS_LOCATION;

  public static final SpriteMap GUI_MAP = new SpriteMap(256, new ResourceLocation("textures/gui/container/generic_54.png"));
  public static final Sprite SLOT_SPRITE;
  public static final Sprite INVENTORY_SPRITE;
  
  public static final NineSliceSprite GUI_NINE_SLICE;
  
  public static final SpriteMap WIDGETS_MAP = new SpriteMap(256, WIDGETS);
  public static final NineSliceSprite BUTTON_NINE_SLICE_DISABLED;
  public static final NineSliceSprite BUTTON_NINE_SLICE_NORMAL;
  public static final NineSliceSprite BUTTON_NINE_SLICE_HOVER;
  static
  {
    int yOff = 46;
    int w = 10;
    int h = 5;
    BUTTON_NINE_SLICE_DISABLED = NineSliceSprite.create
      (
        WIDGETS_MAP, w, h,
      
        0, yOff,
        3, yOff,
        200 - w, yOff,
      
        0, yOff + 3,
        3, yOff + 3,
        200 - w, yOff + 3,
      
        0, yOff + 20 - h,
        3, yOff + 20 - h,
        200 - w, yOff + 20 - h
      );
  
    yOff += 20;
    BUTTON_NINE_SLICE_NORMAL = NineSliceSprite.create
      (
        WIDGETS_MAP, w, h,
      
        0, yOff,
        3, yOff,
        200 - w, yOff,
      
        0, yOff + 3,
        3, yOff + 3,
        200 - w, yOff + 3,
      
        0, yOff + 20 - h,
        3, yOff + 20 - h,
        200 - w, yOff + 20 - h
      );
  
    yOff += 20;
    BUTTON_NINE_SLICE_HOVER = NineSliceSprite.create
      (
        WIDGETS_MAP, w, h,
      
        0, yOff,
        3, yOff,
        200 - w, yOff,
      
        0, yOff + 3,
        3, yOff + 3,
        200 - w, yOff + 3,
      
        0, yOff + 20 - h,
        3, yOff + 20 - h,
        200 - w, yOff + 20 - h
      );
    
    GUI_NINE_SLICE = NineSliceSprite.create
      (
        GUI_MAP, 7, 7,
        0, 0,
        7, 0,
        176 - 7, 0,
        
        0, 7,
        7, 7,
        176 - 7, 7,
        
        0, 222 - 7,
        7, 222 - 7,
        176 - 7, 222 - 7
      );
    
    SLOT_SPRITE = GUI_MAP.getSprite(7, 17, 18, 18);
    INVENTORY_SPRITE = GUI_MAP.getSprite(7, 139, 161, 75);
  }
  
  public static void sendButton(int id, BlockPos pos, ClickType clickType)
  {
    Networking.INSTANCE.sendToServer(new ButtonClickedPacket(id, pos, clickType));
  }
  
  public static boolean inRegion(int x, int y, int rX, int rY, int rWidth, int rHeight)
  {
    return x >= rX && x < rX + rWidth
      && y >= rY && y < rY + rHeight;
  }
  
  public static void renderTexture(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, ResourceLocation textureLocation)
  {
    renderTexture(x, y, width, height, u, v, textureWidth, textureHeight, 0xffffffff, textureLocation);
  }
  
  public static void renderTexture(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int color, ResourceLocation textureLocation)
  {
    float a = ((color >> 24) & 0xff) / 256f;
    float r = ((color >> 16) & 0xff) / 256f;
    float g = ((color >> 8) & 0xff) / 256f;
    float b = (color & 0xff) / 256f;
    Minecraft.getInstance().getTextureManager().bindTexture(textureLocation);

    
    RenderSystem.color4f(r,g , b, a);
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    blit(x, y, u, v, width, height, textureWidth, textureHeight);
  }
  
  //todo: scaleable textures, make buttons be variable height
  
  public static void renderButton(int x, int y, int width, boolean hover)
  {
    renderButton(x, y, width, 20, hover);
  }
  
  public static void renderButton(int x, int y, int width, int height, boolean hover)
  {
    renderButton(x, y, width, height, hover, true);
  }
  
  public static void renderButton(int x, int y, int width, boolean hover, boolean enabled)
  {
    renderButton(x, y, width, 20, hover, enabled);
  }
  
  public static void renderButton(int x, int y, int width, int height, boolean hover, boolean enabled)
  {
    if (enabled)
    {
      if (hover)
      {
        BUTTON_NINE_SLICE_HOVER.render(x, y, width, height);
      }
      else
      {
        BUTTON_NINE_SLICE_NORMAL.render(x, y, width, height);
      }
    }
    else
    {
      BUTTON_NINE_SLICE_DISABLED.render(x, y, width, height);
    }
    
//    int buttonX = 0;
//    int buttonEndX = 200;
//    int offset = 20;
//    int buttonY = 66;
//    if (!enabled)
//    {
//      buttonY -= offset;
//    }
//    else if (hover)
//    {
//      buttonY += offset;
//    }
//
//    int height = 20;
//
//    // start part
//    renderTexture(x, y, 3, height, buttonX, buttonY, 256, 256, WIDGETS);
//    width = width - 3;
//    renderTexture(x + 3, y, width, height,buttonEndX - width, buttonY, 256, 256, WIDGETS);
  }
  
  public static void renderRepeating(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int textureWidth, int textureHeight, int color, ResourceLocation textureLocation)
  {
    int renderWidth = width;
    int xp = x;
    do
    {
      int renderHeight = height;
      int yp = y;
      do
      {
        renderTexture(xp, yp, renderWidth, renderHeight, u, v, textureWidth, textureHeight, color, textureLocation);
        renderHeight -= vHeight;
        yp += vHeight;
      }
      while (vHeight <= renderHeight);
      renderWidth -= uWidth;
      xp += uWidth;
    }
    while (uWidth <= renderWidth);
  }
  
  public static void renderGui(int x, int y)
  {
    renderGui(x, y, 175, 165);
  }
  
  public static void renderGui(int x, int y, int width, int height)
  {
    GUI_NINE_SLICE.render(x, y, width, height);
  }
  
  public static void renderSlotBackground(Slot slot)
  {
    renderSlotBackground(slot.xPos, slot.yPos);
  }
  
  public static void renderSlotBackground(int x, int y)
  {
    SLOT_SPRITE.render(x, y);
  }
  
  public static void renderInventoryBackground(int x, int y)
  {
    INVENTORY_SPRITE.render(x, y);
  }
  
  public static void drawRect(int x, int y, int width, int height, int color)
  {
    fill(x, y, x + width, y + height, color);
  }
}
