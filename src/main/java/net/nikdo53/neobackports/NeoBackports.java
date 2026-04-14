package net.nikdo53.neobackports;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikdo53.neobackports.io.attachment.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;
import net.nikdo53.neobackports.io.components.DataComponents;
import net.nikdo53.neobackports.io.networking.NBNetworking;
import net.nikdo53.neobackports.datamaps.DataMapType;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;
import net.nikdo53.neobackports.test.NBDataMaps;
import net.nikdo53.neobackports.test.NBItems;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(NeoBackports.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NeoBackports {
    public static final String MOD_ID = "neobackports";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static boolean DEBUG_ENABLED = false;


    public NeoBackports() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        NBItems.REGISTER.register(modEventBus);
        NBNetworking.init();
        DataAttachmentRegistry.ATTACHMENT_TYPES.register(modEventBus);
        DataComponents.VANILLA_COMPONENTS.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENTS.register(modEventBus);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!DEBUG_ENABLED) return;

        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        if (player.level().isClientSide()) {
            System.out.println("client:");
        }else {
            System.out.println("server:");
        }

        System.out.println(stack.toString() + " " + stack.getItemHolder().getData(NBDataMaps.TEST_DATA_MAP));
    }

    public static ResourceLocation loc(String path){
        return new ResourceLocation(MOD_ID, path);
    }
}
