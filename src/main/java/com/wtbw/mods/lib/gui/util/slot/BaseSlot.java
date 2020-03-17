package com.wtbw.mods.lib.gui.util.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/*
  @author: Naxanria
*/
public class BaseSlot extends SlotItemHandler
{
  protected boolean visible = true;
  protected boolean enabled = true;
  
  public BaseSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
  {
    super(itemHandler, index, xPosition, yPosition);
  }
  
  public boolean isVisible()
  {
    return visible;
  }
  
  public BaseSlot setVisible(boolean visible)
  {
    this.visible = visible;
    return this;
  }
  
  @Override
  public boolean isEnabled()
  {
    return enabled;
  }
  
  public BaseSlot setEnabled(boolean enabled)
  {
    this.enabled = enabled;
    return this;
  }
}
