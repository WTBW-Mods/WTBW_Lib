package com.wtbw.mods.lib.item;

import com.wtbw.mods.lib.item.util.ItemEnergyCapabilityProvider;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class BasePoweredItem extends Item
{
  protected static final BaseEnergyStorage EMPTY = new BaseEnergyStorage(0);
  public final int capacity;
  public final int extract;
  public final int insert;
  
  public BasePoweredItem(Properties properties, int capacity)
  {
    this(properties, capacity, capacity);
  }
  
  public BasePoweredItem(Properties properties, int capacity, int transfer)
  {
    this(properties, capacity, transfer, transfer);
  }
  
  public BasePoweredItem(Properties properties, int capacity, int extract, int insert)
  {
    super(properties.maxStackSize(1));
    this.capacity = capacity;
    this.extract = extract;
    this.insert = insert;
  }
  
  @Override
  public boolean showDurabilityBar(ItemStack stack)
  {
    IEnergyStorage storage = getEnergyStorage(stack).orElse(EMPTY);
    
    return storage.getEnergyStored() < storage.getMaxEnergyStored();
  }
  
  @Override
  public double getDurabilityForDisplay(ItemStack stack)
  {
    IEnergyStorage storage = getEnergyStorage(stack).orElse(EMPTY);
    
    return Utilities.getEnergyPercentage(storage);
  }
  
  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
  {
    return new ItemEnergyCapabilityProvider(stack, capacity, insert, extract);
  }
  
  public LazyOptional<IEnergyStorage> getEnergyStorage(ItemStack stack)
  {
    return stack.getCapability(CapabilityEnergy.ENERGY);
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    getEnergyStorage(stack)
      .ifPresent(storage ->
        {
          tooltip.add(TextComponentBuilder.create(Utilities.getTooltip(storage, !Screen.hasShiftDown())).aqua().build());
        }
      );
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
