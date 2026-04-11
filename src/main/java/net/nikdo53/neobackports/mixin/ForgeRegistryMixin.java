package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.datamaps.DataMapType;
import net.nikdo53.neobackports.extensions.IRegistryDataMapExtension;
import net.nikdo53.neobackports.registry.ForgeRegistryLookup;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = ForgeRegistry.class, remap = false)
public abstract class ForgeRegistryMixin<V> implements IRegistryDataMapExtension<V> {

    @Shadow
    @Final
    private boolean hasWrapper;

    @Shadow
    public abstract ResourceKey<Registry<V>> getRegistryKey();

    @Unique
    Map<DataMapType<V, ?>, Map<ResourceKey<V>, ?>> neoBackports$dataMaps = new HashMap<>();

    @Override
    public Map<DataMapType<V, ?>, Map<ResourceKey<V>, ?>> getDataMaps() {
        return neoBackports$dataMaps;
    }

    @Override
    public void setDataMaps(Map<DataMapType<V, ?>, Map<ResourceKey<V>, ?>> maps) {
        neoBackports$dataMaps = maps;
    }

    @Override
    public <A> Map<ResourceKey<V>, A> getDataMap(DataMapType<V, A> type) {
        return (Map<ResourceKey<V>, A>) neoBackports$dataMaps.getOrDefault(type, Map.of());
    }

    @Override
    public @Nullable <A> A getData(DataMapType<V, A> type, ResourceKey<V> key) {
        final var innerMap = neoBackports$dataMaps.get(type);
        return innerMap == null ? null : (A) innerMap.get(key);
    }



    //FUCK FORGE REGISTRIES

    @Unique
    public ForgeRegistryLookup<V> neoBackports$lookup = new ForgeRegistryLookup<>((ForgeRegistry<V>) ((Object) this));

    @Unique
    public boolean neoBackports$hasFakeLookup = false;

    @Definition(id = "hasWrapper", field = "Lnet/minecraftforge/registries/ForgeRegistry;hasWrapper:Z")
    @Expression("this.hasWrapper")
    @ModifyExpressionValue(method = "add(ILnet/minecraft/resources/ResourceLocation;Ljava/lang/Object;Ljava/lang/String;)I", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    boolean add(boolean original) {
        return true;
    }


    @Definition(id = "hasWrapper", field = "Lnet/minecraftforge/registries/ForgeRegistry;hasWrapper:Z")
    @Expression("this.hasWrapper")
    @ModifyExpressionValue(method = "resetDelegates", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    boolean resetDelegates(boolean original){
        return false;
    }

    @WrapMethod(method = "lambda$bindDelegate$6", remap = false)
    private Holder.Reference<V> createFakeLookup(ResourceKey<V> rkey, ResourceLocation k, Operation<Holder.Reference<V>> original){

        Holder.Reference<V> call;

        if (!neoBackports$hasFakeLookup) {
            try {
                call = original.call(rkey, k);
            } catch (IllegalStateException e) {
                NeoBackports.LOGGER.info("Created a fake HolderLookup for the forge registry: {} as it does not have a wrapper", getRegistryKey().location());
                neoBackports$hasFakeLookup = true;
                call = Holder.Reference.createStandAlone(neoBackports$lookup, rkey);
            }
        } else {
            call = Holder.Reference.createStandAlone(neoBackports$lookup, rkey);
        }

        return call;
    }


}
