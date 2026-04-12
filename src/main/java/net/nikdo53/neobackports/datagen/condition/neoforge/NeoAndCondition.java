/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.nikdo53.neobackports.datagen.condition.neoforge;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import java.util.ArrayList;
import java.util.List;

public class NeoAndCondition implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation("neoforge", "and");
    private final ICondition[] children;

    public NeoAndCondition(ICondition... values)
    {
        if (values == null || values.length == 0)
            throw new IllegalArgumentException("Values must not be empty");

        for (ICondition child : values)
        {
            if (child == null)
                throw new IllegalArgumentException("Value must not be null");
        }

        this.children = values;
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test(IContext context)
    {
        for (ICondition child : children)
        {
            if (!child.test(context))
                return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return Joiner.on(" && ").join(children);
    }

    public static class Serializer implements IConditionSerializer<NeoAndCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NeoAndCondition value)
        {
            JsonArray values = new JsonArray();
            for (ICondition child : value.children)
                values.add(CraftingHelper.serialize(child));
            json.add("values", values);
        }

        @Override
        public NeoAndCondition read(JsonObject json)
        {
            List<ICondition> children = new ArrayList<>();
            for (JsonElement j : GsonHelper.getAsJsonArray(json, "values"))
            {
                if (!j.isJsonObject())
                    throw new JsonSyntaxException("And condition values must be an array of JsonObjects");
                children.add(CraftingHelper.getCondition(j.getAsJsonObject()));
            }
            return new NeoAndCondition(children.toArray(new ICondition[children.size()]));
        }

        @Override
        public ResourceLocation getID()
        {
            return NeoAndCondition.NAME;
        }
    }
}
