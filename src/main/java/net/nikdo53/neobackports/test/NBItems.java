package net.nikdo53.neobackports.test;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.components.DataComponent;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;
import net.nikdo53.neobackports.registry.DeferredItem;

import java.util.function.Supplier;

public interface NBItems {
    DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, NeoBackports.MOD_ID);

    static <T extends Item> DeferredItem<T> register(String name, Supplier<? extends T> sup){
      return new DeferredItem<>(ITEMS_REGISTRY.register(name, sup));
    }
}
