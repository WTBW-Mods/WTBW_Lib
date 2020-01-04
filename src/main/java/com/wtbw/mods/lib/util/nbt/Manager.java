package com.wtbw.mods.lib.util.nbt;

import com.wtbw.mods.lib.tile.util.RedstoneControl;
import com.wtbw.mods.lib.util.ICast;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.IIntArray;
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
  
  public interface IIntReferenceHolder extends ICast<IntReferenceHolder>
  {
    IntReferenceHolder getReferenceHolder();
  
    @Override
    default IntReferenceHolder cast()
    {
      return getReferenceHolder();
    }
  }
  
  public interface IIntArrayHolder extends ICast<IIntArray>
  {
    IIntArray getIntArray();
    
    @Override
    default IIntArray cast()
    {
      return getIntArray();
    }
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
  
  public abstract static class BlockPos extends Impl<net.minecraft.util.math.BlockPos> implements IIntArrayHolder
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
  
    @Override
    public IIntArray getIntArray()
    {
      return new IIntArray()
      {
        @Override
        public int get(int index)
        {
          net.minecraft.util.math.BlockPos pos = BlockPos.this.get();
          switch (index)
          {
            default:
            case 0:
              return pos.getX();
              
            case 1:
              return pos.getY();
              
            case 2:
              return pos.getZ();
          }
        }
  
        @Override
        public void set(int index, int value)
        {
          net.minecraft.util.math.BlockPos pos = BlockPos.this.get();
          int x = pos.getX();
          int y = pos.getY();
          int z = pos.getZ();
  
          switch (index)
          {
            default:
            case 0:
              x = value;
              break;
            case 1:
              y = value;
              break;
            case 2:
              z = value;
              break;
          }
          
          BlockPos.this.set(new net.minecraft.util.math.BlockPos(x, y, z));
        }
  
        @Override
        public int size()
        {
          return 3;
        }
      };
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
    public final INBTSerializable<CompoundNBT> serializable;
  
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
