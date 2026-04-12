package net.nikdo53.neobackports.mixin;

import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.extensions.ItemStackBackportExtension;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Supplier;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackBackportExtension {
    @Override
    public @Nullable <T> T get(DataComponentType<? extends T> component) {
        return DataComponentRegistry.get((ItemStack) ((Object) this), component);
    }

    @Override
    public <T> T getOrDefault(DataComponentType<T> component, T defaultValue) {
        return DataComponentRegistry.getOrDefault((ItemStack) ((Object) this), component, defaultValue);
    }

    @Override
    public boolean has(DataComponentType<?> component) {
        return DataComponentRegistry.has((ItemStack) ((Object) this), component);
    }

    @Override
    public <T> void set(DataComponentType<? super T> componentType, @Nullable T value) {
        DataComponentRegistry.set((ItemStack) ((Object) this), componentType, value);
    }

    @Override
    public <T> void remove(DataComponentType<? super T> componentType) {
        DataComponentRegistry.remove((ItemStack) ((Object) this), componentType);
    }

    @Override
    public @Nullable <T> T get(Supplier<DataComponentType<? extends T>> component) {
        return get(component.get());
    }

    @Override
    public <T> T getOrDefault(Supplier<DataComponentType<T>> component, T defaultValue) {
        return getOrDefault(component.get(), defaultValue);
    }

    @Override
    public boolean has(Supplier<DataComponentType<?>> component) {
        return has(component.get());
    }

    @Override
    public <T> void set(Supplier<DataComponentType<T>> component, @Nullable T value) {
        set(component.get(), value);
    }

    @Override
    public <T> void remove(Supplier<DataComponentType<? extends T>> component) {
        remove(component.get());
    }
}
