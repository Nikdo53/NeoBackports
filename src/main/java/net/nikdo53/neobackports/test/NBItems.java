package net.nikdo53.neobackports.test;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.extensions.IDeferredRegisterExtension;
import net.nikdo53.neobackports.registry.DeferredItem;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public interface NBItems {
    DeferredRegisterTyped.Items REGISTER = IDeferredRegisterExtension.createItems(NeoBackports.MOD_ID);

    DeferredItem<Item> TEST_ITEM = REGISTER.register("test_item", () -> new TestItem(new Item.Properties()));

}
