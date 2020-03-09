package com.wtbw.mods.lib.tile.util;

import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;

/*
  @author: Naxanria
*/
public class GuiUpdateHelper
{
  public static void updateEnergy(BaseEnergyStorage storage, int[] values)
  {
    storage.setEnergy(values[0]);
    storage.setCapacity(values[1]);
    if (values.length == 4)
    {
      storage.setReceive(values[2]);
      storage.setExtract(values[3]);
    }
  }
  
  public static int[] getEnergyUpdateValues(BaseEnergyStorage storage)
  {
    return getEnergyUpdateValues(storage, false);
  }
  
  public static int[] getEnergyUpdateValues(BaseEnergyStorage storage, boolean transferValues)
  {
    int size = transferValues ? 4 : 2;
    int[] values = new int[size];
    values[0] = storage.getEnergyStored();
    values[1] = storage.getMaxEnergyStored();
    if (transferValues)
    {
      values[2] = storage.getMaxInsert();
      values[3] = storage.getMaxExtract();
    }
    
    return values;
  }
  
}
