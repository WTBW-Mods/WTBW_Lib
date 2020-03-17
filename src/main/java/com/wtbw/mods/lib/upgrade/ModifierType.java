package com.wtbw.mods.lib.upgrade;

import com.wtbw.mods.lib.util.TextComponentBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public class ModifierType
{
  public static final ModifierType SPEED = new ModifierType("speed");
  public static final ModifierType POWER_USAGE = new ModifierType("power_usage", false);
  public static final ModifierType POWER_CAPACITY = new ModifierType("power_capacity");
  public static final ModifierType RANGE = new ModifierType("range")
  {
    @Override
    protected ITextComponent typeInfo(float value)
    {
      return TextComponentBuilder.create(String.valueOf((int) value), true).aqua()
        .nextTranslate("wtbw.modifier.range.blocks")
        .build();
    }
  };
  
  public final String name;
  private final boolean moreIsPositive;
  
  public ModifierType(String name)
  {
    this(name, true);
  }
  
  public ModifierType(String name, boolean moreIsPositive)
  {
    this.name = name;
    this.moreIsPositive = moreIsPositive;
  }
  
  public String getTranslationKey()
  {
    return "wtbw.modifier." + name.toLowerCase();
  }
  
  public String getDescriptionKey()
  {
    return getTranslationKey() + ".description";
  }
  
  public boolean isPositive(float value)
  {
    return (value < 1) != moreIsPositive;
  }
  
  @OnlyIn(Dist.CLIENT)
  public List<ITextComponent> getInformation(ITooltipFlag tooltipFlag, float value)
  {
    boolean positive = isPositive(value);
    List<ITextComponent> info = new ArrayList<>();
  
    TextComponentBuilder builder = TextComponentBuilder.createTranslated(getTranslationKey());
    if (positive)
    {
      builder.green();
    }
    else
    {
      builder.red();
    }
  
    builder.next(" ").next(typeInfo(value));
    
    info.add(builder.build());
  
    if (Screen.hasShiftDown())
    {
      info.add(TextComponentBuilder.createTranslated(getDescriptionKey()).gray().build());
    }
    
    return info;
  }
  
  protected ITextComponent typeInfo(float value)
  {
    int perc;
    String sign;
    if (value < 1)
    {
      perc = (int) ((1 - value) * 100);
      sign = "-";
    }
    else
    {
      perc = (int) ((value - 1) * 100);
      sign = "+";
    }
    
    return TextComponentBuilder.create(sign + perc + "%").aqua().build();
  }
}
