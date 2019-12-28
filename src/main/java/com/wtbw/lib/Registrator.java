package com.wtbw.lib;

import com.wtbw.lib.block.BaseTileBlock;
import com.wtbw.lib.gui.container.BaseTileContainer;
import com.wtbw.lib.item.BaseItemProperties;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public abstract class Registrator
{
  protected ItemGroup group;
  protected String modid;
  
  private List<BlockItem> blockItems = new ArrayList<>();
  protected IForgeRegistry<Block> blockRegistry;
  protected IForgeRegistry<Item> itemRegistry;
  protected IForgeRegistry<TileEntityType<?>> tileRegistry;
  protected IForgeRegistry<ContainerType<?>> containerRegistry;
  
  public Registrator(ItemGroup group, String modid)
  {
    this.group = group;
    this.modid = modid;
    
    registerEvents();
  }
  
  protected abstract void registerAllBlocks();

  protected abstract void registerAllItems();
  
  protected abstract void registerAllTiles();
  
  protected abstract void registerAllContainers();

  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  
  public void registerEvents()
  {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

    eventBus.addGenericListener(Block.class, this::registerBlocks);
    eventBus.addGenericListener(Item.class, this::registerItems);
    eventBus.addGenericListener(TileEntityType.class, this::registerTileEntity);
    eventBus.addGenericListener(ContainerType.class, this::registerContainers);
  }
  
  protected BaseItemProperties getItemProperties()
  {
    return new BaseItemProperties().group(group);
  }
  
  protected Block.Properties getBlockProperties(Material material, float hardnessAndResistance)
  {
    return getBlockProperties(material, hardnessAndResistance, hardnessAndResistance);
  }
  
  protected Block.Properties getBlockProperties(Material material, float hardness, float resistance)
  {
    return Block.Properties.create(material).hardnessAndResistance(hardness, resistance);
  }
  
  protected Block.Properties getBlockProperties(Material material, float hardnessAndResistance, DyeColor color)
  {
    return Block.Properties.create(material, color).hardnessAndResistance(hardnessAndResistance);
  }
  
  protected Block.Properties getBlockProperties(Material material, float hardness, float resistance, DyeColor color)
  {
    return Block.Properties.create(material, color).hardnessAndResistance(hardness, resistance);
  }
  
  protected Block.Properties getBlockProperties(Material material, float hardnessAndResistance, MaterialColor color)
  {
    return Block.Properties.create(material, color).hardnessAndResistance(hardnessAndResistance);
  }
  
  protected Block.Properties getBlockProperties(Material material, float hardness, float resistance, MaterialColor color)
  {
    return Block.Properties.create(material, color).hardnessAndResistance(hardness, resistance);
  }

  public void registerBlocks(RegistryEvent.Register<Block> event)
  {
    blockRegistry = event.getRegistry();

    registerAllBlocks();
  }

  public void registerItems(RegistryEvent.Register<Item> event)
  {
    itemRegistry = event.getRegistry();
    for (BlockItem blockItem : blockItems)
    {
      itemRegistry.register(blockItem);
    }

    blockItems.clear();

    registerAllItems();
  }

  public void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event)
  {
    tileRegistry = event.getRegistry();

    registerAllTiles();
  }

  public void registerContainers(RegistryEvent.Register<ContainerType<?>> event)
  {
    containerRegistry = event.getRegistry();

    registerAllContainers();
  }

  protected void registerContainer(IContainerFactory factory, String name)
  {
    containerRegistry.register(IForgeContainerType.create((windowId, inv, data) ->
      factory.create(windowId, ClientSetup.getWorld(), data.readBlockPos(), inv)
    ).setRegistryName(modid, name));
  }

  protected  <T extends Block> T register(T block, String registryName)
  {
    return register(block, registryName, true, null);
  }
  
  protected <T extends Block> T register(T block, String registryName, boolean createBlockItem)
  {
    return register(block, registryName, createBlockItem, null);
  }
  
  protected <T extends Block> T register(T block, String registryName, boolean createBlockItem, Item.Properties blockItemProperties)
  {
    block.setRegistryName(modid, registryName);
    if (createBlockItem)
    {
      if (blockItemProperties == null)
      {
        blockItemProperties = getItemProperties();
      }
      blockItems.add((BlockItem) new BlockItem(block, blockItemProperties).setRegistryName(modid, registryName));
    }

    blockRegistry.register(block);

    return block;
  }
  
  protected <T extends Item> T register(T item, String name)
  {
    itemRegistry.register(item.setRegistryName(modid, name));
    return item;
  }
  
  protected TileEntityType<?> register(BaseTileBlock tileBlock)
  {
    return register(tileBlock.tileEntityProvider::get, tileBlock, tileBlock.getRegistryName().getPath());
  }
  
  protected TileEntityType<?> register(Supplier<TileEntity> factory, Block block, String registryName)
  {
    TileEntityType<TileEntity> type = TileEntityType.Builder.create(factory, block).build(null);
    type.setRegistryName(modid, registryName);
    tileRegistry.register(type);
    return type;
  }

  public interface IContainerFactory
  {
    BaseTileContainer<?> create(int windowId, World world, BlockPos pos, PlayerInventory inv);
  }
}
