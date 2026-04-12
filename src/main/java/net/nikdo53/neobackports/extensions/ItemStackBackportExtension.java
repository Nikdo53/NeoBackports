package net.nikdo53.neobackports.extensions;

import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;

import javax.annotation.Nullable;

public interface ItemStackBackportExtension {
    @Nullable
    default <T> T get(DataComponentType<? extends T> component) {
        return DataComponentRegistry.get(neoBackports$selfDontUseThis(), component);
    }

    default <T> T getOrDefault(DataComponentType<T> component, T defaultValue) {
        return DataComponentRegistry.getOrDefault(neoBackports$selfDontUseThis(), component, defaultValue);
    }

    default boolean has(DataComponentType<?> component) {
        return DataComponentRegistry.has(neoBackports$selfDontUseThis(), component);
    }

    default <T> void set(DataComponentType<? super T> componentType, @Nullable T value){
        DataComponentRegistry.set(neoBackports$selfDontUseThis(), componentType, value);
    }

    default <T> void remove(DataComponentType<? super T> componentType){
        DataComponentRegistry.remove(neoBackports$selfDontUseThis(), componentType);
    }



    private ItemStack neoBackports$selfDontUseThis(){
        return (ItemStack) ((Object) this);
    }
}
