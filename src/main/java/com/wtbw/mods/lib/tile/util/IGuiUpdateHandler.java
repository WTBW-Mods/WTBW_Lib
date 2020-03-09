package com.wtbw.mods.lib.tile.util;

import net.minecraft.nbt.CompoundNBT;

/*
  @author: Naxanria
*/
public interface IGuiUpdateHandler
{
  CompoundNBT getGuiUpdateTag();
  void handleGuiUpdateTag(CompoundNBT nbt);
}
