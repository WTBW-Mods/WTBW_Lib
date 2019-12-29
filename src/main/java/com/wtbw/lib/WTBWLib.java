package com.wtbw.lib;

import com.wtbw.lib.keybinds.KeyEventListener;
import com.wtbw.lib.network.Networking;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
  @author: Naxanria
*/
@Mod(WTBWLib.MODID)
public class WTBWLib
{
  public static final String MODID = "wtbw_lib";
  
  public static final Logger LOGGER = LogManager.getLogger(MODID);

  public WTBWLib()
  {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    
    eventBus.addListener(this::setup);
    
    IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
    
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
    {
      forgeEventBus.addListener(KeyEventListener::update);
    });
    
    Networking.registerMessages();
  }
  
  private void setup(final FMLCommonSetupEvent event)
  {
  
  }
}
