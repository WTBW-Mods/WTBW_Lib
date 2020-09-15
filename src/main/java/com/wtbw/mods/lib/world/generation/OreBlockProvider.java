package com.wtbw.mods.lib.world.generation;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.function.Predicate;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class OreBlockProvider
{
  Supplier<Block> block;
  RuleTest fillerBlockType;
  Predicate<BiomeLoadingEvent> validator;
  
  public OreBlockProvider(Supplier<Block> block, RuleTest fillerBlockType, Predicate<BiomeLoadingEvent> validator)
  {
    this.block = block;
    this.fillerBlockType = fillerBlockType;
    this.validator = validator;
  }
  
  public Block getBlock()
  {
    return block.get();
  }
  
  public RuleTest getFillerBlockType()
  {
    return fillerBlockType;
  }
  
  public boolean isBiomeValid(BiomeLoadingEvent event)
  {
    return validator.test(event);
  }
  
  public static class Validators
  {
    public static final Predicate<BiomeLoadingEvent> OVERWORLD = event -> event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND;
    public static final Predicate<BiomeLoadingEvent> NETHER = event -> event.getCategory() == Biome.Category.NETHER;
    public static final Predicate<BiomeLoadingEvent> THE_END = event -> event.getCategory() == Biome.Category.THEEND;
  }
}
