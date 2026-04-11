package net.nikdo53.neobackports.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DeferredHolder<R, T extends R> implements Holder<T> {
    protected final RegistryObject<T> registryObject;

    public DeferredHolder(RegistryObject<T> registryObject) {
        this.registryObject = registryObject;
    }

    public static <R1, T1 extends R1> DeferredHolder<R1, T1> of(RegistryObject<T1> registryObject){
        return new DeferredHolder<>(registryObject);
    }

    public RegistryObject<T> getRegistryObject() {
        return registryObject;
    }

    public Holder<T> getDelegate() {
        return registryObject.getHolder().orElseThrow();
    }

    @Override
    public @NotNull T value() {
        return registryObject.get();
    }

    public Optional<T> asOptional() {
        return isBound() ? Optional.of(value()) : Optional.empty();
    }

    public ResourceLocation getId() {
        return getKey().location();
    }

    public ResourceKey<T> getKey() {
        return registryObject.getKey();
    }

    @Override
    public boolean isBound() {
        return getDelegate() != null && getDelegate().isBound();
    }

    @Override
    public boolean is(ResourceLocation resourceLocation) {
        return getKey().location().equals(resourceLocation);
    }

    @Override
    public boolean is(ResourceKey<T> resourceKey) {
        return resourceKey == getKey();
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return predicate.test(getKey());
    }

    @Override
    public boolean is(TagKey<T> tagKey) {
        return getDelegate() != null && getDelegate().is(tagKey);
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return getDelegate() != null ? getDelegate().tags() : Stream.empty();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return Either.left(getKey());
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.of(getKey());
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> holderOwner) {
        return getDelegate() != null && getDelegate().canSerializeIn(holderOwner);
    }

    @Override
    public boolean equals(Object obj) {
        return registryObject.equals(obj);
    }

    @Override
    public String toString() {
        return registryObject.toString();
    }
}
