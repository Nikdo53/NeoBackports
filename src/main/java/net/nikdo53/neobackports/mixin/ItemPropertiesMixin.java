package net.nikdo53.neobackports.mixin;

import net.minecraft.world.item.Item;
import net.nikdo53.neobackports.extensions.ItemPropertiesComponentExtension;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin implements ItemPropertiesComponentExtension {
    @Unique
    private final List<DataDefault<?>> DEFAULT_COMPONENTS = new ArrayList<>();

    @Override
    public <T> Item.Properties component(DataComponentType<T> component, T value) {
        DEFAULT_COMPONENTS.add(new DataDefault<>(component, value));
        return ((Item.Properties) (Object) this);
    }

    @Override
    public List<DataDefault<?>> getDefaultComponents() {
        return DEFAULT_COMPONENTS;
    }
}
