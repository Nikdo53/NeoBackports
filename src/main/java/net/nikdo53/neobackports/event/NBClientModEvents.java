package net.nikdo53.neobackports.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.datamaps.DataMapsManager;
import net.nikdo53.neobackports.screen.BlurScreenBackports;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NeoBackports.MOD_ID, value = Dist.CLIENT)
public class NBClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ModLoader.get().postEvent(new RegisterMenuScreensEvent());

    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
            event.addPackFinders(
                    BlurScreenBackports.RESOURCE_PACK_ID,
                    PackType.CLIENT_RESOURCES,
                    Component.literal("Blurred Menus"),
                    PackSource.BUILT_IN,
                    false,
                    Pack.Position.TOP
            );
    }
}
