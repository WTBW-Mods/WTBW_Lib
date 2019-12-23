package com.wtbw.lib.gui.util;

import java.util.List;

/*
  @author: Naxanria
*/
public interface ITooltipProvider
{
  boolean isHover(int mouseX, int mouseY);

  List<String> getTooltip();
}
