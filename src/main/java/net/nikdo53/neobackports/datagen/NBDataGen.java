package net.nikdo53.neobackports.datagen;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nikdo53.neobackports.NeoBackports;

@Mod.EventBusSubscriber(modid = NeoBackports.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NBDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();


        generator.addProvider(event.includeServer(), new NBDataMapsProvider(packOutput, lookupProvider));
    }

}
