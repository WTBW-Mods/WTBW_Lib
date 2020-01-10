package com.wtbw.mods.lib.util.rand;

import com.wtbw.mods.lib.util.BiValue;

import java.util.*;

/*
  @author: Naxanria
*/
public class ChanceMap<V>
{
  protected boolean attemptsAsCount = false;
  protected Random random;

  Map<V, List<BiValue<Float, Integer>>> chanceMap = new LinkedHashMap<>();
  protected int entries;
  
  public ChanceMap()
  {
    random = new Random();
  }
  
  public ChanceMap(long seed)
  {
    random = new Random(seed);
  }
  
  public ChanceMap(Random random)
  {
    this.random = random;
  }
  
  public ChanceMap<V> add(float chance, int attempts, V value)
  {
    List<BiValue<Float, Integer>> list;
    if (chanceMap.containsKey(value))
    {
      list = chanceMap.get(value);
    }
    else
    {
      list = new ArrayList<>();
    }
    list.add(new BiValue<>(chance, attempts));
    chanceMap.putIfAbsent(value, list);
    entries++;
    
    return this;
  }
  
  public Map<V, List<BiValue<Float, Integer>>> getChanceMap()
  {
    return chanceMap;
  }
  
  public BiValue<List<BiValue<V, Integer>>, List<Float>> getChances()
  {
    List<BiValue<V, Integer>> values = new ArrayList<>();
    List<Float> chances = new ArrayList<>();
    
    chanceMap.forEach
    (
      (v, biValues) ->
      {
        biValues.forEach
        (
          biValue ->
          {
            values.add(new BiValue<>(v, biValue.b));
            chances.add(biValue.a);
          }
        );
      }
    );
    
    return new BiValue<>(values, chances);
  }
  
  public Map<V, Integer> getMaxRolls()
  {
    Map<V, Integer> map = new HashMap<>();
    
    for (Map.Entry<V, List<BiValue<Float, Integer>>> entry : chanceMap.entrySet())
    {
      int tot = 0;
      for (BiValue<Float, Integer> biValue : entry.getValue())
      {
        tot += biValue.b;
      }
      map.put(entry.getKey(), tot);
    }
    
  
//    for (Entry<V> entry : entries)
//    {
//      int count = 0;
//      if (map.containsKey(entry.value))
//      {
//        count = map.get(entry.value);
//      }
//
//      count += entry.attempts;
//
//      map.put(entry.value, count);
//    }
    
    return map;
  }
  
  public Map<V, Integer> getRolls()
  {
    Map<V, Integer> map = new HashMap<>();
  
    for (Map.Entry<V, List<BiValue<Float, Integer>>> entry : chanceMap.entrySet())
    {
      int tot = 0;
      for (BiValue<Float, Integer> biValue : entry.getValue())
      {
        if (attemptsAsCount)
        {
          if (RandomUtil.chance(random, biValue.a))
          {
            tot += biValue.b;
          }
        }
        else
        {
          for (int i = 0; i < biValue.b; i++)
          {
            if (RandomUtil.chance(random, biValue.a))
            {
              tot++;
            }
          }
        }
      }
      
      map.put(entry.getKey(), tot);
    }
    
//    for (Entry<V> entry : entries)
//    {
//      int count = 0;
//      if (map.containsKey(entry.value))
//      {
//        count = map.get(entry.value);
//      }
//
//      if (attemptsAsCount)
//      {
//        if (RandomUtil.chance(random, entry.chance))
//        {
//          count += entry.attempts;
//        }
//      }
//      else
//      {
//        for (int i = 0; i < entry.attempts; i++)
//        {
//          if (RandomUtil.chance(random, entry.chance))
//          {
//            count++;
//          }
//        }
//      }
//
//      if (count > 0)
//      {
//        map.put(entry.value, count);
//      }
//    }
//
    return map;
  }
  
  public boolean isAttemptsAsCount()
  {
    return attemptsAsCount;
  }
  
  public ChanceMap<V> setAttemptsAsCount(boolean attemptsAsCount)
  {
    this.attemptsAsCount = attemptsAsCount;
    return this;
  }
  
  public int getEntries()
  {
    return entries;
  }
  
  public Random getRandom()
  {
    return random;
  }
}
