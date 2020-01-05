package com.wtbw.mods.lib.util.rand;

import java.util.*;

/*
  @author: Naxanria
*/
public class ChanceMap<V>
{
  public boolean isAttemptsAsCount()
  {
    return attemptsAsCount;
  }
  
  public ChanceMap<V> setAttemptsAsCount(boolean attemptsAsCount)
  {
    this.attemptsAsCount = attemptsAsCount;
    return this;
  }
  
  protected List<Entry<V>> entries = new ArrayList<>();
  protected boolean attemptsAsCount = false;
  protected Random random;
  
  public ChanceMap()
  {
    random = new Random();
  }
  
  public ChanceMap(long seed)
  {
    random = new Random(seed);
  }
  
  public ChanceMap<V> add(float chance, int attempts, V value)
  {
    entries.add(new Entry<>(value, chance, attempts));
    return this;
  }
  
  public List<Entry<V>> getEntries()
  {
    return entries;
  }
  
  public Map<V, Integer> getMaxResultMap()
  {
    Map<V, Integer> map = new HashMap<>();
  
    for (Entry<V> entry : entries)
    {
      int count = 0;
      if (map.containsKey(entry.value))
      {
        count = map.get(entry.value);
      }
      
      count += entry.attempts;
      
      map.put(entry.value, count);
    }
    
    return map;
  }
  
  public Map<V, Integer> getChancedMap()
  {
    Map<V, Integer> map = new HashMap<>();
    
    for (Entry<V> entry : entries)
    {
      int count = 0;
      if (map.containsKey(entry.value))
      {
        count = map.get(entry.value);
      }
      
      if (attemptsAsCount)
      {
        if (RandomUtil.chance(random, entry.chance))
        {
          count += entry.attempts;
        }
      }
      else
      {
        for (int i = 0; i < entry.attempts; i++)
        {
          if (RandomUtil.chance(random, entry.chance))
          {
            count++;
          }
        }
      }
      
      if (count > 0)
      {
        map.put(entry.value, count);
      }
    }
    
    return map;
  }
  
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
}
