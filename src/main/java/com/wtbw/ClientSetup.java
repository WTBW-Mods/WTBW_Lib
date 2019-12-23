package com.wtbw;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/

public class ClientSetup
{
  public static World getWorld()
  {
    return Minecraft.getInstance().world;
  }
}
