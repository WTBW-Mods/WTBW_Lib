package com.wtbw.mods.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import javax.annotation.Nullable;

/*
  @author: Naxanria
*/
public class SixWayTileBlock<TE extends TileEntity> extends BaseTileBlock<TE>
{
  public static final DirectionProperty FACING = BlockStateProperties.FACING;
  
  public SixWayTileBlock(Properties properties, TileEntityProvider<TE> tileEntityProvider)
  {
    super(properties, tileEntityProvider);
  }
  
  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    return getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
  }
  
  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  {
    builder.add(FACING);
  }
  
  public BlockState rotate(BlockState state, Rotation rot)
  {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }
  
  public BlockState mirror(BlockState state, Mirror mirrorIn)
  {
    return state.rotate(mirrorIn.toRotation(state.get(FACING)));
  }
}
