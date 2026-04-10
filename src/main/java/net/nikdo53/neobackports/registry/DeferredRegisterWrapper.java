package net.nikdo53.neobackports.registry;

import com.google.common.collect.Multimaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class DeferredRegisterWrapper<T> {
    public final DeferredRegister<T> parent;

    public DeferredRegisterWrapper(DeferredRegister<T> parent){
        this.parent = parent;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <I extends T> RegistryObject<I> register(final String name, final Supplier<? extends I> sup) {
        return parent.register(name, sup);
    }


    public Supplier<IForgeRegistry<T>> makeRegistry(final Supplier<RegistryBuilder<T>> sup) {
        return parent.makeRegistry(sup);
    }


    @NotNull
    public TagKey<T> createTagKey(@NotNull String path) {
        return parent.createTagKey(path);
    }


    @NotNull
    public TagKey<T> createTagKey(@NotNull ResourceLocation location) {
        return parent.createTagKey(location);
    }

    @NotNull
    public TagKey<T> createOptionalTagKey(@NotNull String path, @NotNull Set<? extends Supplier<T>> defaults) {
        return parent.createOptionalTagKey(path, defaults);
    }


    @NotNull
    public TagKey<T> createOptionalTagKey(@NotNull ResourceLocation location, @NotNull Set<? extends Supplier<T>> defaults) {
       return parent.createOptionalTagKey(location, defaults);
    }

    public void addOptionalTagDefaults(@NotNull TagKey<T> name, @NotNull Set<? extends Supplier<T>> defaults) {
        parent.addOptionalTagDefaults(name, defaults);
    }

    public void register(IEventBus bus) {
        parent.register(bus);
    }

}
