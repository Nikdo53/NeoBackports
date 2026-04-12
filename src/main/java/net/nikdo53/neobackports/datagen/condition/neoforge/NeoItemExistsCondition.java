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
import net.minecraftforge.registries.ForgeRegistries;

public class NeoItemExistsCondition implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation("neoforge", "item_exists");
    private final ResourceLocation item;

    public NeoItemExistsCondition(String location)
    {
        this(new ResourceLocation(location));
    }

    public NeoItemExistsCondition(String namespace, String path)
    {
        this(new ResourceLocation(namespace, path));
    }

    public NeoItemExistsCondition(ResourceLocation item)
    {
        this.item = item;
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test(IContext context)
    {
        return ForgeRegistries.ITEMS.containsKey(item);
    }

    @Override
    public String toString()
    {
        return "item_exists(\"" + item + "\")";
    }

    public static class Serializer implements IConditionSerializer<NeoItemExistsCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NeoItemExistsCondition value)
        {
            json.addProperty("item", value.item.toString());
        }

        @Override
        public NeoItemExistsCondition read(JsonObject json)
        {
            return new NeoItemExistsCondition(new ResourceLocation(GsonHelper.getAsString(json, "item")));
        }

        @Override
        public ResourceLocation getID()
        {
            return NeoItemExistsCondition.NAME;
        }
    }
}
