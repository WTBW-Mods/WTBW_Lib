package com.wtbw.mods.lib.util;

import net.minecraftforge.common.util.NonNullSupplier;

import java.util.HashMap;

/*
  @author: Naxanria
*/
public class BiMap<K0, K1, V> extends HashMap<K0, HashMap<K1, V>>
{
  public BiMap<K0, K1, V> put(K0 key0, K1 key1, V value)
  {
    HashMap<K1, V> map;
    if (containsKey(key0))
    {
      map = get(key0);
    }
    else
    {
      map = new HashMap<>();
      put(key0, map);
    }
    
    map.put(key1, value);
    
    return this;
  }
  
  public V get(K0 key0, K1 key1)
  {
    HashMap<K1, V> map = get(key0);
    return map != null ? map.get(key1) : null;
  }
  
  public V getOrDefault(K0 key0, K1 key1, V defaultValue)
  {
    HashMap<K1, V> map = get(key0);
    return map != null ? map.get(key1) : defaultValue;
  }
  
  public V getOrDefault(K0 key0, K1 key1, NonNullSupplier<V> defaultValueSupplier)
  {
    HashMap<K1, V> map = get(key0);
    return map != null ? map.get(key1) : defaultValueSupplier.get();
  }
  
  public void forEach(TriConsumer<? super K0, ? super K1, ? super V> consumer)
  {
    forEach((k0, map) -> map.forEach((k1, v) -> consumer.accept(k0, k1, v)));
  }
  
  @Override
  public void clear()
  {
    values().forEach(HashMap::clear);
    super.clear();
  }
}
