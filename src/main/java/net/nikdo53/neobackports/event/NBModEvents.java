package net.nikdo53.neobackports.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.crafting.*;
import net.minecraftforge.common.crafting.conditions.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.datagen.condition.neoforge.*;
import net.nikdo53.neobackports.datamaps.NeoForgeDataMaps;
import net.nikdo53.neobackports.io.networking.NBNetworking;
import net.nikdo53.neobackports.io.networking.PayloadRegistrar;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import net.nikdo53.neobackports.datamaps.DataMapsManager;
import net.nikdo53.neobackports.test.NBDataMaps;
import net.nikdo53.neobackports.test.TestPacket;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NeoBackports.MOD_ID)
public class NBModEvents {

    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(NBDataMaps.TEST_DATA_MAP);
        event.register(NeoForgeDataMaps.COMPOSTABLES);
        event.register(NeoForgeDataMaps.FURNACE_FUELS);

    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        DataMapsManager.initDataMaps();
        ModLoader.get().postEvent(new RegisterPayloadHandlersEvent());
    }

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(NBNetworking.CHANNEL, NeoBackports.MOD_ID);

        registrar.playToClient(TestPacket.TYPE, TestPacket.STREAM_CODEC, TestPacket::handle);
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        ResourceProvider resourceProvider = event.getResourceProvider();
        BlurShaderLoader.INSTANCE.loadBlurEffect(resourceProvider);
    }
    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event) {
        ForgeRegistryHelper.getInstance(NeoForgeRegistries.Keys.ATTACHMENT_TYPES)
                .createWithWrapper(event, reg -> NeoForgeRegistries.ATTACHMENT_TYPES_REAL = reg);

        ForgeRegistryHelper.getInstance(NeoForgeRegistries.Keys.DATA_COMPONENT_TYPE)
                .createWithWrapper(event, reg -> NeoForgeRegistries.DATA_COMPONENT_TYPE = reg);

    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event){
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(NeoAndCondition.Serializer.INSTANCE);
            CraftingHelper.register(NeoFalseCondition.Serializer.INSTANCE);
            CraftingHelper.register(NeoItemExistsCondition.Serializer.INSTANCE);
            CraftingHelper.register(NeoModLoadedCondition.Serializer.INSTANCE);
            CraftingHelper.register(NeoNotCondition.Serializer.INSTANCE);
            CraftingHelper.register(NeoOrCondition.Serializer.INSTANCE);
            CraftingHelper.register(NeoTrueCondition.Serializer.INSTANCE);
            CraftingHelper.register(NeoTagEmptyCondition.Serializer.INSTANCE);
        }
    }

}
