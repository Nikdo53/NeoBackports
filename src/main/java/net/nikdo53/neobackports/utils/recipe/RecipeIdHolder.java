package net.nikdo53.neobackports.utils.recipe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.nikdo53.neobackports.NeoBackports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeIdHolder {
    private static final BiMap<ResourceLocation, Recipe<?>> RECIPE_IDS = HashBiMap.create();

    private static BiMap<Recipe<?>, ResourceLocation> getReversed(){
        return RECIPE_IDS.inverse();
    }

    public static void register(ResourceLocation id, Recipe<?> recipe, boolean isClient){
        if(NeoBackports.DEBUG_ENABLED)
            NeoBackports.LOGGER.info("Added recipe with id: {} is Client: {}", id, isClient);

        RECIPE_IDS.put(id, recipe);
    }

    public static void clear(boolean isClient) {
        if(NeoBackports.DEBUG_ENABLED)
            NeoBackports.LOGGER.info("Cleared RecipeIdHolder, is Client: {} contained: {}", isClient, RECIPE_IDS.toString());

        RECIPE_IDS.clear();
    }

    public static ResourceLocation getId(Recipe<?> recipe){
        ResourceLocation id = getReversed().get(recipe);
        if (id == null) {
            throw new IllegalStateException("Recipe " + recipe + " does not have an ID registered in RecipeIdHolder!");
        }
        return id;
    }
}
