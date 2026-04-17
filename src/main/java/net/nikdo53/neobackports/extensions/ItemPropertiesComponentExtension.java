package net.nikdo53.neobackports.extensions;

import net.minecraft.world.item.Item;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataDefault;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.List;
import java.util.function.Supplier;

public interface ItemPropertiesComponentExtension {
    default <T> Item.Properties component(DataComponentType<T> component, T value) {
        return null;
    }

    default <T> Item.Properties component(DeferredHolder<DataComponentType<?>, DataComponentType<T>> component, T value) {
        return null;
    }

    default List<DataDefault<?>> getDefaultComponents(){
        return List.of();
    }

}
