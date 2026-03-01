package net.nikdo53.neobackports.io.utils;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BackportCodecs {

    interface IngredientCodecs {
        Codec<Ingredient> CODEC = ingredientCodec(true);
        Codec<Ingredient> CODEC_NONEMPTY = ingredientCodec(false);

        private static Codec<Ingredient> ingredientCodec(boolean allowEmpty) {
            Codec<Ingredient.Value[]> codec = Codec.list(INGREDIENT_VALUE).comapFlatMap((p_300810_) -> !allowEmpty && p_300810_.isEmpty() ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") : DataResult.success(p_300810_.toArray(new Ingredient.Value[0])), List::of);
            return Codec.either(codec, INGREDIENT_VALUE).flatComapMap((p_300805_) -> p_300805_.map(values -> new Ingredient(Arrays.stream(values)), (p_300806_) -> new Ingredient(Arrays.stream(new Ingredient.Value[]{p_300806_}))), (p_300808_) -> {
                if (p_300808_.values.length == 1) {
                    return DataResult.success(Either.right(p_300808_.values[0]));
                } else {
                    return p_300808_.values.length == 0 && !allowEmpty ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") : DataResult.success(Either.left(p_300808_.values));
                }
            });
        }
        Codec<Ingredient.TagValue> TAG_VALUE_CODEC = RecordCodecBuilder.create((p_301118_) -> p_301118_.group(TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter((p_301154_) -> p_301154_.tag)).apply(p_301118_, Ingredient.TagValue::new));

        Codec<ItemStack> SIMPLE_ITEM_CODEC = ForgeRegistries.ITEMS.getCodec().xmap(Item::getDefaultInstance, ItemStack::getItem);

        Codec<Ingredient.ItemValue> ITEM_VALUE = RecordCodecBuilder.create((p_330109_) ->
                p_330109_.group(SIMPLE_ITEM_CODEC.fieldOf("item").forGetter((value) -> value.item)).apply(p_330109_, Ingredient.ItemValue::new));

        Codec<Ingredient.Value> INGREDIENT_VALUE = Codec.either(ITEM_VALUE, TAG_VALUE_CODEC).xmap((p_300956_) -> p_300956_.map((p_300932_) -> p_300932_, (p_301313_) -> p_301313_), (p_301304_) -> {
            if (p_301304_ instanceof Ingredient.TagValue ingredient$tagvalue) {
                return Either.right(ingredient$tagvalue);
            } else if (p_301304_ instanceof Ingredient.ItemValue ingredient$itemvalue) {
                return Either.left(ingredient$itemvalue);
            } else {
                throw new UnsupportedOperationException("This is neither an item value nor a tag value.");
            }
        });
    }


    static <K, V> StrictUnboundedMapCodec<K, V> strictUnboundedMap(Codec<K> key, Codec<V> value) {
        return new StrictUnboundedMapCodec<>(key, value);
    }

    record StrictUnboundedMapCodec<K, V>(Codec<K> keyCodec, Codec<V> elementCodec) implements Codec<Map<K, V>>, BaseMapCodec<K, V> {
        @Override
        public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
            ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();

            for (Pair<T, T> pair : input.entries().toList()) {
                DataResult<K> dataresult = this.keyCodec().parse(ops, pair.getFirst());
                DataResult<V> dataresult1 = this.elementCodec().parse(ops, pair.getSecond());
                DataResult<Pair<K, V>> dataresult2 = dataresult.apply2stable(Pair::of, dataresult1);
                Optional<DataResult.PartialResult<Pair<K, V>>> optional = dataresult2.error();
                if (optional.isPresent()) {
                    String s = optional.get().message();
                    return DataResult.error(() -> dataresult.result().isPresent() ? "Map entry '" + dataresult.result().get() + "' : " + s : s);
                }

                if (!dataresult2.result().isPresent()) {
                    return DataResult.error(() -> "Empty or invalid map contents are not allowed");
                }

                Pair<K, V> pair1 = dataresult2.result().get();
                builder.put(pair1.getFirst(), pair1.getSecond());
            }

            Map<K, V> map = builder.build();
            return DataResult.success(map);
        }

        @Override
        public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input)
                    .setLifecycle(Lifecycle.stable())
                    .flatMap(p_301208_ -> this.decode(ops, (MapLike<T>)p_301208_))
                    .map(p_300941_ -> Pair.of((Map<K, V>)p_300941_, input));
        }

        public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T value) {
            return this.encode(input, ops, ops.mapBuilder()).build(value);
        }

        @Override
        public String toString() {
            return "StrictUnboundedMapCodec[" + this.keyCodec + " -> " + this.elementCodec + "]";
        }
    }


}
