package com.wtbw.mods.lib.gui.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wtbw.mods.lib.gui.util.sprite.NineSliceSprite;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.gui.util.sprite.SpriteMap;
import com.wtbw.mods.lib.network.ButtonClickedPacket;
import com.wtbw.mods.lib.network.Networking;
import com.wtbw.mods.lib.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

/*
  @author: Naxanria
*/
public class GuiUtil extends AbstractGui
{
  public static final int WHITE = 0xffffffff;
  public static final int LIGHT_GRAY = 0xffaaaaaa;
  public static final int GRAY = 0xff888888;
  public static final int DARK_GRAY = 0xff444444;
  public static final int BLACK = 0xff000000;
  public static final int RED = 0xffff0000;
  public static final int GREEN = 0xff00ff00;
  public static final int BLUE = 0xff0000ff;
  
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
  
  public static void renderTexture(MatrixStack stack, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, ResourceLocation textureLocation)
  {
    renderTexture(stack, x, y, width, height, u, v, textureWidth, textureHeight, 0xffffffff, textureLocation);
  }
  
  public static void renderTexture(MatrixStack stack, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int color, ResourceLocation textureLocation)
  {
    float a = ((color >> 24) & 0xff) / 256f;
    float r = ((color >> 16) & 0xff) / 256f;
    float g = ((color >> 8) & 0xff) / 256f;
    float b = (color & 0xff) / 256f;
    Minecraft.getInstance().getTextureManager().bindTexture(textureLocation);

    
    RenderSystem.color4f(r, g , b, a);
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    blit(stack, x, y, u, v, width, height, textureWidth, textureHeight);
  }
  
  //todo: scaleable textures, make buttons be variable height
  
  public static void renderButton(MatrixStack stack, int x, int y, int width, boolean hover)
  {
    renderButton(stack, x, y, width, 20, hover);
  }
  
  public static void renderButton(MatrixStack stack, int x, int y, int width, int height, boolean hover)
  {
    renderButton(stack, x, y, width, height, hover, true);
  }
  
  public static void renderButton(MatrixStack stack, int x, int y, int width, boolean hover, boolean enabled)
  {
    renderButton(stack, x, y, width, 20, hover, enabled);
  }
  
