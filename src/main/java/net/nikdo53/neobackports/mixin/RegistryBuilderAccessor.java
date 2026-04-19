package net.nikdo53.neobackports.mixin;

import net.minecraft.core.Holder;
import net.minecraftforge.registries.RegistryBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(value = RegistryBuilder.class, remap = false)
public interface RegistryBuilderAccessor<T> {

    @Invoker("hasWrapper")
    RegistryBuilder<T> setWrapper();

    @Invoker("intrusiveHolderCallback")
    RegistryBuilder<T> addIntrusiveHolderCallback(Function<T, Holder.Reference<T>> intrusiveHolderCallback);
}
