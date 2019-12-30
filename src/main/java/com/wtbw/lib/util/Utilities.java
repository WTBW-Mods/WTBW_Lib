package com.wtbw.lib.util;

import com.wtbw.lib.WTBWLib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
  @author: Naxanria
*/
@SuppressWarnings("WeakerAccess")
public class Utilities
{
  private static int index;
  
  public static int getBurnTime(ItemStack itemStack)
  {
    // copied form AbstractFurnaceTileEntity
    if (itemStack.isEmpty())
    {
      return 0;
    }
    else
    {
      Item item = itemStack.getItem();
      int ret = itemStack.getBurnTime();
      return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(itemStack, ret == -1 ? AbstractFurnaceTileEntity.getBurnTimes().getOrDefault(item, 0) : ret);
    }
  }

  public static List<IRecipe<?>> getRecipes(RecipeManager manager, IRecipeType<?> type)
  {
    Collection<IRecipe<?>> recipes = manager.getRecipes();
    return recipes.stream().filter(iRecipe -> iRecipe.getType() == type).collect(Collectors.toList());
  }

  public static Direction getFacingFromVector(Vec3d vec)
  {
    return Direction.getFacingFromVector(vec.x, vec.y, vec.z);
  }

  public static List<ItemStack> getHotbar(PlayerEntity player)
  {
    return getHotbar(player, -1);
  }

  public static List<ItemStack> getHotbar(PlayerEntity player, int exclude)
  {
    List<ItemStack> hotbar = new ArrayList<>();
    for (int i = 0; i < 9; i++)
    {
      if (i == exclude)
      {
        continue;
      }

      hotbar.add(player.inventory.mainInventory.get(i));
    }
    return hotbar;
  }

  public static <T> List<T> swap(List<T> list, int indexA, int indexB)
  {
    T temp = list.get(indexA);
    list.set(indexA, list.get(indexB));
    list.set(indexB, temp);

    return list;
  }

  public static BlockRayTraceResult getLookingAt(PlayerEntity player, double range)
  {
    return getLookingAt(player, range, RayTraceContext.FluidMode.NONE);
  }
  
  public static BlockRayTraceResult getLookingAt(PlayerEntity player, double range, RayTraceContext.FluidMode fluidMode)
  {
    World world = player.world;

    Vec3d look = player.getLookVec();
    Vec3d startPos = getVec3d(player).add(0, player.getEyeHeight(), 0);
    Vec3d endPos = startPos.add(look.mul(range, range, range));
    RayTraceContext context = new RayTraceContext(startPos, endPos, RayTraceContext.BlockMode.OUTLINE, fluidMode, player);
    return world.rayTraceBlocks(context);
  }
  
  public static BlockRayTraceResult getLookingAt(Vec3d position, Vec3d look, double range, Entity entity)
  {
    Vec3d endPos = position.add(look.mul(range, range, range));
    RayTraceContext context = new RayTraceContext(position, endPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, entity);
    return entity.world.rayTraceBlocks(context);
  }
  
  public static boolean isMiss(BlockRayTraceResult lookingAt)
  {
    return lookingAt.getType() == BlockRayTraceResult.Type.MISS;
  }

  public static Vec3d getVec3d(BlockPos pos)
  {
    return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
  }

  public static Vec3d getVec3d(Entity entity)
  {
    return entity.getPositionVec();
//    return new Vec3d(entity.posX, entity.posY, entity.posZ);
  }

  public static <T> List<T> listOf(T... toList)
  {
    return Arrays.asList(toList);
  }

  public static List<BlockPos> getBlocks(BlockPos pos, Direction facing)
  {
    return getBlocks(pos, facing, 3);
  }

  public static List<BlockPos> getBlocks(BlockPos pos, Direction facing, int radius)
  {
    List<BlockPos> positions = new ArrayList<>();

    if (radius % 2 == 0)
    {
      WTBWLib.LOGGER.error("Trying to get blocks with an even radius! Aborting");
      return positions;
    }

    if (radius < 3)
    {
      positions.add(pos);
      return positions;
    }

    int d = radius / 2;
    int min = -d;
    int max = d;

    switch (facing)
    {
      case DOWN:
      case UP:
        for (int x = min; x <= max; x++)
        {
          for (int z = min; z <= max; z++)
          {
            positions.add(pos.add(x, 0, z));
          }
        }

        break;
      case NORTH:
      case SOUTH:
        for (int x = min; x <= max; x++)
        {
          for (int y = min; y <= max; y++)
          {
            positions.add(pos.add(x, y, 0));
          }
        }
        break;
      case WEST:
      case EAST:
        for (int z = min; z <= max; z++)
        {
          for (int y = min; y <= max; y++)
          {
            positions.add(pos.add(0, y, z));
          }
        }
        break;
    }

    return positions;
  }

