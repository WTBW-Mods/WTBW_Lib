package com.wtbw.mods.lib.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;

/*
  @author: Naxanria
*/
public class WorldGenUtil
{
  public static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerFeature(String modid, String name, ConfiguredFeature<FC, ?> feature)
  {
    return Registry.register(WorldGenRegistries.field_243653_e, new ResourceLocation(modid, name), feature);
  }
}
