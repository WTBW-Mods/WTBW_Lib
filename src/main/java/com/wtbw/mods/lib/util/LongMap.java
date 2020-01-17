package com.wtbw.mods.lib.util;

import java.util.HashMap;
import java.util.Random;

/*
  @author: Naxanria
*/
public class LongMap<V> extends HashMap<Long, V>
{
  private static Random random = new Random();
  
  public long getNextId()
  {
    long next;
    while (containsKey(next = random.nextLong()));
    
    return next;
  }
}
