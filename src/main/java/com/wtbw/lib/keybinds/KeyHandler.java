package com.wtbw.lib.keybinds;


import com.wtbw.lib.WTBWLib;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public class KeyHandler
{
  private static List<KeyParser> parsers = new ArrayList<>();
//  private static Map<Integer, Boolean> keyStates = new HashMap<>();
//  private static Map<Integer, Boolean> previousStates = new HashMap<>();
//  private static List<Integer> toWatch = new ArrayList<>();
  
  KeyHandler()
  { }
  
  public KeyParser register(KeyParser parser)
  {
    if (Minecraft.getInstance() == null || parser == null || parser.keyBinding == null)
    {
      WTBWLib.LOGGER.warn("Failed to add keybind. This is ok in data generation mode!");
      return parser;
    }
    
    ClientRegistry.registerKeyBinding(parser.keyBinding);
    parsers.add(parser);
    
    return parser;
  }
  
  void update()
  {
    for (KeyParser kp :
      parsers)
    {
      kp.update();
    }
  }
}
