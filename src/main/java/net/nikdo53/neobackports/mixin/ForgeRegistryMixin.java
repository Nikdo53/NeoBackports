package net.nikdo53.neobackports.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.nikdo53.neobackports.datamaps.DataMapType;
import net.nikdo53.neobackports.extensions.IForgeRegistryExtension;
import net.nikdo53.neobackports.extensions.IRegistryDataMapExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(value = ForgeRegistry.class, remap = false)
public abstract class ForgeRegistryMixin<V> implements IRegistryDataMapExtension<V>, IForgeRegistryExtension<V> {
    @Unique
    private static final ResourceLocation WRAPPER_ID = new ResourceLocation("forge", "registry_defaulted_wrapper");

    @Shadow
    @Final
    private boolean hasWrapper;

    @Shadow
    public abstract ResourceKey<Registry<V>> getRegistryKey();


    @Shadow
    public abstract ResourceLocation getRegistryName();

    @Shadow
    public abstract <T> T getSlaveMap(ResourceLocation name, Class<T> type);

    @Shadow
    @Final
    private Map<ResourceLocation, ?> slaves;

    @Override
    public Map<DataMapType<V, ?>, Map<ResourceKey<V>, ?>> getDataMaps() {
        return neoBackports$getVanillaOrThrow().getDataMaps();
    }

    @Unique
    private @NotNull Registry<V> neoBackports$getVanillaOrThrow() {
        Registry<V> registry = (Registry<V>) BuiltInRegistries.REGISTRY.get(getRegistryName());
        if (registry == null){
            throw new IllegalStateException("Cannot get data maps on forge registry " + getRegistryName() + " as it does not have a vanilla counterpart");
        }
        return registry;
    }

    @Override
    public <A> Map<ResourceKey<V>, A> getDataMap(DataMapType<V, A> type) {
        return neoBackports$getVanillaOrThrow().getDataMap(type);
    }

    @Override
    public @Nullable <A> A getData(DataMapType<V, A> type, ResourceKey<V> key) {
        return neoBackports$getVanillaOrThrow().getData(type, key);
    }

    @Override
    @SuppressWarnings("unchecked, rawtypes")
    public HolderLookup.RegistryLookup<V> getRegistryLookup() {
        if (hasWrapper)
            return BuiltInRegistries.REGISTRY.get((ResourceKey) getRegistryKey()).asLookup();
        throw new IllegalStateException("forge registry " + getRegistryName() + " has no wrapper!");
    }

    @Override
    public Registry<V> getVanillaRegistry() {
        return (Registry<V>) slaves.get(WRAPPER_ID);
    }
}
