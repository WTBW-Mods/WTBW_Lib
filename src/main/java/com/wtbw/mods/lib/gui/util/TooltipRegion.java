package com.wtbw.mods.lib.gui.util;

/*
  @author: Naxanria
*/
public abstract class TooltipRegion extends Region implements ITooltipProvider
{
  public TooltipRegion(int x, int y, int width, int height)
  {
    super(x, y, width, height);
  }
  
  @Override
  public boolean isHover(int mouseX, int mouseY)
  {
    return isInside(mouseX, mouseY);
  }
}
