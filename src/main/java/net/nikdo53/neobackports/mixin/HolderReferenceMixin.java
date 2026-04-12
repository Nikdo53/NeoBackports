package net.nikdo53.neobackports.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.nikdo53.neobackports.extensions.IDataMapHolderExtension;
import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin<T> implements IDataMapHolderExtension<T> {

    @Shadow
    @Final
    private HolderOwner<T> owner;

    @Shadow
    public abstract ResourceKey<T> key();

    @Shadow
    public abstract Optional<ResourceKey<T>> unwrapKey();

    @Override
    public @Nullable <A> A getData(DataMapType<T, A> type) {
        if (owner instanceof HolderLookup.RegistryLookup<T> lookup) {
            return lookup.getData(type, key());
        }
        return null;
    }

    @Override
    public String getRegisteredName() {
        return unwrapKey().map(p_316542_ -> p_316542_.location().toString()).orElse("[unregistered]");
    }

}
