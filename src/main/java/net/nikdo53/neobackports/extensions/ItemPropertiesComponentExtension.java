package net.nikdo53.neobackports.extensions;

import net.minecraft.world.item.Item;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataDefault;

import java.util.List;

public interface ItemPropertiesComponentExtension {
    default <T> Item.Properties component(DataComponentType<T> component, T value) {
        return null;
    }

    default List<DataDefault<?>> getDefaultComponents(){
        return List.of();
    }

}
