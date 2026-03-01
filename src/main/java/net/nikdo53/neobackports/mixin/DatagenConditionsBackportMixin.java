package net.nikdo53.neobackports.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.nikdo53.neobackports.datagen.condition.ConditionalOps;
import net.nikdo53.neobackports.datagen.condition.IDatagenConditionsExtension;
import net.nikdo53.neobackports.datagen.condition.WithConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
@Mixin(RegistriesDatapackGenerator.class)
public class DatagenConditionsBackportMixin implements IDatagenConditionsExtension {
    @Unique
    public Map<ResourceKey<?>, List<ICondition>> neoBackports$conditions;
    @Unique
    private static Map<ResourceKey<?>, List<ICondition>> neoBackports$tempConditions;


    @Inject(method = "run", at = @At("HEAD"))
    private void injectConditionMap(CachedOutput output, CallbackInfoReturnable<CompletableFuture<?>> cir){
        neoBackports$conditions = getConditionsMap();
        neoBackports$tempConditions = neoBackports$conditions;
    }

    @Inject(method = "lambda$dumpRegistryCap$5", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/data/registries/RegistriesDatapackGenerator;dumpValue(Ljava/nio/file/Path;Lnet/minecraft/data/CachedOutput;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Encoder;Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;"), cancellable = true)
    private static <E> void wrapWithConditions(PackOutput.PathProvider packoutput$pathprovider, CachedOutput output, DynamicOps<JsonElement> ops, RegistryDataLoader.RegistryData<E> registryData, Holder.Reference<E> reference,
                                               CallbackInfoReturnable<CompletableFuture<?>> cir){
        if (!neoBackports$tempConditions.isEmpty()) {
            var conditionalCodec = ConditionalOps.createConditionalCodecWithConditions(registryData.elementCodec());
            var conditionalValue = Optional.of(new WithConditions<>(neoBackports$tempConditions.getOrDefault(reference.key(), List.of()), reference.value()));

           cir.setReturnValue(RegistriesDatapackGenerator.dumpValue(packoutput$pathprovider.json(reference.key().location()), output, ops, conditionalCodec,  conditionalValue));
        }
    }


}
