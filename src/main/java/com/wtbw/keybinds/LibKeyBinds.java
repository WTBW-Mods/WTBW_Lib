package com.wtbw.keybinds;

import com.wtbw.item.util.ICycleTool;
import com.wtbw.network.CycleToolPacket;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

/*
  @author: Naxanria
*/
public class LibKeyBinds
{
  public static KeyParser INCREASE_TOOL_RADIUS = null;
  public static KeyParser DECREASE_TOOL_RADIUS = null;
  
  public static final Default DEFAULT = new Default();
  
  public static class Default extends KeyEventListener
  {
    private Default()
    {
      super("wtbw");
    }
  
    @Override
    public void registerKeys()
    { }
    
    public void registerToolRadiusKeyBinds()
    {
      if (INCREASE_TOOL_RADIUS == null)
      {
        INCREASE_TOOL_RADIUS = register("increase_tool_radius", GLFW.GLFW_KEY_EQUAL, KeyConflictContext.IN_GAME, () -> mc.currentScreen == null && getInHand().getItem() instanceof ICycleTool, () ->
        {
          if (getInHand().getItem() instanceof ICycleTool)
          {
            CycleToolPacket.send((byte) 1);
          }
        });
      }
      
      if (DECREASE_TOOL_RADIUS == null)
      {
        DECREASE_TOOL_RADIUS = register("decrease_tool_radius", GLFW.GLFW_KEY_MINUS, KeyConflictContext.IN_GAME, () -> mc.currentScreen == null && getInHand().getItem() instanceof ICycleTool, () ->
        {
          if (getInHand().getItem() instanceof ICycleTool)
          {
            CycleToolPacket.send((byte) -1);
          }
        });
      }
    }
  }
}
