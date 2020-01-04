package com.wtbw.mods.lib.config;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

/*
  @author: Naxanria
*/
public abstract class BaseConfig
{
  protected final ForgeConfigSpec.Builder builder;
  protected final String modid;
  
  public BaseConfig(String modid, ForgeConfigSpec.Builder builder)
  {
    this.builder = builder;
    this.modid = modid;
  }
  
  protected ForgeConfigSpec.Builder push(String name)
  {
    return builder.push(name);
  }
  
  protected ForgeConfigSpec.Builder pop()
  {
    return builder.pop();
  }
  
  protected String key(String name)
  {
    return modid + ".config.client." + name;
  }
  
  public static boolean isResourceLocation(String resource)
  {
    return ResourceLocation.isResouceNameValid(resource);
  }
}
