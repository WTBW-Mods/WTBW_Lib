package com.wtbw.mods.lib.util;

import com.wtbw.mods.lib.WTBWLib;
import com.wtbw.mods.lib.tile.util.InventoryWrapper;
import com.wtbw.mods.lib.util.rand.RandomUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
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
  
  public static <R extends IRecipe<IInventory>> R getRecipe(World world, IRecipeType<R> type, ItemStack stack)
  {
    InventoryWrapper wrapper = new InventoryWrapper(stack);
    return getRecipe(world, type, wrapper);
  }
  
  public static <R extends IRecipe<IInventory>> R getRecipe(World world, IRecipeType<R> type, List<ItemStack> stacks)
  {
    return getRecipe(world, type, new InventoryWrapper(stacks));
  }
  
  public static <C extends IInventory, R extends IRecipe<C>> R getRecipe(World world, IRecipeType<R> type, C inventory)
  {
    Optional<R> recipe = world.getRecipeManager().getRecipe(type, inventory, world);
    
    return recipe.orElse(null);
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
  
  public static <T> NonNullList<T> nnListOf(T... toList)
  {
    NonNullList<T> list = NonNullList.create();
    list.addAll(Arrays.asList(toList));
    return list;
  }
  
  public static <T> T getSafe(T[] array, int index)
  {
    return array[index % array.length];
  }
  
  public static <T> T getNext(T[] array, T current)
  {
    return getSafe(array, getIndex(array, current) + 1);
  }

  public static Area getArea(BlockPos pos, Direction facing, int size)
  {
    return getArea(pos, facing, size, size);
  }
  
  public static Area getArea(BlockPos pos, Direction facing, int size, int height)
  {
    Vec3i directionVec = facing.getDirectionVec();
    int dirX = directionVec.getX();
    int dirY = directionVec.getY();
    int dirZ = directionVec.getZ();
    
    int hSize = size / 2;
  
    int startX = dirX == 0 ? pos.getX() - hSize : pos.getX() + ((dirX == 1) ? size - 1 : - size + 1);
    int startY = dirY == 0 ? pos.getY() - hSize : pos.getY() + ((dirY == 1) ? size - 1 : - size + 1);
    int startZ = dirZ == 0 ? pos.getZ() - hSize : pos.getZ() + ((dirZ == 1) ? size - 1 : - size + 1);
    int endX = dirX == 0 ? pos.getX() + hSize : pos.getX();
    int endY = dirY == 0 ? pos.getY() + hSize : pos.getY();
    int endZ = dirZ == 0 ? pos.getZ() + hSize : pos.getZ();

    if (height != size)
    {
      startY = pos.getY() + (dirY == 0 ? -height : 0);
      endY = pos.getY() + (dirY == 0 ? 0 : dirY * height);
    }
  
    return new Area(new BlockPos(startX, startY, startZ), new BlockPos(endX, endY, endZ));
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
  
  public static BiValue<BlockPos, BlockPos> sort(BlockPos start, BlockPos end)
  {
    int x1 = Math.min(start.getX(), end.getX());
    int x2 = Math.max(start.getX(), end.getX());
    int y1 = Math.min(start.getY(), end.getY());
    int y2 = Math.max(start.getY(), end.getY());
    int z1 = Math.min(start.getZ(), end.getZ());
    int z2 = Math.max(start.getZ(), end.getZ());
    
    return new BiValue<>(new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
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
  
  public static int sendPowerAround(World world, BlockPos pos, int toSend, boolean shareEqually)
  {
    return sendPowerAround(world, pos, toSend, shareEqually, Direction.values());
  }
  
  /**
   * Shares power to neighbouring energy acceptors
   * @param world the world
   * @param pos the position to send from
   * @param toSend the amount of power to send
   * @param shareEqually if each gets an equal share, or first come first serve
   * @param sides what sides to send to, null for all sides
   * @return the amount not send
   */
  public static int sendPowerAround(World world, BlockPos pos, int toSend, boolean shareEqually, @Nullable Direction[] sides)
  {
    if (toSend <= 0)
    {
      return 0;
    }
    
    if (sides == null)
    {
      sides = Direction.values();
    }
    
    List<IEnergyStorage> storageFound = new ArrayList<>();
    
    for(Direction side : sides)
    {
      TileEntity tileEntity = world.getTileEntity(pos.offset(side));
      if (tileEntity != null)
      {
        LazyOptional<IEnergyStorage> capability = tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
        capability.ifPresent(
          storage1 ->
          {
            if (storage1.canReceive())
            {
              storageFound.add(storage1);
            }
          });
      }
    }
    
    if (storageFound.size() == 0)
    {
      return toSend;
    }
    
    if (shareEqually)
    {
      int each = toSend / storageFound.size();

      for (int i = 0; i < storageFound.size(); i++)
      {
        int insert = storageFound.get(i).receiveEnergy(each, false);
        if (insert == 0)
        {
          storageFound.remove(i);
          i--;
          continue;
        }
        toSend -= insert;
      }
    }
    else
    {
      // shuffle so we spread randomly; more equally
      RandomUtil.shuffle(world.rand, storageFound);
      for (int i = 0; i < storageFound.size(); i++)
      {
        int insert = storageFound.get(i).receiveEnergy(toSend, false);
        if (insert == 0)
        {
          storageFound.remove(i);
          i--;
          continue;
        }
        toSend -= insert;
      }
    }
    
    return toSend;
  }
  
  public static ITextComponent getTitle(TileEntity tile)
  {
    return new TranslationTextComponent("block." + tile.getType().getRegistryName().toString().replace(":", "."));
  }
  
  public static boolean isInBounds(int value, int min, int max)
  {
    return value >= min && value <= max;
  }
  
  public static DecimalFormat df_1 = new DecimalFormat("#.#");
  public static DecimalFormat df_2 = new DecimalFormat("#.##");
  public static String abbreviate(int num)
  {
    if (num >= 1000000000)
    {
      
      return df_2.format(num / (double) 1000000000) + "G";
    }
  
    if (num >= 1000000)
    {
      return df_2.format(num / (double) 1000000) + "M";
    }
  
    if (num >= 1000)
    {
      return df_1.format(num / (double) 1000) + "k";
    }
    
    return String.valueOf(num);
  }
  
  public static String getTooltip(@Nonnull IEnergyStorage storage, boolean abbreviated)
  {
    String energy = abbreviated ? Utilities.abbreviate(storage.getEnergyStored()) : String.valueOf(storage.getEnergyStored());
    String capacity = abbreviated ? Utilities.abbreviate(storage.getMaxEnergyStored()) : String.valueOf(storage.getMaxEnergyStored());
    
    return energy + "/" + capacity + " FE";
  }
  
  public static String getTooltip(@Nonnull IFluidTank tank, boolean abbreviated)
  {
    int fluidAmount = tank.getFluidAmount();
    int fluidCapacity = tank.getCapacity();
    
    String amount = abbreviated ? Utilities.abbreviate(fluidAmount / 1000) : String.valueOf(fluidAmount);
    String capacity = abbreviated ? Utilities.abbreviate(fluidCapacity / 1000) : String.valueOf(fluidCapacity);
    
    return amount + "/" + capacity + " mB";
  }
  
  
  public static <T> T make(T t, Consumer<T> consumer)
  {
    consumer.accept(t);
    
    return t;
  }
  
  public static boolean isSlimeChunk(World world, BlockPos pos)
  {
    return isSlimeChunk(world, new ChunkPos(pos));
  }
  
  public static boolean isSlimeChunk(World world, ChunkPos pos)
  {
    return SharedSeedRandom.seedSlimeChunk(pos.x, pos.z, world.getSeed(), 987234911L).nextInt(10) == 0;
  }
  
  public static int distanceSqr(ChunkPos a, ChunkPos b)
  {
    int dx = a.x - b.x;
    int dz = a.z - b.z;
    return dx * dx + dz * dz;
  }
  
  public static int distanceSqr(BlockPos a, BlockPos b)
  {
    int dx = a.getX() - b.getX();
    int dy = a.getY() - b.getY();
    int dz = a.getZ() - b.getZ();
    return dx * dx + dy * dy + dz * dz;
  }
  
  public static int spawnExp(World world, BlockPos pos, BlockState state, int fortune, int silk)
  {
    return spawnExp(world, pos, state.getExpDrop(world, pos, fortune, silk));
  }
  
  public static int spawnExp(World world, BlockPos pos, int exp)
  {
    return spawnExp(world, pos, exp, true);
  }
  
  public static int spawnExp(World world, BlockPos pos, int exp, boolean split)
  {
    if (exp > 0)
    {
      if (split)
      {
        while (exp > 0)
        {
          int drop = ExperienceOrbEntity.getXPSplit(exp);
          exp -= drop;
          world.addEntity(new ExperienceOrbEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, drop));
        }
      }
      else
      {
        world.addEntity(new ExperienceOrbEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, exp));
      }
    }
    
    return exp;
  }
  
  public static double getEnergyPercentage(IEnergyStorage storage)
  {
    if (storage.getMaxEnergyStored() == 0)
    {
      return 0;
    }
    
    return 1d - (storage.getEnergyStored() / (double) storage.getMaxEnergyStored());
  }
}
