package com.wtbw.mods.lib.upgrade;

import com.wtbw.mods.lib.WTBWLib;

/*
  @author: Naxanria
*/
public class ModifierType
{
  public static final ModifierType SPEED = new ModifierType("speed");
  public static final ModifierType POWER_USAGE = new ModifierType("power_usage");
  public static final ModifierType POWER_CAPACITY = new ModifierType("power_capacity");
  public static final ModifierType RANGE = new ModifierType("range");
  
  public final String name;
  
  public ModifierType(String name)
  {
    this.name = name;
  }
  
  public String getTranslationKey()
  {
    return "wtbw.modifier." + name.toLowerCase();
  }
  
  public String getDescriptionKey()
  {
    return getTranslationKey() + ".description";
  }
}
