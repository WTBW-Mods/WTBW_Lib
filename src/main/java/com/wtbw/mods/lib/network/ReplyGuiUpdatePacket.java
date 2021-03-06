package com.wtbw.mods.lib.network;

import com.wtbw.mods.lib.util.ClientHelper;
import com.wtbw.mods.lib.tile.util.IGuiUpdateHandler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class ReplyGuiUpdatePacket extends Packet
{
  public final BlockPos pos;
  public final CompoundNBT nbt;
  
  public ReplyGuiUpdatePacket(BlockPos pos, CompoundNBT nbt)
  {
    this.pos = pos;
    this.nbt = nbt;
  }
  
  public ReplyGuiUpdatePacket(PacketBuffer buffer)
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
    context.enqueueWork(() ->
    {
      DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
      {
        World world = ClientHelper.getWorld();
        if (world != null)
        {
          TileEntity tileEntity = world.getTileEntity(pos);
          if (tileEntity instanceof IGuiUpdateHandler)
          {
            ((IGuiUpdateHandler) tileEntity).handleGuiUpdateTag(nbt);
          }
        }
      });
    });
    context.setPacketHandled(true);
  }
}
