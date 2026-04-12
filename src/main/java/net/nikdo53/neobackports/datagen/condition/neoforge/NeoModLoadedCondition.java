/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.nikdo53.neobackports.datagen.condition.neoforge;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.fml.ModList;

public class NeoModLoadedCondition implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation("neoforge", "mod_loaded");
    private final String modid;

    public NeoModLoadedCondition(String modid)
    {
        this.modid = modid;
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test(IContext context)
    {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public String toString()
    {
        return "mod_loaded(\"" + modid + "\")";
    }

    public static class Serializer implements IConditionSerializer<NeoModLoadedCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NeoModLoadedCondition value)
        {
            json.addProperty("modid", value.modid);
        }

        @Override
        public NeoModLoadedCondition read(JsonObject json)
        {
            return new NeoModLoadedCondition(GsonHelper.getAsString(json, "modid"));
        }

        @Override
        public ResourceLocation getID()
        {
            return NeoModLoadedCondition.NAME;
        }
    }
}
