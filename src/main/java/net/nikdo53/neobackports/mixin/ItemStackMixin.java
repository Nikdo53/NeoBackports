package net.nikdo53.neobackports.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.extensions.ItemStackBackportExtension;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackBackportExtension {
    @Shadow
    public abstract void shrink(int decrement);

    @Shadow
    public abstract ItemStack copyWithCount(int count);

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

    @Override
    public ItemStack consumeAndReturn(int amount, @Nullable LivingEntity entity) {
        ItemStack itemstack = copyWithCount(amount);
        consume(amount, entity);
        return itemstack;
    }

    @Override
    public void consume(int amount, @Nullable LivingEntity entity) {
        if (entity == null || !(entity instanceof Player player && player.getAbilities().instabuild)) {
            shrink(amount);
        }
    }
}
