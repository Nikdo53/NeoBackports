package net.nikdo53.neobackports.registry;

import com.google.common.collect.Multimaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import net.nikdo53.neobackports.extensions.IDeferredRegisterExtension;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegisterWrapper<T>{
    public final DeferredRegister<T> parent;

    public DeferredRegisterWrapper(DeferredRegister<T> parent){
        this.parent = parent;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <I extends T> DeferredHolder<T, I> register(final String name, final Supplier<? extends I> sup) {
        return createHolder(parent.register(name, sup));
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
        return createHolder(parent.register(name, () -> func.apply(new ResourceLocation(getModId(), name))));
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

    /**
     * @return The unmodifiable view of registered entries. Useful for bulk operations on all values.
     */
    public Collection<DeferredHolder<T, T>> getEntries() {
        return parent.getEntries().stream().map(this::createHolder).toList();
    }

    /**
     * Create a {@link DeferredHolder} or an inheriting capabilityType to be stored.
     * @return The new instance of {@link DeferredHolder} or an inheriting capabilityType.
     */
    protected <I extends T> DeferredHolder<T, I> createHolder(RegistryObject<I> registryObject) {
        return DeferredHolder.of(registryObject);
    }

    public void register(IEventBus bus) {
        parent.register(bus);
    }

    public String getModId(){
        return parent.neobackports$getModId();
    }

    @Override
    public String toString() {
        return "Wrapped[" + parent.toString() + "]";
    }
}
