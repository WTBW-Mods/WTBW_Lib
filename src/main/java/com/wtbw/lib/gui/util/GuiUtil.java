package com.wtbw.lib.gui.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wtbw.lib.network.ButtonClickedPacket;
import com.wtbw.lib.network.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/*
  @author: Naxanria
*/
public class GuiUtil extends AbstractGui
{
  public static final ResourceLocation WIDGETS = Widget.WIDGETS_LOCATION;
  private static ResourceLocation lastTexture = null;
  
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
    if (textureLocation != lastTexture)
    {
      Minecraft.getInstance().getTextureManager().bindTexture(textureLocation);
      lastTexture = textureLocation;
    }
    
    RenderSystem.color4f(r,g , b, a);
    RenderSystem.enableBlend();
    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    blit(x, y, u, v, width, height, textureWidth, textureHeight);
  }
  
  //todo: scaleable textures, make buttons be variable height
  
  public static void renderButton(int x, int y, int width, boolean hover)
  {
    int buttonX = 0;
    int buttonEndX = 200;
    int hoverOffset = 20;
    int buttonY = 66 + (hover ? hoverOffset : 0);
    int height = 20;
    
    
    // start part
    renderTexture(x, y, 3, height, buttonX, buttonY, 256, 256, WIDGETS);
    width = width - 3;
    renderTexture(x + 3, y, width, height,buttonEndX - width, buttonY, 256, 256, WIDGETS);
  }
}
