package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.nikdo53.neobackports.utils.recipe.RecipeIdHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientboundUpdateRecipesPacket.class)
public class ClientboundUpdateRecipesPacketMixin {

    @WrapOperation(method = "fromNetwork", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeSerializer;fromNetwork(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/world/item/crafting/Recipe;"))
    private static Recipe<?> fromNetwork(RecipeSerializer<Recipe<?>> instance, ResourceLocation location, FriendlyByteBuf buf, Operation<Recipe<?>> original) {
        Recipe<?> call = original.call(instance, location, buf);
        RecipeIdHolder.register(location, call, true);
        return call;
    }

}
