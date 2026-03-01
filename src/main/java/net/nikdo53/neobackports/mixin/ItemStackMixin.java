package net.nikdo53.neobackports.mixin;

import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.extensions.ItemStackBackportExtension;
import net.nikdo53.neobackports.io.components.DataComponent;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackBackportExtension {
    @Override
    public @Nullable <T> T get(DataComponent<? extends T> component) {
        return DataComponentRegistry.get((ItemStack) ((Object) this), component);
    }

    @Override
    public <T> T getOrDefault(DataComponent<T> component, T defaultValue) {
        return DataComponentRegistry.getOrDefault((ItemStack) ((Object) this), component, defaultValue);
    }

    @Override
    public boolean has(DataComponent<?> component) {
        return DataComponentRegistry.has((ItemStack) ((Object) this), component);
    }

    @Override
    public <T> void set(DataComponent<? super T> componentType, @Nullable T value) {
        DataComponentRegistry.set((ItemStack) ((Object) this), componentType, value);
    }

    @Override
    public <T> void remove(DataComponent<? super T> componentType) {
        DataComponentRegistry.remove((ItemStack) ((Object) this), componentType);
    }
}
