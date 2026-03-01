package net.nikdo53.neobackports.datagen.condition;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.nikdo53.neobackports.mixin.CraftingHelperAccessor;

import java.util.List;

public class ConditionCodecs {
    public static final Codec<ICondition> CODEC = new Codec<>() {

        @Override
        public <T> DataResult<Pair<ICondition, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).flatMap(map -> {
                T typeElement = map.get(ops.createString("type"));
                if (typeElement == null) {
                    return DataResult.error(() -> "Missing 'type' field for Forge ICondition");
                }

                return ops.getStringValue(typeElement).flatMap(typeStr -> {
                    ResourceLocation id = new ResourceLocation(typeStr);
                    IConditionSerializer<?> serializer = CraftingHelperAccessor.getConditions().get(id);

                    if (serializer == null) {
                        return DataResult.error(() -> "Unknown Forge condition type: " + id);
                    }

                    try {
                        JsonObject json = convertToJson(ops, input);
                        ICondition condition = serializer.read(json);
                        return DataResult.success(Pair.of(condition, ops.empty()));
                    } catch (Exception e) {
                        return DataResult.error(e::getMessage);
                    }
                });
            });
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> DataResult<T> encode(ICondition input, DynamicOps<T> ops, T prefix) {
            IConditionSerializer<ICondition> serializer = (IConditionSerializer<ICondition>) CraftingHelperAccessor.getConditions().get(input.getID());
            ResourceLocation id = serializer.getID();

            JsonObject json = new JsonObject();
            json.addProperty("type", id.toString());
            serializer.write(json, input);

            return DataResult.success(convertFromJson(ops, json));
        }
    };

    public static final Codec<List<ICondition>> LIST_CODEC = CODEC.listOf();


    private static <T> JsonObject convertToJson(DynamicOps<T> ops, T input) {
        return (JsonObject) ops.convertTo(JsonOps.INSTANCE, input);
    }

    private static <T> T convertFromJson(DynamicOps<T> ops, JsonObject json) {
        return JsonOps.INSTANCE.convertTo(ops, json);
    }
}
