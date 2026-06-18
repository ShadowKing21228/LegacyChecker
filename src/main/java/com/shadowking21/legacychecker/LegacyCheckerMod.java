package com.shadowking21.legacychecker;

import com.shadowking21.legacychecker.config.ConfigRegistry;
import com.shadowking21.legacychecker.handler.ModCheckHandler;
import com.shadowking21.legacychecker.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = LCReference.MOD_ID, name = LCReference.MOD_NAME, version = LCReference.VERSION, dependencies = "required-after:shadowconfig")
public class LegacyCheckerMod {

    public static final Logger LOGGER = LogManager.getLogger("LegacyChecker");

    @SidedProxy(modId = LCReference.MOD_ID, clientSide = "com.shadowking21.legacychecker.proxy.ClientProxy", serverSide = "com.shadowking21.legacychecker.proxy.CommonProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigRegistry.init();
        //ModCheckHandler.checkRequiredMods();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ModCheckHandler());
    }
}
