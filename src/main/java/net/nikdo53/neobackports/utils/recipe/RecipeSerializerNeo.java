package net.nikdo53.neobackports.utils.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.utils.recipe.holder.RecipeHolder;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public interface RecipeSerializerNeo<T extends Recipe<?>> extends RecipeSerializer<T> {

    MapCodec<T> codec();

    StreamCodec<T> streamCodec();

    RecipeHolder<? extends Container, ? extends Recipe<? extends Container>> recipeHolderFactory(T recipe, ResourceLocation recipeId);

    @Override
    @SuppressWarnings("unchecked")
    default T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe){
        if (itemCodecWarningEnabled()){
            JsonElement result = serializedRecipe.get("result");
            if (result != null && result.isJsonObject() ){

                JsonElement id = result.getAsJsonObject().get("id");
                if (id != null){
                    NeoBackports.LOGGER.warn("Recipe {} has a result with an 'id' field, probably caused by using Itemstack.CODEC. Please use BackportCodecs.ITEM_STACK_RECIPE codec or change the field to 'item' so it matches 1.20 standards ", recipeId);
                }
            }
        }

        T result;

        try {
            result = codec().codec().parse(JsonOps.INSTANCE, serializedRecipe).getOrThrow(false, NeoBackports.LOGGER::error);
        } catch (Exception e){
            NeoBackports.LOGGER.error("Failed to parse recipe {}: {}", recipeId, e.getMessage());
            throw e;
        }
        var holder = recipeHolderFactory(result, recipeId);

        return (T) holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    default @Nullable T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer){
        T result = streamCodec().decode(buffer);;
        var holder = recipeHolderFactory(result, recipeId);
        return (T) holder;
    }

    @Override
    @SuppressWarnings("unchecked, rawtypes")
    default void toNetwork(FriendlyByteBuf buffer, T recipe){
        if (recipe instanceof RecipeHolder holder){
            streamCodec().encode(buffer, (T) holder.getRecipe());
        } else {
            throw new IllegalStateException("Recipe " + recipe + " is not an instance of RecipeHolder");
        }
    }

    default boolean itemCodecWarningEnabled(){
        return true;
    }
}
