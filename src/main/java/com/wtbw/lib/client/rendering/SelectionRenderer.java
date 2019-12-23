package com.wtbw.lib.client.rendering;

import com.wtbw.lib.util.Utilities;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

/*
  @author: Naxanria
*/
public class SelectionRenderer extends ItemStackWorldRenderer
{
  @Override
  public boolean render(ItemStack stack, PlayerEntity player, RayTraceResult hit, WorldRenderer context, ActiveRenderInfo info)
  {
    if (hit.getType() != RayTraceResult.Type.BLOCK)
    {
      return false;
    }
    
    World world = player.world;
    
    BlockPos pos = ((BlockRayTraceResult)hit).getPos();
    List<BlockPos> blocks = Utilities.getBlocks(pos, ((BlockRayTraceResult) hit).getFace());
    
    BlockState blockstate = world.getBlockState(pos);
    if (blockstate.getBlockHardness(world, pos) == 0 || !stack.getItem().canHarvestBlock(blockstate))
    {
      return false;
    }
    
    for (BlockPos blockpos : blocks)
    {
      blockstate = world.getBlockState(blockpos);
      if (!stack.getItem().canHarvestBlock(blockstate))
      {
        continue;
      }
  
      renderOutline(context, info, world, blockstate, blockpos);
    }
    
    return true;
  }
}
