package net.nikdo53.neobackports.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.nikdo53.neobackports.extensions.IRegistryDataMapExtension;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RegistryLookupWrapper<T> implements HolderLookup.RegistryLookup<T> {
    public HolderLookup.RegistryLookup<T> parent;
    public IRegistryDataMapExtension<T> dataMapGetter;

    public RegistryLookupWrapper(HolderLookup.RegistryLookup<T> parent, IRegistryDataMapExtension<T> dataMapGetter){
        this.parent = parent;
        this.dataMapGetter = dataMapGetter;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        if (owner instanceof RegistryLookupWrapper<T> wrapper){
            return parent.canSerializeIn(wrapper.parent);
        }
        return owner == this;
    }

    @Override
    public Optional<HolderSet.Named<T>> get(TagKey<T> tagKey) {
        return parent.get(tagKey);
    }

    @Override
    public Optional<Holder.Reference<T>> get(ResourceKey<T> resourceKey) {
        return parent.get(resourceKey);
    }

    @Override
    public HolderSet.Named<T> getOrThrow(TagKey<T> tagKey) {
        return parent.getOrThrow(tagKey);
    }

    @Override
    public Holder.Reference<T> getOrThrow(ResourceKey<T> resourceKey) {
        return parent.getOrThrow(resourceKey);
    }

    @Override
    public Stream<HolderSet.Named<T>> listTags() {
        return parent.listTags();
    }

    @Override
    public Stream<Holder.Reference<T>> listElements() {
        return parent.listElements();
    }

    @Override
    public HolderLookup<T> filterElements(Predicate<T> predicate) {
        return parent.filterElements(predicate);
    }

    @Override
    public Stream<TagKey<T>> listTagIds() {
        return parent.listTagIds();
    }

    @Override
    public Stream<ResourceKey<T>> listElementIds() {
        return parent.listElementIds();
    }

    @Override
    public Lifecycle registryLifecycle() {
        return parent.registryLifecycle();
    }

    @Override
    public ResourceKey<? extends Registry<? extends T>> key() {
        return parent.key();
    }

    @Override
    public HolderLookup<T> filterFeatures(FeatureFlagSet enabledFeatures) {
        return parent.filterFeatures(enabledFeatures);
    }

    @Override
    public @Nullable <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        return dataMapGetter.getData(type, key);
    }
}
