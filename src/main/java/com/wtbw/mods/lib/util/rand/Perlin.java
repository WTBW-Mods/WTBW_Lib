package com.wtbw.mods.lib.util.rand;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

/*
  @author: Naxanria
*/
public class Perlin
{
  private Random random;
  
  private float[] seeded;
  private float[] octaves;
  private int size = 256;

  private int octaveCount = 256;
  
  public Perlin()
  {
    init(System.currentTimeMillis());
  }
  
  public Perlin(long seed)
  {
    init(seed);
  }
  
  public void init(long seed)
  {
    random = new Random(seed);
    seeded = new float[size];
    octaves = new float[size];
  
    for (int i = 0; i < size; i++)
    {
      seeded[i] = random.nextFloat();
    }
  
    for (int i = 0; i < size; i++)
    {
      float totalScale = 0;
      float value = 0;
      float scale = 1;
      for (int o = 0; (1 << (o + 1)) <= octaveCount; o++)
      {
        value += getSampled(i, 1 << (o + 1)) * scale;
        
        totalScale += scale;
        scale /= 2f;
      }
      
      // normalize the value
      value /= totalScale;
      
      octaves[i] = value;
    }
  }
  
  private float getSampled(int index, int samples)
  {
    float total = 0;
    int step = size / samples;
    for (int i = 0; i < samples; i++)
    {
      int sampleIndex = (step * i + index) % size;
      total += seeded[sampleIndex];
    }
    
    return total / samples;
  }
  
  private float getValue(int i)
  {
    return octaves[i % size];
  }
  
  public float get(float x)
  {
    int left = (int) x;
    int right = left < x ? left + 1 : left;
    float off = x - left;
  
    return MathHelper.lerp(off, getValue(left), getValue(right));
  }
}
