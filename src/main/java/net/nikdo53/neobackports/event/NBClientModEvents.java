package net.nikdo53.neobackports.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.datamaps.DataMapsManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NeoBackports.MOD_ID, value = Dist.CLIENT)
public class NBClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ModLoader.get().postEvent(new RegisterMenuScreensEvent());

    }
}
