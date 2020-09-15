package com.wtbw.mods.lib.world.generation;

import com.wtbw.mods.lib.WTBWLib;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class WorldGenManager
{
  private static final List<Supplier<OreConfig>> oreConfigs = new ArrayList<>();
  private static final Map<OreConfig, ConfiguredFeature<?, ?>> featureMap = new HashMap<>();
  
  public static void registerOregen(Supplier<OreConfig> config)
  {
    oreConfigs.add(config);
  }
  
  public static void onWorldGen(final BiomeLoadingEvent event)
  {
    for (Supplier<OreConfig> supplier :
      oreConfigs)
    {
      OreConfig oreConfig = supplier.get();
      if (!oreConfig.isOreEnabled())
      {
        continue;
      }
      
      if (!featureMap.containsKey(oreConfig))
      {
        featureMap.put(oreConfig, getOreFeature(oreConfig, Feature.ORE));
      }
  
      ConfiguredFeature<?, ?> feature = featureMap.get(oreConfig);
  
      OreBlockProvider provider = oreConfig.provider;
      if (provider.isBiomeValid(event))
      {
        event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
      }
    }
  }
  
  private static ConfiguredFeature<?, ?> getOreFeature(OreConfig config, Feature<OreFeatureConfig> feature)
  {
    OreBlockProvider provider = config.getProvider();
    
    if (config.isOreEnabled())
    {
      WTBWLib.LOGGER.info("Creating ore config {} max: {} bottom: {} top: {}", provider.getBlock().getRegistryName().toString(), config.getMaxHeight(), config.getBottomOffset(), config.getTopOffset());
      
      return feature.withConfiguration(new OreFeatureConfig(provider.getFillerBlockType(), provider.getBlock().getDefaultState(), config.maxVeinSize()))
        .func_242733_d(config.getMaxHeight()).func_242728_a().func_242731_b(config.getPerChunk());
    }
    
    return null;
  }
}
