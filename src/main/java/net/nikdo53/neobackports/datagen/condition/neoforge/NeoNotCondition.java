/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.nikdo53.neobackports.datagen.condition.neoforge;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class NeoNotCondition implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation("neoforge", "not");
    private final ICondition child;

    public NeoNotCondition(ICondition child)
    {
        this.child = child;
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test(IContext context)
    {
        return !child.test(context);
    }

    @Override
    public String toString()
    {
        return "!" + child;
    }

    public static class Serializer implements IConditionSerializer<NeoNotCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NeoNotCondition value)
        {
            json.add("value", CraftingHelper.serialize(value.child));
        }

        @Override
        public NeoNotCondition read(JsonObject json)
        {
            return new NeoNotCondition(CraftingHelper.getCondition(GsonHelper.getAsJsonObject(json, "value")));
        }

        @Override
        public ResourceLocation getID()
        {
            return NeoNotCondition.NAME;
        }
    }
}
