package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.nikdo53.neobackports.extensions.ILifecycleRegistryExtension;
import net.nikdo53.neobackports.registry.RegistryLookupWrapper;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements ILifecycleRegistryExtension<T>, Registry<T> {
    @Unique
    Map<ResourceKey<T>, Lifecycle> neoBackports$lifecycleKeyMap = new HashMap<>();

    @WrapOperation(method = "holderOwner", at = @At(value = "FIELD", target = "Lnet/minecraft/core/MappedRegistry;lookup:Lnet/minecraft/core/HolderLookup$RegistryLookup;", opcode = Opcodes.GETFIELD))
    public HolderLookup.RegistryLookup<T> holderOwnerWrapper(MappedRegistry<T> instance, Operation<HolderLookup.RegistryLookup<T>> original) {
        return new RegistryLookupWrapper<>(original.call(instance), instance);
    }

    @WrapOperation(method = "asLookup", at = @At(value = "FIELD", target = "Lnet/minecraft/core/MappedRegistry;lookup:Lnet/minecraft/core/HolderLookup$RegistryLookup;", opcode = Opcodes.GETFIELD))
    public HolderLookup.RegistryLookup<T> asLookupWrapper(MappedRegistry<T> instance, Operation<HolderLookup.RegistryLookup<T>> original) {
        return new RegistryLookupWrapper<>(original.call(instance), instance);
    }

    @Override
    public Map<ResourceKey<T>, Lifecycle> getLifecycleKeyMap() {
        return neoBackports$lifecycleKeyMap;
    }

    @Definition(id = "lifecycles", field = "Lnet/minecraft/core/MappedRegistry;lifecycles:Ljava/util/Map;")
    @Expression("this.lifecycles")
    @Inject(method = "registerMapping(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/core/Holder$Reference;", at = @At("MIXINEXTRAS:EXPRESSION"))
    public void registerMapping(int id, ResourceKey<T> key, T value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<T>> cir){
        neoBackports$lifecycleKeyMap.put(key, lifecycle);
    }

    @Override
    public @NotNull Codec<Holder<T>> holderByNameCodecNeo() {
        Codec<Holder<T>> codec = ResourceLocation.CODEC.flatXmap((p_258174_) -> {
            return this.getHolder(ResourceKey.create(this.key(), p_258174_)).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry key in " + this.key() + ": " + p_258174_;
                });
            });
        }, (p_206061_) -> {
            return p_206061_.unwrapKey().map(ResourceKey::location).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry element in " + this.key() + ":" + p_206061_;
                });
            });
        });
        return ExtraCodecs.overrideLifecycle(codec, (p_258178_) -> {
            return getLifecycleKeyMap().get(p_258178_.getKey());
        }, (p_258171_) -> {
            return getLifecycleKeyMap().get(p_258171_.getKey());
        });
    }

    @Override
    public Codec<Holder<T>> holderByNameCodec() {
        Codec<Holder<T>> codec = ResourceLocation.CODEC.flatXmap((p_258174_) -> {
            return this.getHolder(ResourceKey.create(this.key(), p_258174_)).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry key in " + this.key() + ": " + p_258174_;
                });
            });
        }, (p_206061_) -> {
            return p_206061_.unwrapKey().map(ResourceKey::location).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown registry element in " + this.key() + ":" + p_206061_;
                });
            });
        });
        return ExtraCodecs.overrideLifecycle(codec, this::neoBackports$getLifecycleFromHolder, this::neoBackports$getLifecycleFromHolder);    
    }
    
    @Unique
    public Lifecycle neoBackports$getLifecycleFromHolder(Holder<T> holder){
        ResourceKey<T> key = holder.getKey();
        if (key != null) {
            Lifecycle lifecycle = getLifecycleKeyMap().get(key);
            if (lifecycle != null)
                return lifecycle;
        }
        return this.lifecycle(holder.value());

    }
}
