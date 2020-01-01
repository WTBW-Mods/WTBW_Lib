package com.wtbw.lib.util.nbt;

import com.wtbw.lib.tile.util.RedstoneControl;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.IntReferenceHolder;
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
  
  public interface IIntReferenceHolder
  {
    IntReferenceHolder getReferenceHolder();
  }
  
  public abstract static class Int extends Impl<Integer> implements IIntReferenceHolder
  {
    private IntReferenceHolder referenceHolder;
    
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
  
    @Override
    public IntReferenceHolder getReferenceHolder()
    {
      if (referenceHolder == null)
      {
        referenceHolder = new IntReferenceHolder()
        {
          @Override
          public int get()
          {
            return Int.this.get();
          }
  
          @Override
          public void set(int value)
          {
            Int.this.set(value);
          }
        };
      }
      
      return referenceHolder;
    }
  }
  
  public abstract static class Bool extends Impl<Boolean> implements IIntReferenceHolder
  {
    private IntReferenceHolder referenceHolder;
    
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
  
    @Override
    public IntReferenceHolder getReferenceHolder()
    {
      if (referenceHolder == null)
      {
        referenceHolder = new IntReferenceHolder()
        {
          @Override
          public int get()
          {
            return Bool.this.get() ? 1 : 0;
          }
  
          @Override
          public void set(int value)
          {
            Bool.this.set(value == 1);
          }
        };
      }
      
      return referenceHolder;
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
