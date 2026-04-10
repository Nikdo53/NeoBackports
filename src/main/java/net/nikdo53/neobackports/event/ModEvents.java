package net.nikdo53.neobackports.event;

import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import net.nikdo53.neobackports.datamaps.DataMapsManager;
import net.nikdo53.neobackports.datamaps.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.test.NBDataMaps;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NeoBackports.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(NBDataMaps.TEST_DATA_MAP);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        DataMapsManager.initDataMaps();
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        ResourceProvider resourceProvider = event.getResourceProvider();
        BlurShaderLoader.INSTANCE.loadBlurEffect(resourceProvider);
    }
    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event) {
        ForgeRegistryHelper.getInstance(NeoForgeRegistries.Keys.ATTACHMENT_TYPES)
                .create(event, reg -> NeoForgeRegistries.ATTACHMENT_TYPES = reg);
    }

}
