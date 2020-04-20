package com.wtbw.mods.lib.util;

import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

/*
  @author: Naxanria
*/
public class TagsHelper
{
  public static boolean hasTag(FluidStack stack, String tag)
  {
    return hasTag(stack, new ResourceLocation(tag));
  }
  
  public static boolean hasTag(FluidStack stack, ResourceLocation tag)
  {
    return FluidTags.getCollection().get(tag).contains(stack.getFluid());
  }
}
