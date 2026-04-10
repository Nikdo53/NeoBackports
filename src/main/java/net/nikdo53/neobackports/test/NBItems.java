package net.nikdo53.neobackports.test;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.registry.DeferredItem;

import java.util.function.Supplier;

public interface NBItems {
    DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, NeoBackports.MOD_ID);

    RegistryObject<Item> TEST_ITEM = REGISTER.register("test_item", () -> new TestItem(new Item.Properties()));

    static <T extends Item> DeferredItem<T> register(String name, Supplier<? extends T> sup){
      return new DeferredItem<>(REGISTER.register(name, sup));
    }
}
