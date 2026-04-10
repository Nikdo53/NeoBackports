package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.nikdo53.neobackports.extensions.IRegistryDataMapExtension;
import net.nikdo53.neobackports.registry.RegistryLookupWrapper;
import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements IRegistryDataMapExtension<T> {

    @Shadow
    public abstract ResourceKey<? extends Registry<T>> key();

    @Override
    public Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps() {
        return neoBackports$getForgeRegistry().getDataMaps();
    }

    @Unique
    private ForgeRegistry<T> neoBackports$getForgeRegistry() {
        return RegistryManager.ACTIVE.getRegistry(key());
    }

    @Override
    public void setDataMaps(Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> maps) {
        neoBackports$getForgeRegistry().setDataMaps(maps);
    }

    @Override
    public <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> type) {
        return neoBackports$getForgeRegistry().getDataMap(type);
    }

    @Override
    public @Nullable <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        return neoBackports$getForgeRegistry().getData(type, key);
    }



/*    @WrapOperation(method = "<init>(Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Lifecycle;Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/core/MappedRegistry;lookup:Lnet/minecraft/core/HolderLookup$RegistryLookup;", opcode = Opcodes.PUTFIELD))
    private void wrapLookupField(MappedRegistry<T> instance, HolderLookup.RegistryLookup<T> value, Operation<Void> original){
        original.call(instance, new RegistryLookupWrapper<>(value, instance));
    }*/

    @WrapOperation(method = "holderOwner", at = @At(value = "FIELD", target = "Lnet/minecraft/core/MappedRegistry;lookup:Lnet/minecraft/core/HolderLookup$RegistryLookup;", opcode = Opcodes.GETFIELD))
    public HolderLookup.RegistryLookup<T> holderOwnerWrapper(MappedRegistry<T> instance, Operation<HolderLookup.RegistryLookup<T>> original) {
        return new RegistryLookupWrapper<>(original.call(instance), instance);
    }

    @WrapOperation(method = "asLookup", at = @At(value = "FIELD", target = "Lnet/minecraft/core/MappedRegistry;lookup:Lnet/minecraft/core/HolderLookup$RegistryLookup;", opcode = Opcodes.GETFIELD))
    public HolderLookup.RegistryLookup<T> asLookupWrapper(MappedRegistry<T> instance, Operation<HolderLookup.RegistryLookup<T>> original) {
        return new RegistryLookupWrapper<>(original.call(instance), instance);
    }

}
