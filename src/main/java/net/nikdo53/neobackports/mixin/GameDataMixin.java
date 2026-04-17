package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = GameData.class, remap = false)
public class GameDataMixin {

    @Definition(id = "RuntimeException", type = RuntimeException.class)
    @Expression("new RuntimeException()")
    @Inject(method = "postRegisterEvents", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static void postRegisterEvents(CallbackInfo ci, @Local(ordinal = 1) Set<ResourceLocation> ordered){
        ResourceLocation location = NeoForgeRegistries.Keys.DATA_COMPONENT_TYPE.location();

        ArrayList<ResourceLocation> list = new ArrayList<>(ordered);
        ordered.clear();
        ordered.add(location);
        ordered.addAll(list);

    }

}
