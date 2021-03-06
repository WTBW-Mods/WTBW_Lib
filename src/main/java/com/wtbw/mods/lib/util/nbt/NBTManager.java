package com.wtbw.mods.lib.util.nbt;

import com.wtbw.mods.lib.WTBWLib;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class NBTManager
{
  private Map<String, Manager> managerMap = new HashMap<>();
  
  public void read(CompoundNBT nbt)
  {
    for (Map.Entry<String, Manager> entry : managerMap.entrySet())
    {
      if (nbt.contains(entry.getKey()))
      {
        entry.getValue().read(entry.getKey(), nbt);
      }
    }
  }
  
  public CompoundNBT write(CompoundNBT nbt)
  {
    for (Map.Entry<String, Manager> entry : managerMap.entrySet())
    {
      entry.getValue().write(entry.getKey(), nbt);
    }
    
    return nbt;
  }
  
  public NBTManager register(String name, Manager manager)
  {
    return register(name, manager, true);
  }
  
  public NBTManager register(String name, Manager manager, boolean trackGui)
  {
    if (!trackGui)
    {
      manager.noGuiTracking();
    }
    
    managerMap.put(name, manager);
    return this;
  }
  
  public NBTManager registerInt(String name, Supplier<Integer> get, Consumer<Integer> set)
  {
    return register(name, new Manager.Int()
    {
      @Override
      public Integer get()
      {
        return get.get();
      }
      
      @Override
      public void set(Integer value)
      {
        set.accept(value);
      }
    });
  }
  
  public NBTManager registerLong(String name, Supplier<Long> get, Consumer<Long> set)
  {
    return register(name, new Manager.Long()
    {
      @Override
      public java.lang.Long get()
      {
        return get.get();
      }
      
      @Override
      public void set(java.lang.Long value)
      {
        set.accept(value);
      }
    });
  }
  
  public NBTManager registerBoolean(String name, Supplier<Boolean> get, Consumer<Boolean> set)
  {
    return register(name, new Manager.Bool()
    {
      @Override
      public Boolean get()
      {
        return get.get();
      }
    
      @Override
      public void set(Boolean value)
      {
        set.accept(value);
      }
    });
  }
  
  public NBTManager registerBlockPos(String name, Supplier<BlockPos> get, Consumer<BlockPos> set)
  {
    return register(name, new Manager.BlockPos()
    {
      @Override
      public net.minecraft.util.math.BlockPos get()
      {
        return get.get();
      }
      
      @Override
      public void set(net.minecraft.util.math.BlockPos value)
      {
        set.accept(value);
      }
    });
  }
  
  public NBTManager register(String name, INBTSerializable<CompoundNBT> serializable)
  {
    return register(name, serializable, true);
  }
  
  public NBTManager register(String name, INBTSerializable<CompoundNBT> serializable, boolean trackGui)
  {
    Manager.Serializable manager = new Manager.Serializable(serializable);
    if (!trackGui)
    {
      manager.noGuiTracking();
    }
    
    return register(name, manager);
  }
  
  public NBTManager register(String name, FluidTank tank)
  {
    return register(name, tank, true);
  }
  
  public NBTManager register(String name, FluidTank tank, boolean trackGui)
  {
    Manager.FluidTankManager manager = new Manager.FluidTankManager(tank);
    if (!trackGui)
    {
      manager.noGuiTracking();
    }
    return register(name, manager);
  }
  
  public boolean contains(String name)
  {
    return managerMap.containsKey(name);
  }
  
  public Manager get(String name)
  {
    return managerMap.get(name);
  }
  
  public List<Manager.IIntReferenceHolder> referenceHolders()
  {
    List<Manager.IIntReferenceHolder> referenceHolders = new ArrayList<>();
    for (Map.Entry<String, Manager> entry : managerMap.entrySet())
    {
      if (entry.getValue() instanceof Manager.IIntReferenceHolder && entry.getValue().isGuiTracking())
      {
        referenceHolders.add((Manager.IIntReferenceHolder) entry.getValue());
      }
    }
    
    return referenceHolders;
  }
  
  public List<Manager.IIntArrayHolder> arrayHolders()
  {
    List<Manager.IIntArrayHolder> holders = new ArrayList<>();
    
    for (Map.Entry<String, Manager> entry : managerMap.entrySet())
    {
      Manager manager = entry.getValue();
      if (manager.isGuiTracking())
      {
        if (manager instanceof Manager.IIntArrayHolder)
        {
          holders.add((Manager.IIntArrayHolder) manager);
        }
        else if (manager instanceof Manager.Serializable && ((Manager.Serializable) manager).serializable instanceof Manager.IIntArrayHolder)
        {
          holders.add((Manager.IIntArrayHolder) ((Manager.Serializable) manager).serializable);
        }
      }
    }
    
    return holders;
  }
  
  public void listAll()
  {
    WTBWLib.LOGGER.info("Listing registered managers, total: {}", managerMap.size());
    managerMap.forEach((s, manager) -> WTBWLib.LOGGER.info("\t{}: {}", s, manager.getClass().toString()));
  }
}
