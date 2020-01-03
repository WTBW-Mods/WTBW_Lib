package com.wtbw.mods.lib.network;

import com.wtbw.mods.lib.WTBWLib;
import com.wtbw.mods.lib.item.util.ICycleTool;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class CycleToolPacket extends Packet
{
  private final byte dir;

  public CycleToolPacket(byte dir)
  {
    this.dir = dir;
  }

  public CycleToolPacket(PacketBuffer buffer)
  {
    dir = buffer.readByte();
  }

  public static void send(byte dir)
  {
    Networking.INSTANCE.sendToServer(new CycleToolPacket(dir));
  }

  @Override
  public void toBytes(PacketBuffer buffer)
  {
    buffer.writeByte(dir);
  }

  @Override
  public void handle(Supplier<NetworkEvent.Context> ctx)
  {
    ctx.get().enqueueWork(() ->
    {
      ServerPlayerEntity sender = ctx.get().getSender();
      if (sender != null)
      {
        ItemStack heldItemMainhand = sender.getHeldItemMainhand();
        if (heldItemMainhand.getItem() instanceof ICycleTool)
        {
          ICycleTool cycleTool = (ICycleTool) heldItemMainhand.getItem();
          if (cycleTool.getMaxRadius() > 1)
          {
            int r = cycleTool.cycleRadius(heldItemMainhand, dir);
            sender.sendStatusMessage(new TranslationTextComponent(WTBWLib.MODID + ".text.cycled", r), true);
          }
        }
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
