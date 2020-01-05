package com.wtbw.mods.lib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  @author: Naxanria
*/
public class ChanceMap<V>
{
  public static class Entry<V>
  {
    public final V value;
    public final float chance;
    public final int attempts;
  
    public Entry(V value, float chance, int attempts)
    {
      this.value = value;
      this.chance = chance;
      this.attempts = attempts;
    }
  }
  
  private List<Entry<V>> entries = new ArrayList<>();
  
  public ChanceMap()
  {
  
  }
  
  public ChanceMap<V> add(float chance, int attempts, V value)
  {
    entries.add(new Entry<>(value, chance, attempts));
    return this;
  }
  
  public List<BiValue<V, Integer>> get()
  {
    Map<V, Integer> map = new HashMap<>();
    
    for (Entry<V> entry : entries)
    {
    
    }
    
    List<BiValue<V, Integer>> output = new ArrayList<>();
    
    return output;
  }
}
