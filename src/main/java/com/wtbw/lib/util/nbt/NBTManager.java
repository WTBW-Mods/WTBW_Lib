package com.wtbw.lib.util.nbt;

import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;

/*
  @author: Naxanria
*/
public class NBTManager
{
  public Map<String, Manager> managerMap = new HashMap<>();
  
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
    managerMap.put(name, manager);
    return this;
  }
}
