package com.wtbw.mods.lib.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;
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
