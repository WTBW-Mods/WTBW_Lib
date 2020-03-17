package com.wtbw.mods.lib.item.upgrade;

import com.wtbw.mods.lib.upgrade.IUpgradeProvider;
import com.wtbw.mods.lib.upgrade.ModifierType;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/*
  @author: Naxanria
*/
public class BaseUpgradeItem extends Item implements IUpgradeProvider
{
  protected final Map<ModifierType, Float> modifiers;
  protected final int upgradeCost;
  
  public BaseUpgradeItem(Properties properties, Map<ModifierType, Float> modifiers)
  {
    this(properties, 0, modifiers);
  }
  
  public BaseUpgradeItem(Properties properties, int upgradeCost, Map<ModifierType, Float> modifiers)
  {
    super(properties);
  
    this.modifiers = modifiers;
    this.upgradeCost = upgradeCost;
  }
  
  @Override
  public Map<ModifierType, Float> modifierMap()
  {
    return modifiers;
  }
  
  @Override
  public int upgradeCost()
  {
    return upgradeCost;
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    for (Map.Entry<ModifierType, Float> entry : modifiers.entrySet())
    {
      ModifierType type = entry.getKey();
      float value = entry.getValue();
      tooltip.addAll(type.getInformation(flagIn, value));
    }
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
