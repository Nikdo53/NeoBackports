/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.nikdo53.neobackports.datagen.condition.neoforge;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public final class NeoFalseCondition implements ICondition
{
    public static final NeoFalseCondition INSTANCE = new NeoFalseCondition();
    private static final ResourceLocation NAME = new ResourceLocation("neoforge", "false");

    private NeoFalseCondition() {}

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test(IContext condition)
    {
        return false;
    }

    @Override
    public String toString()
    {
        return "false";
    }

    public static class Serializer implements IConditionSerializer<NeoFalseCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NeoFalseCondition value) { }

        @Override
        public NeoFalseCondition read(JsonObject json)
        {
            return NeoFalseCondition.INSTANCE;
        }

        @Override
        public ResourceLocation getID()
        {
            return NeoFalseCondition.NAME;
        }
    }
}
