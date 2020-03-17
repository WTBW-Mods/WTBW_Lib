package com.wtbw.mods.lib.gui.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public abstract class BaseTabbedScreen<C extends Container> extends BaseContainerScreen<C>
{
  protected List<Runnable> tabInitializers = new ArrayList<>();
  protected int currentTab = 0;
  
  public BaseTabbedScreen(C container, PlayerInventory inventory, ITextComponent title)
  {
    super(container, inventory, title);
    
  }
  
  public <T extends BaseTabbedScreen<C>> T addTabInitializer(Runnable runnable)
  {
    tabInitializers.add(runnable);
    return (T) this;
  }
  
  @Override
  protected void init()
  {
    super.init();
    tabInitializers.get(currentTab).run();
  }
  
  protected void setIndex(int index)
  {
    currentTab = MathHelper.clamp(index, 0, tabInitializers.size() - 1);
    init(minecraft, width, height);
  }
}
