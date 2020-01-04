package com.wtbw.mods.lib.tile.util;

import com.wtbw.mods.lib.gui.util.ClickType;

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
    getControl().tileEntity.markDirty();
    return getControl().handleButton(buttonID, clickType);
  }
}
