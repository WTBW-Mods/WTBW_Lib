package com.wtbw.mods.lib.upgrade;

import java.util.Map;

/*
  @author: Naxanria
*/
public interface IUpgradeProvider
{
  Map<ModifierType, Float> modifierMap();
  int upgradeCost();
}
