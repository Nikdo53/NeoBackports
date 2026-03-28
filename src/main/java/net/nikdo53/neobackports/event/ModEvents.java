package net.nikdo53.neobackports.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.registry.datamaps.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.test.NBDataMaps;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NeoBackports.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(NBDataMaps.TEST_DATA_MAP);
    }
}