  public static BiValue<BlockPos, BlockPos> getRegion(BlockPos center, Direction facing, int radius)
  {
    if (radius % 2 == 0)
    {
      WTBWLib.LOGGER.error("Trying to get blocks with an even radius! Aborting");
      return new BiValue<>(null, null);
    }

    if (radius < 3)
    {
      return new BiValue<>(center, center);
    }

    int d = radius / 2;
    int min = -d;
    int max = d;

    BlockPos start = null;
    BlockPos end = null;

    switch (facing)
    {
      case DOWN:
      case UP:
        start = center.add(min, 0, min);
        end = center.add(max, 0, max);
        break;

      case NORTH:
      case SOUTH:
        start = center.add(min, min, 0);
        end = center.add(max, max, 0);
        break;

      case WEST:
      case EAST:
        start = center.add(0, min, min);
        end = center.add(0, max, max);
        break;
    }

    return new BiValue<>(start, end);
  }
  
  public static float yaw(Direction direction)
  {
    float hpi = (float) (Math.PI / 2);
    
    switch (direction)
    {
      default:
      case DOWN:
      case UP:
        return 0;

      case NORTH:
        return 0;
        
      case SOUTH:
        return 2 * hpi;
        
      case WEST:
        return hpi;
        
      case EAST:
        return 3 * hpi;
    }
  }
  
  public static float pitch(Direction direction)
  {
    return (float)(direction == Direction.UP ? Math.PI / 2 : (direction == Direction.DOWN ? 3 * Math.PI / 2 : 0));
  }

  public static void dropItems(World world, List<ItemStack> items, BlockPos pos)
  {
    for (ItemStack stack : items)
    {
      if (stack.isEmpty())
      {
        continue;
      }
      
      dropItem(world, stack, pos);
    }
  }
  
  public static void dropItems(World world, ItemStackHandler handler, BlockPos pos)
  {
    for (int i = 0; i < handler.getSlots(); i++)
    {
      ItemStack stack = handler.getStackInSlot(i);
      if (stack.isEmpty())
      {
        continue;
      }
      
      dropItem(world, stack, pos);
    }
  }
  
  public static void dropItem(World world, ItemStack stack, BlockPos pos)
  {
    InventoryHelper.spawnItemStack(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack);
  }

  public static AxisAlignedBB getBoundingBox(BlockPos center, double radius)
  {
    return new AxisAlignedBB(center.add(-radius, -radius, -radius), center.add(radius, radius, radius));
  }
  
  public static AxisAlignedBB getHorizontalBoundingBox(BlockPos center, double radius, double height)
  {
    return new AxisAlignedBB(center.add(-radius, 0, -radius), center.add(radius, height, radius));
  }
  
  public static <T> boolean contains(T[] array, T search)
  {
    return getIndex(array, search) != -1;
  }
  
  public static <T> int getIndex(T[] array, T search)
  {
    for (int i = 0; i < array.length; i++)
    {
      if (array[i] == search)
      {
        return i;
      }
    }
  
    return -1;
  }
  
  public static boolean isInside(BlockPos start, BlockPos end, BlockPos check)
  {
    int x = check.getX();
    int y = check.getY();
    int z = check.getZ();
    
    // sort the coordinates
    int x1 = Math.min(start.getX(), end.getX());
    int x2 = Math.max(start.getX(), end.getX());
    int y1 = Math.min(start.getY(), end.getY());
    int y2 = Math.max(start.getY(), end.getY());
    int z1 = Math.min(start.getZ(), end.getZ());
    int z2 = Math.max(start.getZ(), end.getZ());
    
    return x >= x1 && x <= x2
      && y >= y1 && y <= y2
      && z >= z1 && z <= z2;
  }
}
