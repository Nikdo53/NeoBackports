/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.nikdo53.neobackports.datagen.condition.neoforge;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public final class NeoTrueCondition implements ICondition
{
    public static final NeoTrueCondition INSTANCE = new NeoTrueCondition();
    private static final ResourceLocation NAME = new ResourceLocation("neoforge", "true");

    private NeoTrueCondition() {}

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test(IContext context)
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "true";
    }

    public static class Serializer implements IConditionSerializer<NeoTrueCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NeoTrueCondition value) { }

        @Override
        public NeoTrueCondition read(JsonObject json)
        {
            return NeoTrueCondition.INSTANCE;
        }

        @Override
        public ResourceLocation getID()
        {
            return NeoTrueCondition.NAME;
        }
    }
}
