package com.wtbw.mods.lib.item;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class BaseItemProperties extends Item.Properties
{
  private int burnTime = 0;
  private List<ITextComponent> tooltip;
  
  public int getBurnTime()
  {
    return burnTime;
  }
  
  public BaseItemProperties setBurnTime(int burnTime)
  {
    this.burnTime = burnTime;
    return this;
  }
  
  public List<ITextComponent> getTooltip()
  {
    return tooltip;
  }
  
  public BaseItemProperties addTooltip(String tooltip)
  {
    return addTooltip(new TranslationTextComponent(tooltip));
  }
  
  public BaseItemProperties addTooltip(ITextComponent tooltip)
  {
    if (this.tooltip == null)
    {
      this.tooltip = new ArrayList<>();
    }
    
    this.tooltip.add(tooltip);
    
    return this;
  }
  
  @Override
  public BaseItemProperties food(Food foodIn)
  {
    return (BaseItemProperties) super.food(foodIn);
  }
  
  @Override
  public BaseItemProperties maxStackSize(int maxStackSizeIn)
  {
    return (BaseItemProperties) super.maxStackSize(maxStackSizeIn);
  }
  
  @Override
  public BaseItemProperties defaultMaxDamage(int maxDamageIn)
  {
    return (BaseItemProperties) super.defaultMaxDamage(maxDamageIn);
  }
  
  @Override
  public BaseItemProperties maxDamage(int maxDamageIn)
  {
    return (BaseItemProperties) super.maxDamage(maxDamageIn);
  }
  
  @Override
  public BaseItemProperties containerItem(Item containerItemIn)
  {
    return (BaseItemProperties) super.containerItem(containerItemIn);
  }
  
  @Override
  public BaseItemProperties group(ItemGroup groupIn)
  {
    return (BaseItemProperties) super.group(groupIn);
  }
  
  @Override
  public BaseItemProperties rarity(Rarity rarityIn)
  {
    return (BaseItemProperties) super.rarity(rarityIn);
  }
  
  @Override
  public BaseItemProperties setNoRepair()
  {
    return (BaseItemProperties) super.setNoRepair();
  }
  
  @Override
  public BaseItemProperties addToolType(ToolType type, int level)
  {
    return (BaseItemProperties) super.addToolType(type, level);
  }
  
  @Override
  public BaseItemProperties setISTER(Supplier<Callable<ItemStackTileEntityRenderer>> teisr)
  {
    return (BaseItemProperties) super.setISTER(teisr);
  }
}
