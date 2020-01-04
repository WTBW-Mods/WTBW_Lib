package com.wtbw.mods.lib.util;

/*
  @author: Naxanria
*/
public interface ICast<T>
{
  default T cast()
  {
    return (T) this;
  }
}
