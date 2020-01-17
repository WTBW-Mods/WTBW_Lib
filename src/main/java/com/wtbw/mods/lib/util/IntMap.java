package com.wtbw.mods.lib.util;

import java.util.HashMap;
import java.util.Random;

/*
  @author: Naxanria
*/
public class IntMap<V> extends HashMap<Integer, V>
{
  private static Random random = new Random();
  
  public int getNextId()
  {
    int next;
    while (containsKey(next = random.nextInt()));
    
    return next;
  }
}
