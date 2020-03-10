package com.wtbw.mods.lib.network;

import com.wtbw.mods.lib.WTBWLib;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class Networking
{
  public static SimpleChannel INSTANCE;
  private static int id = 0;

  protected static int id()
  {
    return id++;
  }

  public static void registerMessages()
  {
    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(WTBWLib.MODID, "network"), () -> "1.0", s -> true, s -> true);

    INSTANCE.registerMessage(id(), CycleToolPacket.class, CycleToolPacket::toBytes, CycleToolPacket::new, CycleToolPacket::handle);
    INSTANCE.registerMessage(id(), ColoredRedstoneParticlePacket.class, ColoredRedstoneParticlePacket::toBytes, ColoredRedstoneParticlePacket::new, ColoredRedstoneParticlePacket::handle);
    INSTANCE.registerMessage(id(), ButtonClickedPacket.class, ButtonClickedPacket::toBytes, ButtonClickedPacket::new, ButtonClickedPacket::handle);
    INSTANCE.registerMessage(id(), RequestGuiUpdatePacket.class, RequestGuiUpdatePacket::toBytes, RequestGuiUpdatePacket::new, RequestGuiUpdatePacket::handle);
    INSTANCE.registerMessage(id(), ReplyGuiUpdatePacket.class, ReplyGuiUpdatePacket::toBytes, ReplyGuiUpdatePacket::new, ReplyGuiUpdatePacket::handle);
  }
  
  public static int sendAround(World world, BlockPos pos, double radius, Object packet)
  {
    if (world.isRemote)
    {
      throw new IllegalStateException("Can only be used on server side!");
    }
    
    List<PlayerEntity> players = world.getEntitiesWithinAABB(EntityType.PLAYER, Utilities.getBoundingBox(pos, radius), (e) -> true);
    for (PlayerEntity e : players)
    {
      ServerPlayerEntity player = (ServerPlayerEntity) e;
      INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    return players.size();
  }
  
  public static <MSG> void registerMessage(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
  {
    INSTANCE.registerMessage(id(), messageType, encoder, decoder, messageConsumer);
  }

}
