package net.nikdo53.neobackports.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.nikdo53.neobackports.NeoBackports;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RegistryDataLoader.class)
public class IConditionButNotReallyMixin {

    @WrapOperation(method = "loadRegistryContents", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/crafting/conditions/ICondition;shouldRegisterEntry(Lcom/google/gson/JsonElement;)Z"))
    private static boolean shouldRegisterEntry(JsonElement json, Operation<Boolean> original) {
        boolean resultOriginal = original.call(json);
        if (!(json instanceof JsonObject obj) || !obj.has("neoforge:conditions")){
            return resultOriginal;
        }

        boolean neoConditions = true;
        try {
             neoConditions = CraftingHelper.processConditions(obj, "neoforge:conditions", ICondition.IContext.TAGS_INVALID);
        } catch (Exception e) {
            NeoBackports.LOGGER.debug("Failed to process neoforge:conditions for entry, skipping...", e);
        }
        return resultOriginal && neoConditions;
    }


}
