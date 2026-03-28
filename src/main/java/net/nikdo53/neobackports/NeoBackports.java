package net.nikdo53.neobackports;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.networking.NBNetworking;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import net.nikdo53.neobackports.registry.datamaps.DataMapsManager;
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


    public NeoBackports() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        NBItems.ITEMS_REGISTRY.register(modEventBus);
        DataAttachmentRegistry.init();
        NBNetworking.init();
        DataMapsManager.initDataMaps(modEventBus);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();

/*
        String data = player.getData(DataAttachmentRegistry.TEST_ATTACHMENT);
        System.out.println("data = [" + data + "] is client = " + player.level().isClientSide());

        player.setData(DataAttachmentRegistry.TEST_ATTACHMENT, data + "1");*/



        if (stack.is(Items.STICK)){ // this acts as what would be the .json
            HashMap<DataMapType<Item, ?>, Map<ResourceKey<Item>, ?>> mainMap = new HashMap<>();

            HashMap<ResourceKey<Item>, Integer> valueMap = new HashMap<>();
            valueMap.put(Items.DIAMOND.builtInRegistryHolder().key(), 1);
            valueMap.put(Items.EMERALD.builtInRegistryHolder().key(), 2);
            valueMap.put(Items.GOLD_BLOCK.builtInRegistryHolder().key(), 3);

            mainMap.put(NBDataMaps.TEST_DATA_MAP, valueMap);

            ForgeRegistries.ITEMS.setDataMaps(mainMap);

        }

        System.out.println(stack.toString() + " " + stack.getItemHolder().getData(NBDataMaps.TEST_DATA_MAP));
    }

    public static ResourceLocation loc(String path){
        return new ResourceLocation(MOD_ID, path);
    }
}
