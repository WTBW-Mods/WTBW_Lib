package com.wtbw.mods.lib.network;

import com.wtbw.mods.lib.WTBWLib;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class RequestGuiUpdatePacket extends Packet
{
  public final BlockPos pos;
  public final CompoundNBT nbt;
  
  public RequestGuiUpdatePacket(BlockPos pos, CompoundNBT nbt)
  {
    this.pos = pos;
    this.nbt = nbt;
  }
  
  public RequestGuiUpdatePacket(PacketBuffer buffer)
  {
    pos = buffer.readBlockPos();
    nbt = buffer.readCompoundTag();
  }
  
  @Override
  public void toBytes(PacketBuffer buffer)
  {
    buffer.writeBlockPos(pos);
    buffer.writeCompoundTag(nbt);
  }
  
  @Override
  public void handle(Supplier<NetworkEvent.Context> ctx)
  {
    NetworkEvent.Context context = ctx.get();
    NetworkDirection direction = context.getDirection();
    if (direction == NetworkDirection.PLAY_TO_CLIENT)
    {
      context.enqueueWork(() ->
      {
        ServerPlayerEntity sender = context.getSender();
        if (sender != null)
        {
          World world = sender.world;
          if (world != null)
          {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IGuiUpdateHandler)
            {
              ((IGuiUpdateHandler) tileEntity).handleGuiUpdateTag(nbt);
            }
          }
        }
      });
      context.setPacketHandled(true);
    }
    else if (direction == NetworkDirection.PLAY_TO_SERVER)
    {
      context.enqueueWork(() ->
      {
        ServerPlayerEntity sender = context.getSender();
        if (sender != null)
        {
          World world = sender.world;
          if (world != null)
          {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IGuiUpdateHandler)
            {
              IGuiUpdateHandler handler = (IGuiUpdateHandler) tileEntity;
              CompoundNBT tag = handler.getGuiUpdateTag();
              
              Networking.INSTANCE.sendTo(new RequestGuiUpdatePacket(pos, tag), sender.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
          }
        }
      });
      context.setPacketHandled(true);
    }
    else
    {
      WTBWLib.LOGGER.warn("Unknown packet received!");
      new Exception().printStackTrace();
    }
  }
  
  
  public static <T extends TileEntity & IGuiUpdateHandler> void request(T tile)
  {
    request(tile.getPos());
  }
  
  public static void request(BlockPos pos)
  {
    CompoundNBT empty = new CompoundNBT();
    
    RequestGuiUpdatePacket packet = new RequestGuiUpdatePacket(pos, empty);
    Networking.INSTANCE.sendToServer(packet);
  }
}
