package com.wtbw.lib.util.nbt;

import com.wtbw.lib.tile.util.RedstoneControl;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.INBTSerializable;

/*
  @author: Naxanria
*/
public abstract class Manager
{
  public abstract void read(String name, CompoundNBT nbt);
  public abstract void write(String name, CompoundNBT nbt);
  
  private abstract static class Impl<T> extends Manager
  {
    public abstract T get();
    public abstract void set(T value);
  }
  
  public abstract static class Int extends Impl<Integer>
  {
    @Override
    public void read(String name, CompoundNBT nbt)
    {
      set(nbt.getInt(name));
    }
  
    @Override
    public void write(String name, CompoundNBT nbt)
    {
      nbt.putInt(name, get());
    }
  }
  
  public abstract static class Bool extends Impl<Boolean>
  {
    @Override
    public void read(String name, CompoundNBT nbt)
    {
      set(nbt.getBoolean(name));
    }
  
    @Override
    public void write(String name, CompoundNBT nbt)
    {
      nbt.putBoolean(name, get());
    }
  }
  
  public abstract static class BlockPos extends Impl<net.minecraft.util.math.BlockPos>
  {
    @Override
    public void read(String name, CompoundNBT nbt)
    {
      set(NBTUtil.readBlockPos(nbt.getCompound(name)));
    }
  
    @Override
    public void write(String name, CompoundNBT nbt)
    {
      nbt.put(name, NBTUtil.writeBlockPos(get()));
    }
  }
  
  public abstract static class BlockState extends Impl<net.minecraft.block.BlockState>
  {
    @Override
    public void read(String name, CompoundNBT nbt)
    {
      set(NBTUtil.readBlockState(nbt.getCompound(name)));
    }
  
    @Override
    public void write(String name, CompoundNBT nbt)
    {
      nbt.put(name, NBTUtil.writeBlockState(get()));
    }
  }
  
  public static class Redstone extends Manager
  {
    private final RedstoneControl control;
  
    public Redstone(RedstoneControl control)
    {
      this.control = control;
    }
  
    @Override
    public void read(String name, CompoundNBT nbt)
    {
      control.deserialize(nbt.getCompound(name));
    }
  
    @Override
    public void write(String name, CompoundNBT nbt)
    {
      nbt.put(name, control.serialize());
    }
  }
  
//  public static class ItemHandler extends Manager
//  {
//    private final ItemStackHandler handler;
//
//    protected ItemHandler(ItemStackHandler handler)
//    {
//      this.handler = handler;
//    }
//
//    @Override
//    public void read(String name, CompoundNBT nbt)
//    {
//      handler.deserializeNBT(nbt.getCompound(name));
//    }
//
//    @Override
//    public void write(String name, CompoundNBT nbt)
//    {
//      nbt.put(name, handler.serializeNBT());
//    }
//  }
//
//  public static class Energy extends Manager
//  {
//    private final BaseEnergyStorage storage;
//
//    public Energy(BaseEnergyStorage storage)
//    {
//      this.storage = storage;
//    }
//
//    @Override
//    public void read(String name, CompoundNBT nbt)
//    {
//      storage.deserializeNBT(nbt.getCompound(name));
//    }
//
//    @Override
//    public void write(String name, CompoundNBT nbt)
//    {
//      nbt.put(name, storage.serializeNBT());
//    }
//  }
  
  public static class Serializable extends Manager
  {
    private final INBTSerializable<CompoundNBT> serializable;
  
    public Serializable(INBTSerializable<CompoundNBT> serializable)
    {
      this.serializable = serializable;
    }
  
    @Override
    public void read(String name, CompoundNBT nbt)
    {
      serializable.deserializeNBT(nbt.getCompound(name));
    }
  
    @Override
    public void write(String name, CompoundNBT nbt)
    {
      nbt.put(name, serializable.serializeNBT());
    }
  }
}
