package com.wtbw.lib.gui.util;

import java.util.List;

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
