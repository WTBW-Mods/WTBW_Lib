package com.wtbw.mods.lib.tile.util;

/*
  @author: Naxanria
*/
public enum RedstoneMode
{
  IGNORE,
  ON,
  OFF,
  PULSE;
  
  public static final RedstoneMode[] noPulse = new RedstoneMode[]{ IGNORE, ON, OFF };
  public static final RedstoneMode[] onOff = new RedstoneMode[]{ ON, OFF };
  
}
