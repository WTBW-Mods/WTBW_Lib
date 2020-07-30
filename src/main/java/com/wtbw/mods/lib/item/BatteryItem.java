package com.wtbw.mods.lib.item;

import com.wtbw.mods.lib.WTBWLib;
import com.wtbw.mods.lib.item.util.BaseItemEnergyStorage;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
  @author: Naxanria
*/
public class BatteryItem extends BasePoweredItem
{
  public static final ResourceLocation BATTERY_TAG = new ResourceLocation("forge", "battery");
  
  public BatteryItem(Properties properties, int capacity, int inOut)
  {
    this(properties, capacity, inOut, inOut);
  }
  
  public BatteryItem(Properties properties, int capacity, int extract, int insert)
  {
    super(properties, capacity, extract, insert);
  }
  
  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
  {
    getEnergyStorage(stack).ifPresent(storage ->
    {
      if (storage instanceof BaseItemEnergyStorage)
      {
        BaseItemEnergyStorage itemStorage = (BaseItemEnergyStorage) storage;
        if (itemStorage.getEnergyStored() > 0)
        {
          if (entity instanceof PlayerEntity)
          {
            PlayerEntity player = (PlayerEntity) entity;
            
            AtomicInteger maxSend = new AtomicInteger(Math.min(itemStorage.getMaxExtract(), itemStorage.getEnergyStored()));
            for (int i = 0; i < player.inventory.getSizeInventory() && maxSend.get() > 0; i++)
            {
              ItemStack stackInSlot = player.inventory.getStackInSlot(i);
              // skip the battery
              if (hasBatteryItemTag(stackInSlot))
              {
                continue;
              }
              if (!stackInSlot.isEmpty())
              {
                stackInSlot.getItem();
                stackInSlot.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage ->
                {
                  if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored())
                  {
                    int send = energyStorage.receiveEnergy(maxSend.get(), false);
                    itemStorage.extractInternal(send, false);
                    maxSend.addAndGet(-send);
                  }
                });
              }
            }
          }
        }
      }
    });
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    getEnergyStorage(stack).ifPresent(es ->
    {
      if (es instanceof BaseItemEnergyStorage)
      {
        BaseItemEnergyStorage storage = (BaseItemEnergyStorage) es;
        int in = storage.getMaxInsert();
        int out = storage.getMaxExtract();
        
        String msg;
        boolean shift = Screen.hasShiftDown();
        if (in == out)
        {
          msg = !shift ? Utilities.abbreviate(in) : String.valueOf(in);
        }
        else
        {
          msg = !shift ? Utilities.abbreviate(in) + "/" + Utilities.abbreviate(out) : in + "/" + out;
        }
        
        tooltip.add(TextComponentBuilder.createTranslated(WTBWLib.MODID + ".tooltip.in_out").insertSpaces().next(msg).aqua().build());
      }
    });
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
  public static boolean hasBatteryItemTag(ItemStack stack)
  {
    return hasBatteryItemTag(stack.getItem());
  }
  
  public static boolean hasBatteryItemTag(Item item)
  {
    return item.getTags().contains(BATTERY_TAG);
  }
}
