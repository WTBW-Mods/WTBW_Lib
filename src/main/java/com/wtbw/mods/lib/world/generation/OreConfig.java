package com.wtbw.mods.lib.world.generation;

import com.wtbw.mods.lib.config.SubConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

/*
  @author: Naxanria
*/
public class OreConfig extends SubConfig
{
  protected final OreBlockProvider provider;
  private final String name;
  
  private ForgeConfigSpec.BooleanValue enabled;
  private ForgeConfigSpec.IntValue maxVein;
  private ForgeConfigSpec.IntValue perChunk;
  private ForgeConfigSpec.IntValue start;
  private ForgeConfigSpec.IntValue end;
  
  private Function<String, String> langKeyProvider;
  
  public OreConfig(ForgeConfigSpec.Builder builder, String name, OreBlockProvider provider, int maxVein, int perChunk, int start, int end)
  {
    this(builder, name, provider, maxVein, perChunk, start, end, "");
  }
  
  public OreConfig(ForgeConfigSpec.Builder builder, String name, OreBlockProvider provider, int maxVein, int perChunk, int start, int end, String langPath)
  {
    super(builder);
    
    this.langKeyProvider = getDefaultLangKeyProvider(langPath);
    
    this.provider = provider;
  
    this.name = name;
  
    push(name);
  
    this.maxVein = builder
      .comment("Max size of vein", "Default: " + maxVein)
      .translation(key("max_vein"))
      .defineInRange("maxVein", maxVein, 1, 256);
  
    this.perChunk = builder
      .comment("Veins per chunk", "Default: " + perChunk)
      .translation(key("per_chunk"))
      .defineInRange("perChunk", perChunk, 1, 256);
  
    this.start = builder
      .comment("Start height of veins", "Default: " + start)
      .translation(key("start"))
      .defineInRange("start", start, 0, 256);
  
    this.end = builder
      .comment("End height of veins", "Default: " + end)
      .translation(key("end"))
      .defineInRange("end", end, 0, 256);
  
    this.enabled = builder
      .comment("Is this oregen option enabled")
      .translation(key("enabled"))
      .define("enabled", true);
  
    pop();
  }
  
  protected String key(String name)
  {
    return langKeyProvider.apply(name);
  }
  
  @Override
  protected void init()
  {
  
  }
  
  @Override
  protected void reload()
  {
  
  }
  
  public boolean isOreEnabled()
  {
    return enabled.get();
  }
  
  public int maxVeinSize()
  {
    return maxVein.get();
  }
  
  public int getPerChunk()
  {
    return perChunk.get();
  }
  
  public int getBottomOffset()
  {
    return start.get();
  }
  
  public int getTopOffset()
  {
    return start.get();
  }
  
  public int getMaxHeight()
  {
    return end.get() + 1;
  }
  
  public OreBlockProvider getProvider()
  {
    return provider;
  }
  
  public static Function<String, String> getDefaultLangKeyProvider(String path)
  {
    return (s) -> path + "." + s;
  }
}