  public static void renderButton(MatrixStack stack, int x, int y, int width, int height, boolean hover, boolean enabled)
  {
    if (enabled)
    {
      if (hover)
      {
        BUTTON_NINE_SLICE_HOVER.render(stack, x, y, width, height);
      }
      else
      {
        BUTTON_NINE_SLICE_NORMAL.render(stack, x, y, width, height);
      }
    }
    else
    {
      BUTTON_NINE_SLICE_DISABLED.render(stack, x, y, width, height);
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
  
  public static void renderRepeating(MatrixStack stack, int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int textureWidth, int textureHeight, int color, ResourceLocation textureLocation)
  {
    int renderWidth = width;
    int xp = x;
    do
    {
      int renderHeight = height;
      int yp = y;
      do
      {
        renderTexture(stack, xp, yp, renderWidth, renderHeight, u, v, textureWidth, textureHeight, color, textureLocation);
        renderHeight -= vHeight;
        yp += vHeight;
      }
      while (vHeight <= renderHeight);
      renderWidth -= uWidth;
      xp += uWidth;
    }
    while (uWidth <= renderWidth);
  }
  
  // https://github.com/mekanism/Mekanism/blob/77aef85572e21d3a8c65b6b21c87d9577139c161/src/main/java/mekanism/client/gui/GuiUtils.java drawTiledSprite
  public static void renderRepeatingSprite(MatrixStack stack, int xPosition, int yPosition, int yOffset, int width, int height, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int zLevel)
  {
//    if (width == 0 || height == 0 || textureWidth == 0 || textureHeight == 0)
//    {
//      return;
//    }
//
//    Minecraft minecraft = Minecraft.getInstance();
//    TextureManager textureManager = minecraft.getTextureManager();
//    textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
//
////    y += height;
////    height -= textureHeight;
//
//    int xCount = width / textureWidth;
//    int xRemain = width - (xCount * textureWidth);
//    int yCount = height / textureHeight;
//    int yRemain = height - (yCount * textureHeight);
//
//    float uMin = sprite.getMinU();
//    float uMax = sprite.getMaxU();
//    float vMin = sprite.getMinV();
//    float vMax = sprite.getMaxV();
//    float uDiff = uMax - uMin;
//    float vDiff= vMax - vMin;
//
//    RenderSystem.enableBlend();
//    RenderSystem.enableAlphaTest();
//
//    BufferBuilder buffer = Tessellator.getInstance().getBuffer();
//    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//
//    for (int xTile = 0; xTile <= xCount; xTile++)
//    {
//      int w = (xTile == xCount) ? xRemain : textureWidth;
//      if (w == 0)
//      {
//        break;
//      }
//
//      int xStart = x + (xTile * textureWidth);
//      int xEnd = x + w;
//      float uStart = uMin;
//      float uEnd = uDiff * (w / (float) textureWidth) + uStart;
//
//      for (int yTile = 0; yTile <= yCount; yTile++)
//      {
//        int h = (yTile == yCount) ? yRemain : textureHeight;
//        if (h == 0)
//        {
//          break;
//        }
//
//        int yStart = y + (yTile * textureHeight);
//        int yEnd = y + h;
//        float vStart = vMin;
//        float vEnd = vDiff * (h / (float) textureHeight) + vStart;
//
//        buffer.pos(xStart, yEnd, z).tex(uStart, vEnd).endVertex();
//        buffer.pos(xEnd, yEnd, z).tex(uEnd, vEnd).endVertex();
//        buffer.pos(xEnd, yStart, z).tex(uEnd, vStart).endVertex();
//        buffer.pos(xStart, yStart, z).tex(uStart, vStart).endVertex();
//      }
//    }
//
//    buffer.finishDrawing();
//    WorldVertexBufferUploader.draw(buffer);
//    RenderSystem.disableAlphaTest();
//    RenderSystem.disableBlend();
    
    
    if (width == 0 || height == 0 || textureWidth == 0 || textureHeight == 0) {
      return;
    }
    bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
    int xTileCount = width / textureWidth;
    int xRemainder = width - (xTileCount * textureWidth);
    int yTileCount = height / textureHeight;
    int yRemainder = height - (yTileCount * textureHeight);
    int yStart = yPosition + yOffset;
    float uMin = sprite.getMinU();
    float uMax = sprite.getMaxU();
    float vMin = sprite.getMinV();
    float vMax = sprite.getMaxV();
    float uDif = uMax - uMin;
    float vDif = vMax - vMin;
    RenderSystem.enableBlend();
    RenderSystem.enableAlphaTest();
    BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();
    vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    for (int xTile = 0; xTile <= xTileCount; xTile++) {
      int targetWidth = (xTile == xTileCount) ? xRemainder : textureWidth;
      if (targetWidth == 0) {
        break;
      }
      int x = xPosition + (xTile * textureWidth);
      int maskRight = textureWidth - targetWidth;
      int shiftedX = x + textureWidth - maskRight;
      float uMaxLocal = uMax - (uDif * maskRight / textureWidth);
      for (int yTile = 0; yTile <= yTileCount; yTile++) {
        int targetHeight = (yTile == yTileCount) ? yRemainder : textureHeight;
        if (targetHeight == 0) {
          //Note: We don't want to fully break out because our height will be zero if we are looking to
          // draw the remainder, but there is no remainder as it divided evenly
          break;
        }
        int y = yStart - ((yTile + 1) * textureHeight);
        int maskTop = textureHeight - targetHeight;
        float vMaxLocal = vMax - (vDif * maskTop / textureHeight);
        vertexBuffer.pos(x, y + textureHeight, zLevel).tex(uMin, vMaxLocal).endVertex();
        vertexBuffer.pos(shiftedX, y + textureHeight, zLevel).tex(uMaxLocal, vMaxLocal).endVertex();
        vertexBuffer.pos(shiftedX, y + maskTop, zLevel).tex(uMaxLocal, vMin).endVertex();
        vertexBuffer.pos(x, y + maskTop, zLevel).tex(uMin, vMin).endVertex();
      }
    }
    vertexBuffer.finishDrawing();
    WorldVertexBufferUploader.draw(vertexBuffer);
    RenderSystem.disableAlphaTest();
    RenderSystem.disableBlend();
  }
  
  public static void bindTexture(ResourceLocation texture)
  {
    Minecraft.getInstance().textureManager.bindTexture(texture);
  }
  
  public static void renderGui(MatrixStack stack, int x, int y)
  {
    renderGui(stack, x, y, 175, 165);
  }
  
  public static void renderGui(MatrixStack stack, int x, int y, int width, int height)
  {
    GUI_NINE_SLICE.render(stack, x, y, width, height);
  }
  
  public static void renderSlotBackground(MatrixStack stack, Slot slot)
  {
    renderSlotBackground(stack, slot.xPos, slot.yPos);
  }
  
  public static void renderSlotBackground(MatrixStack stack, int x, int y)
  {
    SLOT_SPRITE.render(stack, x, y);
  }
  
  public static void renderInventoryBackground(MatrixStack stack, int x, int y)
  {
    INVENTORY_SPRITE.render(stack, x, y);
  }
  
  public static void drawRect(MatrixStack stack, int x, int y, int width, int height, int color)
  {
    fill(stack, x, y, x + width, y + height, color);
  }
  
  public static void color(DyeColor color)
  {
    color(color.getColorValue());
  }
  
  public static void color(int color)
  {
    float[] rgba = ColorUtil.getRGBAf(color);
    RenderSystem.color4f(rgba[0], rgba[1], rgba[2], rgba[3]);
  }
}
