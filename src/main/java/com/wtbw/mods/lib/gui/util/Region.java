package com.wtbw.mods.lib.gui.util;

/*
  @author: Naxanria
*/
public class Region
{
  public int x;
  public int y;
  public int width;
  public int height;
  
  public Region(int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public boolean isInside(int px, int py)
  {
    return px >= x && px < x + width
      && py >= y && py < y + height;
  }
}
