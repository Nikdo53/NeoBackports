/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.nikdo53.neobackports.datagen.condition.neoforge;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class NeoTagEmptyCondition implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation("neoforge", "tag_empty");
    private final TagKey<Item> tag;

    public NeoTagEmptyCondition(String location)
    {
        this(new ResourceLocation(location));
    }

    public NeoTagEmptyCondition(String namespace, String path)
    {
        this(new ResourceLocation(namespace, path));
    }

    public NeoTagEmptyCondition(ResourceLocation tag)
    {
        this.tag = TagKey.create(Registries.ITEM, tag);
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test(IContext context)
    {
        return context.getTag(tag).isEmpty();
    }

    @Override
    public String toString()
    {
        return "tag_empty(\"" + tag.location() + "\")";
    }

    public static class Serializer implements IConditionSerializer<NeoTagEmptyCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NeoTagEmptyCondition value)
        {
            json.addProperty("tag", value.tag.location().toString());
        }

        @Override
        public NeoTagEmptyCondition read(JsonObject json)
        {
            return new NeoTagEmptyCondition(new ResourceLocation(GsonHelper.getAsString(json, "tag")));
        }

        @Override
        public ResourceLocation getID()
        {
            return NeoTagEmptyCondition.NAME;
        }
    }
}
