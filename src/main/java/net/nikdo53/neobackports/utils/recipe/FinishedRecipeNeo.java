package net.nikdo53.neobackports.utils.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.nikdo53.neobackports.NeoBackports;

public interface FinishedRecipeNeo<T extends Recipe<?>> extends FinishedRecipe {
    @Override
    default void serializeRecipeData(JsonObject json){
        JsonElement element = codec().codec().encodeStart(JsonOps.INSTANCE, getInstance()).getOrThrow(false, NeoBackports.LOGGER::error);
        element.getAsJsonObject().entrySet().forEach(entry -> json.add(entry.getKey(), entry.getValue()));
    }

    MapCodec<T> codec();

    T getInstance();
}
