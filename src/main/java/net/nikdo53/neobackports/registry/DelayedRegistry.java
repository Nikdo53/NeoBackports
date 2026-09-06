package net.nikdo53.neobackports.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

//not named deferred to not be confused with neo stuff
public class DelayedRegistry<T> implements Registry<T> {
    public @Nullable Registry<T> parent = null;
    public boolean hasBeenSet = false;

    public void setRegistry(Registry<T> registry){
        if (hasBeenSet) {
            throw new IllegalStateException("Registry has already been set");
        }
        this.parent = registry;
        this.hasBeenSet = true;
    }

    public void setRegistry(IForgeRegistry<T> registry){
        setRegistry(registry.getVanillaRegistry());
    }

    public Registry<T> getRegistry(){
        if (parent == null) {
            throw new IllegalStateException("Tried calling a delayed registry too early");
        }
        return parent;
    }

    @Override
    public ResourceKey<? extends Registry<T>> key() {
        return getRegistry().key();
    }

    @Override
    public @Nullable ResourceLocation getKey(T t) {
        return getRegistry().getKey(t);
    }

    @Override
    public Optional<ResourceKey<T>> getResourceKey(T t) {
        return getRegistry().getResourceKey(t);
    }

    @Override
    public int getId(@Nullable T t) {
        return getRegistry().getId(t);
    }

    @Override
    public @Nullable T byId(int i) {
        return getRegistry().byId(i);
    }

    @Override
    public int size() {
        return getRegistry().size();
    }

    @Override
    public @Nullable T get(@Nullable ResourceKey<T> resourceKey) {
        return getRegistry().get(resourceKey);
    }

    @Override
    public @Nullable T get(@Nullable ResourceLocation resourceLocation) {
        return getRegistry().get(resourceLocation);
    }

    @Override
    public Lifecycle lifecycle(T t) {
        return getRegistry().lifecycle(t);
    }

    @Override
    public Lifecycle registryLifecycle() {
        return null;
    }

    @Override
    public Set<ResourceLocation> keySet() {
        return getRegistry().keySet();
    }

    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return getRegistry().entrySet();
    }

    @Override
    public Set<ResourceKey<T>> registryKeySet() {
        return getRegistry().registryKeySet();
    }

    @Override
    public Optional<Holder.Reference<T>> getRandom(RandomSource randomSource) {
        return getRegistry().getRandom(randomSource);
    }

    @Override
    public boolean containsKey(ResourceLocation resourceLocation) {
        return getRegistry().containsKey(resourceLocation);
    }

    @Override
    public boolean containsKey(ResourceKey<T> resourceKey) {
        return getRegistry().containsKey(resourceKey);
    }

    @Override
    public Registry<T> freeze() {
        return getRegistry().freeze();
    }

    @Override
    public Holder.Reference<T> createIntrusiveHolder(T t) {
        return getRegistry().createIntrusiveHolder(t);
    }

    @Override
    public Optional<Holder.Reference<T>> getHolder(int i) {
        return getRegistry().getHolder(i);
    }

    @Override
    public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> resourceKey) {
        return getRegistry().getHolder(resourceKey);
    }

    @Override
    public Holder<T> wrapAsHolder(T t) {
        return getRegistry().wrapAsHolder(t);
    }

    @Override
    public Stream<Holder.Reference<T>> holders() {
        return getRegistry().holders();
    }

    @Override
    public Optional<HolderSet.Named<T>> getTag(TagKey<T> tagKey) {
        return getRegistry().getTag(tagKey);
    }

    @Override
    public HolderSet.Named<T> getOrCreateTag(TagKey<T> tagKey) {
        return getRegistry().getOrCreateTag(tagKey);
    }

    @Override
    public Stream<Pair<TagKey<T>, HolderSet.Named<T>>> getTags() {
        return getRegistry().getTags();
    }

    @Override
    public Stream<TagKey<T>> getTagNames() {
        return getRegistry().getTagNames();
    }

    @Override
    public void resetTags() {
        getRegistry().resetTags();
    }

    @Override
    public void bindTags(Map<TagKey<T>, List<Holder<T>>> map) {
        getRegistry().bindTags(map);
    }

    @Override
    public HolderOwner<T> holderOwner() {
        return getRegistry().holderOwner();
    }

    @Override
    public HolderLookup.RegistryLookup<T> asLookup() {
        return getRegistry().asLookup();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return getRegistry().iterator();
    }
}
