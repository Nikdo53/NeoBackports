package net.nikdo53.neobackports.utils.recipe;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeIdHolder {
    public static final BiMap<ResourceLocation, Recipe<?>> RECIPE_IDS = HashBiMap.create();

    public static BiMap<Recipe<?>, ResourceLocation> getReversed(){
        return RECIPE_IDS.inverse();
    }
}
