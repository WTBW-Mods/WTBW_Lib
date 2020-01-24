package com.wtbw.mods.lib.block;

import com.wtbw.mods.lib.tile.util.IComparatorProvider;
import com.wtbw.mods.lib.tile.util.IContentHolder;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.Cache;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class BaseTileBlock<TE extends TileEntity> extends Block
{
  public final TileEntityProvider<TE> tileEntityProvider;
  
  protected boolean hasGui = true;
  protected boolean hasComparatorOverride = false;
  protected boolean hideExtraTooltips = false;
  
  protected Cache<BaseEnergyStorage> storage = Cache.create(() -> new BaseEnergyStorage(10000));

  public BaseTileBlock(Properties properties, TileEntityProvider<TE> tileEntityProvider)
  {
    super(properties);

    this.tileEntityProvider = tileEntityProvider;
  }

  public BaseTileBlock setComparatorOverride(boolean override)
  {
    hasComparatorOverride = override;
    return this;
  }
  
  public BaseTileBlock comparator()
  {
    return setComparatorOverride(true);
  }
  
  public BaseTileBlock setHasGui(boolean gui)
  {
    hasGui = gui;
    return this;
  }
  
  public BaseTileBlock setGui()
  {
    hasGui = true;
    return this;
  }
  
  @Override
  public boolean hasTileEntity(BlockState state)
  {
    return true;
  }

  @Nullable
  @Override
  public TE createTileEntity(BlockState state, IBlockReader world)
  {
    return tileEntityProvider.get(world, state);
  }

  protected TE getTileEntity(IBlockReader world, BlockPos pos)
  {
    return (TE) world.getTileEntity(pos);
  }
  
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit)
  {
    if (hasGui)
    {
      if (!playerEntity.isCrouching())
      {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof INamedContainerProvider)
        {
          if (!world.isRemote)
          {
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            onGuiOpen(state, world, pos, ((ServerPlayerEntity) playerEntity), hand, hit);
          }

          return ActionResultType.SUCCESS;
        }
      }
    }

    return super.onBlockActivated(state, world, pos, playerEntity, hand, hit);
  }
  
  protected void onGuiOpen(BlockState state, World world, BlockPos pos, ServerPlayerEntity player, Hand hand, BlockRayTraceResult hit)
  { }
  
  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
  {
    if (state.getBlock() != newState.getBlock())
    {
      TE tileEntity = getTileEntity(worldIn, pos);
      if (tileEntity != null && tileEntity instanceof IContentHolder)
      {
        ((IContentHolder) tileEntity).dropContents();
      }
    }
    
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }
  
  public interface TileEntityProvider<TE extends TileEntity>
  {
    default TE get()
    {
      return get(null, null);
    }

    TE get(IBlockReader world, BlockState state);
  }
  
  @Override
  public boolean hasComparatorInputOverride(BlockState state)
  {
    return hasComparatorOverride;
  }
  
  @Override
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
  {
    if (hasComparatorOverride)
    {
      TE tileEntity = getTileEntity(worldIn, pos);
      if (tileEntity instanceof IComparatorProvider)
      {
        return ((IComparatorProvider) tileEntity).getComparatorStrength();
      }
    }
    
    return super.getComparatorInputOverride(blockState, worldIn, pos);
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    if (!hideExtraTooltips)
    {
      CompoundNBT data = stack.getChildTag("BlockEntityTag");
  
      if (data != null)
      {
        if (data.contains("storage", Constants.NBT.TAG_COMPOUND))
        {
          BaseEnergyStorage storage = this.storage.get();
      
          storage.deserializeNBT(data.getCompound("storage"));
          if (storage.getEnergyStored() > 0)
          {
            tooltip.add(TextComponentBuilder.create(Utilities.getTooltip(storage, !Screen.hasShiftDown())).aqua().build());
          }
        }
      }
  
      super.addInformation(stack, worldIn, tooltip, flagIn);
    }
  }
}
