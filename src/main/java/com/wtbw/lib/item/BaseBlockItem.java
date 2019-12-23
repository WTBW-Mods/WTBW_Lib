package com.wtbw.lib.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class BaseBlockItem extends BlockItem
{
  protected final BaseItemProperties itemProperties;
  
  public BaseBlockItem(Block blockIn, BaseItemProperties properties)
  {
    super(blockIn, properties);
    this.itemProperties = properties;
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
