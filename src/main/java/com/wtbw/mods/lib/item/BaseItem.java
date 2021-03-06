package com.wtbw.mods.lib.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class BaseItem extends Item
{
  protected final BaseItemProperties itemProperties;
  
  public BaseItem(BaseItemProperties properties)
  {
    super(properties);
  
    itemProperties = properties;
  }
  
  @Override
  public int getBurnTime(ItemStack itemStack)
  {
    return itemProperties.getBurnTime();
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    if (itemProperties.getTooltip() != null)
    {
      tooltip.addAll(itemProperties.getTooltip());
    }
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
