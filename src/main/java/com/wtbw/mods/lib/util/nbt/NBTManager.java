package com.wtbw.mods.lib.util.nbt;

import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    managerMap.put(name, manager);
    return this;
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
      if (entry.getValue() instanceof Manager.IIntReferenceHolder)
      {
        referenceHolders.add((Manager.IIntReferenceHolder) entry.getValue());
      }
    }
    
    return referenceHolders;
  }
}
