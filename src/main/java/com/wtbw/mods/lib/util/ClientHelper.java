package com.wtbw.mods.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/

public class ClientHelper
{
  public static World getWorld()
  {
    return Minecraft.getInstance().world;
  }
  
  public static PlayerEntity getPlayer()
  {
    return Minecraft.getInstance().player;
  }
}
