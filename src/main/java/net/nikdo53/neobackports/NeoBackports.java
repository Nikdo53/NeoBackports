package net.nikdo53.neobackports;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.networking.NBNetworking;
import net.nikdo53.neobackports.test.NBItems;
import org.slf4j.Logger;

@Mod(NeoBackports.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NeoBackports {
    public static final String MOD_ID = "neobackports";
    public static final Logger LOGGER = LogUtils.getLogger();


    public NeoBackports() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        NBItems.ITEMS_REGISTRY.register(modEventBus);
        DataAttachmentRegistry.init();
        NBNetworking.init();
    }


    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        String data = player.getData(DataAttachmentRegistry.TEST_ATTACHMENT);
        System.out.println("data = [" + data + "] is client = " + player.level().isClientSide());

        player.setData(DataAttachmentRegistry.TEST_ATTACHMENT, data + "1");
    }

    public static ResourceLocation loc(String path){
        return new ResourceLocation(MOD_ID, path);
    }
}
