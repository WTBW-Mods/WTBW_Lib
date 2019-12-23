package com.wtbw.lib.tile.util;

import com.wtbw.lib.gui.util.ClickType;

/*
  @author: Naxanria
*/
public interface IRedstoneControlled extends IButtonHandler
{
  RedstoneControl getControl();
  
  default RedstoneMode getRedstoneMode()
  {
    return getControl().getMode();
  }
  
  default RedstoneMode[] availableModes()
  {
    return RedstoneMode.values();
  }
  
  @Override
  default boolean handleButton(int buttonID, ClickType clickType)
  {
    return getControl().handleButton(buttonID, clickType);
  }
}
