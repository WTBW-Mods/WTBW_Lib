package com.wtbw.mods.lib.gui.util;

import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.Utilities;

import java.util.List;

/*
  @author: Naxanria
*/
public class EnergyBar extends ProgressBar implements ITooltipProvider
{
  protected final BaseEnergyStorage storage;
  public EnergyBar(BaseEnergyStorage storage, int x, int y)
  {
    this(storage, x, y, 20, 54);
  }
  
  public EnergyBar(BaseEnergyStorage storage, int x, int y, int width, int height)
  {
    super(x, y, width, height, storage::getMaxEnergyStored, storage::getEnergyStored);
    this.storage = storage;
    gradientColor(0xffff0000, 0xff00ff00);
  }
  
  @Override
  public boolean isHover(int mouseX, int mouseY)
  {
    return mouseOver(mouseX, mouseY);
  }
  
  @Override
  public List<String> getTooltip()
  {
    return Utilities.listOf(storage.getEnergyStored() + "/" + storage.getMaxEnergyStored() + " RF");
  }
}
