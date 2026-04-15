package net.nikdo53.neobackports.extensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface ItemStackBackportExtension {

    default ItemStack consumeAndReturn(int amount, @Nullable LivingEntity entity) {
        ItemStack itemstack = neoBackports$selfDontUseThis().copyWithCount(amount);
        consume(amount, entity);
        return itemstack;
    }

    default void consume(int amount, @Nullable LivingEntity entity) {
        if (entity == null || !(entity instanceof Player player && player.getAbilities().instabuild)) {
            neoBackports$selfDontUseThis().shrink(amount);
        }
    }

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

    @Nullable
    default <T> T get(Supplier<DataComponentType<? extends T>> component) {
        return DataComponentRegistry.get(neoBackports$selfDontUseThis(), component.get());
    }

    default <T> T getOrDefault(Supplier<DataComponentType<T>> component, T defaultValue) {
        return DataComponentRegistry.getOrDefault(neoBackports$selfDontUseThis(), component.get(), defaultValue);
    }

    default boolean has(Supplier<DataComponentType<?>> component) {
        return DataComponentRegistry.has(neoBackports$selfDontUseThis(), component.get());
    }

    default <T> void set(Supplier<DataComponentType<T>> component, @Nullable T value){
        DataComponentRegistry.set(neoBackports$selfDontUseThis(), component.get(), value);
    }

    default <T> void remove(Supplier<DataComponentType<? extends T>> component){
        DataComponentRegistry.remove(neoBackports$selfDontUseThis(), component.get());
    }


    private ItemStack neoBackports$selfDontUseThis(){
        return (ItemStack) ((Object) this);
    }
}
