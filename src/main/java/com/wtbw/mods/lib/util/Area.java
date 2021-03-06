package com.wtbw.mods.lib.util;

import com.wtbw.mods.lib.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

/*
  @author: Naxanria
*/
public class Area implements INBTSerializable<CompoundNBT>
{
  public BlockPos start;
  public BlockPos end;
  
  public Area(int startX, int startY, int startZ, int endX, int endY, int endZ)
  {
    this(new BlockPos(startX, startY, startZ), new BlockPos(endX, endY, endZ));
  }
  
  public Area(BlockPos start, BlockPos end)
  {
    BiValue<BlockPos, BlockPos> sorted = Utilities.sort(start, end);
    this.start = sorted.a;
    this.end = sorted.b;
  }
  
  public boolean isInside(BlockPos pos)
  {
    return isInside(pos.getX(), pos.getY(), pos.getZ());
  }
  
  public boolean isInside(int x, int y, int z)
  {
    return x >= start.getX() && x <= end.getX()
      && y >= start.getY() && y <= end.getY()
      && z >= start.getZ() && z <= end.getZ();
  }
  
  public int getSide(Direction direction)
  {
    switch (direction)
    {
      case DOWN:
        return start.getY();
      case UP:
        return end.getY();
      default:
      case NORTH:
        return start.getZ();
      case SOUTH:
        return end.getZ();
      case WEST:
        return start.getX();
      case EAST:
        return end.getX();
    }
  }
  
  public int width()
  {
    return getSide(Direction.EAST) - getSide(Direction.WEST) + 1;
  }
  
  public int height()
  {
    return getSide(Direction.UP) - getSide(Direction.DOWN) + 1;
  }
  
  public int depth()
  {
    return getSide(Direction.SOUTH) - getSide(Direction.NORTH) + 1;
  }
  
  @Override
  public CompoundNBT serializeNBT()
  {
    CompoundNBT nbt = new CompoundNBT();
    NBTHelper.putBlockPos(nbt, "start", start);
    NBTHelper.putBlockPos(nbt, "end", end);
    return nbt;
  }
  
  @Override
  public void deserializeNBT(CompoundNBT nbt)
  {
    start = NBTHelper.getBlockPos(nbt, "start");
    end = NBTHelper.getBlockPos(nbt, "end");
  }
  
  @Override
  public String toString()
  {
    return "Area [" + start.toString() + "] [" + end.toString() + "] (" + width() + ", " + height() + ", " + depth() + ")";
  }
}

