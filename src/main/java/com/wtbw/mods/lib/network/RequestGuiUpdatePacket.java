package com.wtbw.mods.lib.network;

import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class RequestGuiUpdatePacket extends Packet
{
  public final BlockPos pos;
  
  public RequestGuiUpdatePacket(BlockPos pos)
  {
    this.pos = pos;
  }
  
  public RequestGuiUpdatePacket(PacketBuffer buffer)
  {
    pos = buffer.readBlockPos();
  }
  
  @Override
  public void toBytes(PacketBuffer buffer)
  {
    buffer.writeBlockPos(pos);
  }
  
  @Override
  public void handle(Supplier<NetworkEvent.Context> ctx)
  {
    NetworkEvent.Context context = ctx.get();
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
            
            Networking.INSTANCE.sendTo(new ReplyGuiUpdatePacket(pos, tag), sender.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
          }
        }
      }
    });
    context.setPacketHandled(true);
  }
  
  public static void request(BlockPos pos)
  {
    RequestGuiUpdatePacket packet = new RequestGuiUpdatePacket(pos);
    Networking.INSTANCE.sendToServer(packet);
  }
}
