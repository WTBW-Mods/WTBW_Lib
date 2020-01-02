package com.wtbw.lib.tile.util;

import com.wtbw.lib.util.Utilities;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

/*
  @author: Naxanria
*/
public interface IWTBWNamedContainerProvider extends INamedContainerProvider
{
  @Nonnull
  @Override
  default ITextComponent getDisplayName()
  {
    if (this instanceof TileEntity)
    {
      return Utilities.getTitle((TileEntity) this);
    }
    
    return new StringTextComponent("UNKNOWN");
  }
}
