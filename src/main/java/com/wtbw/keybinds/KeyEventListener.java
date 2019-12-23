package com.wtbw.keybinds;

import com.wtbw.WTBWLib;
import com.wtbw.item.util.ICycleTool;
import com.wtbw.network.CycleToolPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public abstract class KeyEventListener
{
  private static List<KeyEventListener> listeners = new ArrayList<>();
  private static final KeyHandler handler = new KeyHandler();
  
  protected static final Minecraft mc = Minecraft.getInstance();
  
  public final String KEY_BIND_CATEGORY;
  
  public KeyEventListener(String category)
  {
    this.KEY_BIND_CATEGORY = category;
    
    registerKeys();
    
    listeners.add(this);
  }
  
  public abstract void registerKeys();
  
//  {
//    KeyHandler.register(new KeyParser(createKeyBinding("increase_tool_radius", GLFW.GLFW_KEY_EQUAL, KeyConflictContext.IN_GAME))
//    {
//      @Override
//      public void onKeyDown()
//      {
//        ItemStack inHand = getInHand();
//        if (inHand.getItem() instanceof ICycleTool)
//        {
//          CycleToolPacket.send((byte) 1);
//        }
//      }
//
//      @Override
//      public boolean isListening()
//      {
//        return mc.currentScreen == null && getInHand().getItem() instanceof ICycleTool;
//      }
//    });
//
//    KeyHandler.register(new KeyParser(createKeyBinding("decrease_tool_radius", GLFW.GLFW_KEY_MINUS, KeyConflictContext.IN_GAME))
//    {
//      @Override
//      public void onKeyDown()
//      {
//        ItemStack inHand = getInHand();
//        if (inHand.getItem() instanceof ICycleTool)
//        {
//          CycleToolPacket.send((byte) -1);
//        }
//      }
//
//      @Override
//      public boolean isListening()
//      {
//        return mc.currentScreen == null && getInHand().getItem() instanceof ICycleTool;
//      }
//    });
//  }

  protected ItemStack getInHand()
  {
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (player != null)
    {
      return player.getHeldItemMainhand();
    }
    else
    {
      return ItemStack.EMPTY;
    }
  }
  
  
  protected KeyParser register(String name, int key, KeyConflictContext conflictContext, Runnable execute)
  {
    return register(name, key, conflictContext, () -> true, execute);
  }
  
  protected KeyParser register(String name, int key, KeyConflictContext conflictContext, Supplier<Boolean> listening, Runnable execute)
  {
    KeyParser parser = new KeyParser(createKeyBinding(name, key, conflictContext))
    {
      @Override
      public void onKeyDown()
      {
        execute.run();
      }
  
      @Override
      public boolean isListening()
      {
        return listening.get();
      }
    };
    
    handler.register(parser);
    
    return parser;
  }

  protected KeyBinding createKeyBinding(String name, int key, KeyConflictContext conflictContext)
  {
    return new KeyBinding(name, conflictContext, InputMappings.Type.KEYSYM, key, KEY_BIND_CATEGORY);
  }

  public static void update(final TickEvent.ClientTickEvent event)
  {
    if (event.phase == TickEvent.Phase.START)
    {
      handler.update();
    }
  }
}
