package com.wtbw.mods.lib.config;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

  protected static String[] decompose(String resourceName, char splitOn) {
    String[] astring = new String[]{"minecraft", resourceName};
    int i = resourceName.indexOf(splitOn);
    if (i >= 0) {
      astring[1] = resourceName.substring(i + 1, resourceName.length());
      if (i >= 1) {
        astring[0] = resourceName.substring(0, i);
      }
    }

    return astring;
  }

  private static boolean isPathValid(String pathIn) {
    return pathIn.chars().allMatch((p_217857_0_) -> {
      return p_217857_0_ == 95 || p_217857_0_ == 45 || p_217857_0_ >= 97 && p_217857_0_ <= 122 || p_217857_0_ >= 48 && p_217857_0_ <= 57 || p_217857_0_ == 47 || p_217857_0_ == 46;
    });
  }

  /**
   * Returns true if given namespace only consists of allowed characters.
   */
  private static boolean isValidNamespace(String namespaceIn) {
    return namespaceIn.chars().allMatch((p_217859_0_) -> {
      return p_217859_0_ == 95 || p_217859_0_ == 45 || p_217859_0_ >= 97 && p_217859_0_ <= 122 || p_217859_0_ >= 48 && p_217859_0_ <= 57 || p_217859_0_ == 46;
    });
  }

  /**
   * Checks if the specified resource name (namespace and path) contains invalid characters.
   */

  public static boolean isResouceNameValid(String resourceName) {
    String[] astring = decompose(resourceName, ':');
    return isValidNamespace(org.apache.commons.lang3.StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0]) && isPathValid(astring[1]);
  }

  public static boolean isResourceLocation(String resource)
  {
    return isResouceNameValid(resource);
  }
}
