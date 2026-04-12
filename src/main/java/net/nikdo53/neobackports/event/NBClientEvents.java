package net.nikdo53.neobackports.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.screen.OptionsScreenBackports;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NeoBackports.MOD_ID, value = Dist.CLIENT)
public class NBClientEvents {

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Unload event){
        OptionsScreenBackports.PANORAMA = null;
    }
}
