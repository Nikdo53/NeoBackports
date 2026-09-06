package net.nikdo53.neobackports.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.nikdo53.neobackports.extensions.IDataMapHolderExtension;
import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(Holder.class)
public interface HolderMixin<T> extends IDataMapHolderExtension<T> {
    @Shadow
    Optional<ResourceKey<T>> unwrapKey();

    @Shadow
    boolean is(ResourceKey<T> resourceKey);

    @Override
    @Nullable
    default <A> A getData(DataMapType<T, A> type) {
        return null;
    }

    @Override
    default String getRegisteredName() {
        return unwrapKey().map(p_316542_ -> p_316542_.location().toString()).orElse("[unregistered]");
    }

    @Override
    @Nullable
    default ResourceKey<T> getKey() {
        return unwrapKey().orElse(null);
    }

    @Override
    default boolean is(Holder<T> holder) {
        ResourceKey<T> key = getKey();
        if (key == null) return false;
        return is(key);
    }
}
