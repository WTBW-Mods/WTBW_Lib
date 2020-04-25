package com.wtbw.mods.lib.gui.util;

import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.IFluidTank;

import java.util.List;

/*
  @author: Naxanria
*/
public class FluidBar extends ProgressBar implements ITooltipProvider
{
  public final IFluidTank tank;
  public boolean abbreviate = false;
  
  public FluidBar(IFluidTank tank, int x, int y)
  {
    this(tank, x, y, 20, 54);
  }
  
  public FluidBar(IFluidTank tank, int x, int y, int width, int height)
  {
    super(x, y, width, height, tank::getCapacity, tank::getFluidAmount);
    this.tank = tank;
    update();
  }
  
  @Override
  public void update()
  {
    super.update();
  
    Fluid fluid = tank.getFluid().getFluid();
    
    if (fluid == Fluids.EMPTY)
    {
      setTexture(null);
    }
    else
    {
      FluidAttributes attributes = fluid.getAttributes();
      ResourceLocation stillTexture = attributes.getStillTexture();
      int color = attributes.getColor();
  
      setTexture(stillTexture, TextureMode.REPEAT, true);
      setColor(color);
    }
  }
  
  public FluidBar setAbbreviate(boolean abbreviate)
  {
    this.abbreviate = abbreviate;
    return this;
  }
  
  @Override
  public boolean isHover(int mouseX, int mouseY)
  {
    return mouseOver(mouseX, mouseY);
  }
  
  @Override
  public List<String> getTooltip()
  {
    return Utilities.listOf(Utilities.getTooltip(tank, abbreviate));
  }
}
