package net.nikdo53.neobackports.io;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.*;

public interface StreamCodec<A> {
    StreamCodec<Boolean> BOOL = of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
    StreamCodec<Integer> INT = of(FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt);
    StreamCodec<Vector3f> VEC3F = of(FriendlyByteBuf::writeVector3f, FriendlyByteBuf::readVector3f);
    StreamCodec<BlockPos> BLOCK_POS = of(FriendlyByteBuf::writeBlockPos, FriendlyByteBuf::readBlockPos);
    StreamCodec<ChunkPos> CHUNK_POS = of(FriendlyByteBuf::writeChunkPos, FriendlyByteBuf::readChunkPos);
    StreamCodec<ItemStack> ITEM_STACK = of(FriendlyByteBuf::writeItem, FriendlyByteBuf::readItem);
    StreamCodec<Float> FLOAT = of(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
    StreamCodec<Double> DOUBLE = of(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble);
    StreamCodec<Long> LONG = of(FriendlyByteBuf::writeLong, FriendlyByteBuf::readLong);

    StreamCodec<CompoundTag> NBT = of(FriendlyByteBuf::writeNbt, FriendlyByteBuf::readNbt);
    StreamCodec<ResourceLocation> RESOURCE_LOCATION = of(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation);

    StreamCodec<Direction> DIRECTION = enumCodec(Direction.class);
    StreamCodec<String> STRING = of(FriendlyByteBuf::writeUtf, FriendlyByteBuf::readUtf);
    StreamCodec<Ingredient> INGREDIENT = StreamCodec.of((friendlyByteBuf, ingredient) -> ingredient.toNetwork(friendlyByteBuf), Ingredient::fromNetwork);
    StreamCodec<UUID> UUID = STRING.remap(java.util.UUID::fromString, java.util.UUID::toString);
    StreamCodec<CraftingBookCategory> CRAFTING_BOOK_CATEGORY = enumCodec(CraftingBookCategory.class);

     A decode(FriendlyByteBuf buf);
     void encode(FriendlyByteBuf buf, A value);

     default void encode(A value, FriendlyByteBuf buf) {
        encode(buf, value);
    }

    default <O> StreamCodec<O> apply(StreamCodec.CodecOperation<A, O> operation) {
        return operation.apply(this);
    }

    static <OUTPUT> StreamCodec<Holder<OUTPUT>> holderRegistry(Registry<OUTPUT> registry){
        return new StreamCodec<>() {
            @Override
            public Holder<OUTPUT> decode(FriendlyByteBuf buf) {
                return registry.getHolder(buf.readInt()).get();
            }

            @Override
            public void encode(FriendlyByteBuf buf, Holder<OUTPUT> value) {
                buf.writeInt(registry.getId(value.get()));
            }
        };
    }

    static <OUTPUT> StreamCodec<OUTPUT> registry(Registry<OUTPUT> registry){
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                return registry.getHolder(buf.readInt()).get().get();
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                buf.writeInt(registry.getId(value));
            }
        };
    }

    static <OUTPUT> StreamCodec<OUTPUT> byNameRegistry(Registry<OUTPUT> registry){
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                return registry.get(buf.readResourceLocation());
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                buf.writeResourceLocation(Objects.requireNonNull(registry.getKey(value)));
            }
        };
    }



    default StreamCodec<Supplier<A>> toSupplier(){
        return this.remap(StreamCodec::toSupplier, Supplier::get);
    }

    private static <T> Supplier<T> toSupplier(T data){
        return () -> data;
    }

    static <OUTPUT extends Enum<OUTPUT>> StreamCodec<OUTPUT> enumCodec(Class<OUTPUT> enumClass){
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                return buf.readEnum(enumClass);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                buf.writeEnum(value);
            }
        };
    }


    default<REMAP> StreamCodec<REMAP> remap(Function<A, REMAP> fromData, Function<REMAP, A> toData){
        StreamCodec<A> codec = this;
        return new StreamCodec<>() {
            @Override
            public REMAP decode(FriendlyByteBuf buf) {
                return fromData.apply(codec.decode(buf));
            }

            @Override
            public void encode(FriendlyByteBuf buf, REMAP value) {
                codec.encode(buf, toData.apply(value));
            }
        };
    }


    default<LIST extends Collection<A>> StreamCodec<LIST> simpleCollection(IntFunction<LIST> function) {
        return simpleCollection(function, this);
    }

    default StreamCodec<NonNullList<A>> nonNullList(A defaultValue) {
        return simpleCollection((size) -> NonNullList.withSize(size, defaultValue), this);
    }


    default StreamCodec<List<A>> list() {
        return simpleCollection(ArrayList::new, this);
    }

    static <LIST extends Collection<V0>, V0> StreamCodec<LIST> simpleCollection(IntFunction<LIST> function, StreamCodec<V0> codec){
        return new StreamCodec<>() {
            @Override
            public LIST decode(FriendlyByteBuf buf) {
                int size = buf.readInt();
                LIST list = function.apply(size);

                for (int i = 0; i < size; i++) {
                    list.add(codec.decode(buf));
                }

                return list;
            }

            @Override
            public void encode(FriendlyByteBuf buf, LIST value) {
                buf.writeInt(value.size());

                for (V0 subValue : value) {
                    codec.encode(buf, subValue);
                }
            }
        };
    }



     static <MAP extends Map<KEY, VALUE>, KEY, VALUE> StreamCodec<MAP> map(IntFunction<MAP> factory, StreamCodec<KEY> keyCodec, StreamCodec<VALUE> valueCodec){
        return new StreamCodec<>() {
            @Override
            public MAP decode(FriendlyByteBuf buf) {
                int size = buf.readInt();
                MAP map = factory.apply(size);

                for (int i = 0; i < size; i++) {
                    KEY key = keyCodec.decode(buf);
                    VALUE value = valueCodec.decode(buf);
                    map.put(key, value);
                }

                return map;
            }

            @Override
            public void encode(FriendlyByteBuf buf, MAP value) {
                buf.writeInt(value.size());
                value.forEach((key, v) ->{
                    keyCodec.encode(buf, key);
                    valueCodec.encode(buf, v);
                });
            }
        };
    }


     static<A> StreamCodec<A> of(BiConsumer<FriendlyByteBuf, A> encoder, Function<FriendlyByteBuf, A> decoder) {
        return new StreamCodec<>() {
            @Override
            public A decode(FriendlyByteBuf buf) {
                return decoder.apply(buf);
            }

            @Override
            public void encode(FriendlyByteBuf buf, A value) {
                encoder.accept(buf, value);
            }
        };
    }

     static<OUTPUT, V0> StreamCodec<OUTPUT> composite(StreamCodec<V0> codec, Function<OUTPUT, V0> getter, Function<V0, OUTPUT> factory) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec.decode(buf);
                return factory.apply(v0);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec.encode(buf, getter.apply(value));
            }
        };
    }

     static<OUTPUT, V0, V1> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            BiFunction<V0, V1, OUTPUT> factory) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                return factory.apply(v0, v1);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
            }
        };
    }

     static<OUTPUT, V0, V1, V2> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            StreamCodec<V2> codec2, Function<OUTPUT, V2> getter2,

            Function3<V0, V1, V2, OUTPUT> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                V2 v2 = codec2.decode(buf);
                return factory.apply(v0, v1, v2);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
            }
        };
    }

     static<OUTPUT, V0, V1, V2, V3> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            StreamCodec<V2> codec2, Function<OUTPUT, V2> getter2,
            StreamCodec<V3> codec3, Function<OUTPUT, V3> getter3,

            Function4<V0, V1, V2, V3, OUTPUT> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                V2 v2 = codec2.decode(buf);
                V3 v3 = codec3.decode(buf);

                return factory.apply(v0, v1, v2, v3);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
                codec3.encode(buf, getter3.apply(value));

            }
        };
    }

     static<OUTPUT, V0, V1, V2, V3, V4> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            StreamCodec<V2> codec2, Function<OUTPUT, V2> getter2,
            StreamCodec<V3> codec3, Function<OUTPUT, V3> getter3,
            StreamCodec<V4> codec4, Function<OUTPUT, V4> getter4,

            Function5<V0, V1, V2, V3, V4, OUTPUT> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                V2 v2 = codec2.decode(buf);
                V3 v3 = codec3.decode(buf);
                V4 v4 = codec4.decode(buf);

                return factory.apply(v0, v1, v2, v3, v4);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
                codec3.encode(buf, getter3.apply(value));
                codec4.encode(buf, getter4.apply(value));

            }
        };
    }

     static<OUTPUT, V0, V1, V2, V3, V4, V5> StreamCodec<OUTPUT> composite(
            StreamCodec<V0> codec0, Function<OUTPUT, V0> getter0,
            StreamCodec<V1> codec1, Function<OUTPUT, V1> getter1,
            StreamCodec<V2> codec2, Function<OUTPUT, V2> getter2,
            StreamCodec<V3> codec3, Function<OUTPUT, V3> getter3,
            StreamCodec<V4> codec4, Function<OUTPUT, V4> getter4,
            StreamCodec<V5> codec5, Function<OUTPUT, V5> getter5,

            Function6<V0, V1, V2, V3, V4, V5, OUTPUT> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                V0 v0 = codec0.decode(buf);
                V1 v1 = codec1.decode(buf);
                V2 v2 = codec2.decode(buf);
                V3 v3 = codec3.decode(buf);
                V4 v4 = codec4.decode(buf);
                V5 v5 = codec5.decode(buf);

                return factory.apply(v0, v1, v2, v3, v4, v5);
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                codec0.encode(buf, getter0.apply(value));
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
                codec3.encode(buf, getter3.apply(value));
                codec4.encode(buf, getter4.apply(value));
                codec5.encode(buf, getter5.apply(value));
            }
        };
    }

    @FunctionalInterface
    interface CodecOperation<S, T> {
        StreamCodec<T> apply(StreamCodec<S> codec);
    }
}
