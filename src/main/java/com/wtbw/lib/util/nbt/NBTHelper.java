package com.wtbw.lib.util.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class NBTHelper
{
  public static int getInt(CompoundNBT compoundNBT, String name)
  {
    return getInt(compoundNBT, name, 0);
  }
  
  public static int getInt(CompoundNBT compoundNBT, String name, int defaultValue)
  {
    if (compoundNBT.contains(name))
    {
      return compoundNBT.getInt(name);
    }
    
    return defaultValue;
  }
  
  public static CompoundNBT getCompound(CompoundNBT compoundNBT, String name)
  {
    return getCompound(compoundNBT, name, new CompoundNBT());
  }
  
  public static CompoundNBT getCompound(CompoundNBT compoundNBT, String name, CompoundNBT defaultValue)
  {
    if (compoundNBT.contains(name))
    {
      return compoundNBT.getCompound(name);
    }
    
    return defaultValue;
  }
  
  public static float getFloat(CompoundNBT compoundNBT, String name)
  {
    return getFloat(compoundNBT, name, 0);
  }
  
  public static float getFloat(CompoundNBT compoundNBT, String name, float defaultValue)
  {
    if (compoundNBT.contains(name))
    {
      return compoundNBT.getFloat(name);
    }
    
    return defaultValue;
  }
  
  public static boolean getBoolean(CompoundNBT compoundNBT, String name, boolean defaultValue)
  {
    if (compoundNBT.contains(name))
    {
      return compoundNBT.getBoolean(name);
    }
    
    return defaultValue;
  }
  
  public static CompoundNBT putBlockPos(CompoundNBT compoundNBT, String name, BlockPos pos)
  {
    CompoundNBT compound = new CompoundNBT();
    compound.putInt("X", pos.getX());
    compound.putInt("Y", pos.getY());
    compound.putInt("Z", pos.getZ());
    compoundNBT.put(name, compound);
    return compoundNBT;
  }
  
  public static BlockPos getBlockPos(CompoundNBT compoundNBT, String name)
  {
    return getBlockPos(compoundNBT, name, () -> new BlockPos(0, 0, 0));
  }
  
  public static BlockPos getBlockPos(CompoundNBT compoundNBT, String name, Supplier<BlockPos> defaultProvider)
  {
    if (compoundNBT.contains(name))
    {
      CompoundNBT compound = compoundNBT.getCompound(name);
      int x = compound.getInt("X");
      int y = compound.getInt("Y");
      int z = compound.getInt("Z");
      
      return new BlockPos(x, y, z);
    }
    
    return defaultProvider.get();
  }
}
