package net.nikdo53.neobackports.io.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;
import net.nikdo53.neobackports.io.StreamCodec;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.nikdo53.neobackports.io.StreamCodec.*;

public interface ByteBufCodecs {
    StreamCodec<Boolean> BOOL = of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
    StreamCodec<Integer> INT = of(FriendlyByteBuf::writeInt, FriendlyByteBuf::readInt);
    StreamCodec<Integer> VAR_INT = of(FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt);

    StreamCodec<Long> LONG = of(FriendlyByteBuf::writeLong, FriendlyByteBuf::readLong);
    StreamCodec<Long> VAR_LONG = of(FriendlyByteBuf::writeVarLong, FriendlyByteBuf::readVarLong);
    StreamCodec<Float> FLOAT = of(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
    StreamCodec<Double> DOUBLE = of(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble);
    StreamCodec<String> STRING = of(FriendlyByteBuf::writeUtf, FriendlyByteBuf::readUtf);
    StreamCodec<String> STRING_UTF8 = STRING;

    StreamCodec<Vector3f> VEC3F = of(FriendlyByteBuf::writeVector3f, FriendlyByteBuf::readVector3f);
    StreamCodec<Quaternionf> QUATERNIONF = of(FriendlyByteBuf::writeQuaternion, FriendlyByteBuf::readQuaternion);

    StreamCodec<GameProfile> GAME_PROFILE = of(FriendlyByteBuf::writeGameProfile, FriendlyByteBuf::readGameProfile);
    StreamCodec<PropertyMap> GAME_PROFILE_PROPERTIES = of(FriendlyByteBuf::writeGameProfileProperties, FriendlyByteBuf::readGameProfileProperties);

    StreamCodec<BlockPos> BLOCK_POS = of(FriendlyByteBuf::writeBlockPos, FriendlyByteBuf::readBlockPos);
    StreamCodec<ChunkPos> CHUNK_POS = of(FriendlyByteBuf::writeChunkPos, FriendlyByteBuf::readChunkPos);
    StreamCodec<ItemStack> ITEM_STACK = of(FriendlyByteBuf::writeItem, FriendlyByteBuf::readItem);

    StreamCodec<CompoundTag> COMPOUND_TAG = of(FriendlyByteBuf::writeNbt, FriendlyByteBuf::readNbt);
    StreamCodec< CompoundTag> TRUSTED_COMPOUND_TAG = of(FriendlyByteBuf::writeNbt, FriendlyByteBuf::readAnySizeNbt);

    StreamCodec<ResourceLocation> RESOURCE_LOCATION = of(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation);

    StreamCodec<Direction> DIRECTION = enumCodec(Direction.class);
    StreamCodec<Ingredient> INGREDIENT = of((friendlyByteBuf, ingredient) -> ingredient.toNetwork(friendlyByteBuf), Ingredient::fromNetwork);
    StreamCodec<UUID> UUID = of(FriendlyByteBuf::writeUUID, FriendlyByteBuf::readUUID);

    StreamCodec<CraftingBookCategory> CRAFTING_BOOK_CATEGORY = enumCodec(CraftingBookCategory.class);

    static <T> StreamCodec<ResourceKey<T>> resourceKeyWithRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return RESOURCE_LOCATION.map(loc -> ResourceKey.create(registryKey, loc), ResourceKey::location);
    }

    static StreamCodec<ResourceKey<? extends Registry<?>>> registryKey() {
        return RESOURCE_LOCATION.map(ResourceKey::createRegistryKey, ResourceKey::location);
    }

    static StreamCodec<ResourceKey<?>> resourceKey() {
        return composite(
                RESOURCE_LOCATION, ResourceKey::registry,
                RESOURCE_LOCATION, ResourceKey::location,
                (registry, location) -> ResourceKey.create(ResourceKey.createRegistryKey(registry), location)
        );
    }

    StreamCodec< Short> SHORT = new StreamCodec<Short>() {
        public Short decode(FriendlyByteBuf p_320513_) {
            return p_320513_.readShort();
        }

        public void encode(FriendlyByteBuf p_320028_, Short p_320388_) {
            p_320028_.writeShort(p_320388_);
        }
    };
    StreamCodec< Integer> UNSIGNED_SHORT = new StreamCodec< Integer>() {
        public Integer decode(FriendlyByteBuf p_320319_) {
            return p_320319_.readUnsignedShort();
        }

        public void encode(FriendlyByteBuf p_320669_, Integer p_320205_) {
            p_320669_.writeShort(p_320205_);
        }
    };
    StreamCodec< byte[]> BYTE_ARRAY = new StreamCodec< byte[]>() {
        public byte[] decode(FriendlyByteBuf buffer) {
            return buffer.readByteArray(buffer.readableBytes());
        }

        public void encode(FriendlyByteBuf buffer, byte[] value) {
            buffer.writeByteArray(value);
        }
    };

    static StreamCodec< byte[]> byteArray(final int maxSize) {
        return new StreamCodec<>() {
            public byte[] decode(FriendlyByteBuf p_319947_) {
                return p_319947_.readByteArray(maxSize);
            }

            public void encode(FriendlyByteBuf p_320370_, byte[] p_331189_) {
                if (p_331189_.length > maxSize) {
                    throw new EncoderException("ByteArray with size " + p_331189_.length + " is bigger than allowed " + maxSize);
                } else {
                    p_320370_.writeByteArray(p_331189_);
                }
            }
        };
    }

    static StreamCodec< CompoundTag> compoundTagCodec(final Supplier<NbtAccounter> accounter) {
        return new StreamCodec<>() {
            public CompoundTag decode(FriendlyByteBuf buf) {
                CompoundTag tag = buf.readNbt(accounter.get());
                if (tag == null) {
                    throw new DecoderException("Expected non-null compound tag");
                } else {
                    return tag;
                }
            }

            public void encode(FriendlyByteBuf buf, CompoundTag value) {
                buf.writeNbt(value);
            }
        };
    }

    static <T> StreamCodec< T> fromCodecTrusted(Codec<T> codec) {
        return fromCodec(codec, () -> NbtAccounter.UNLIMITED);
    }

    static <T> StreamCodec< T> fromCodec(Codec<T> codec) {
        return fromCodec(codec, () -> new NbtAccounter(2097152L));
    }


    static <T> StreamCodec< T> fromCodec(Codec<T> codec, Supplier<NbtAccounter> accounterSupplier) {
        return of((buf, value) -> {
            CompoundTag compoundTag = new CompoundTag();
            NBTCodecHelper.encode(codec, value, compoundTag, "a");
            compoundTagCodec(accounterSupplier).encode(compoundTag, buf);
        }, (buf -> {
            CompoundTag compoundTag = compoundTagCodec(accounterSupplier).decode(buf);
            return NBTCodecHelper.decode(codec, compoundTag, "a");
        }));
    }

    static <V> StreamCodec<Optional<V>> optional(final StreamCodec<V> codec) {
        return new StreamCodec<>() {
            @Override
            public Optional<V> decode(FriendlyByteBuf buf) {
                return buf.readBoolean() ? Optional.of(codec.decode(buf)) : Optional.empty();
            }

            @Override
            public void encode(FriendlyByteBuf buf, Optional<V> value) {
                if (value.isPresent()) {
                    buf.writeBoolean(true);
                    codec.encode(buf, value.get());
                } else {
                    buf.writeBoolean(false);
                }

            }
        };
    }

    static int readCount(FriendlyByteBuf buffer, int maxSize) {
        int i = VAR_INT.decode(buffer);
        if (i > maxSize) {
            throw new DecoderException(i + " elements exceeded max size of: " + maxSize);
        } else {
            return i;
        }
    }

    static void writeCount(FriendlyByteBuf buffer, int count, int maxSize) {
        if (count > maxSize) {
            throw new EncoderException(count + " elements exceeded max size of: " + maxSize);
        } else {
            VAR_INT.encode(buffer, count);
        }
    }

    static <C extends Collection<V>, V> StreamCodec<C> collection(IntFunction<C> factory, StreamCodec<V> codec) {
        return collection(factory, codec, Integer.MAX_VALUE);
    }

    static <C extends Collection<V>, V> StreamCodec<C> collection(
            final IntFunction<C> factory, final StreamCodec<V> codec, final int maxSize
    ) {
        return new StreamCodec<C>() {
            public C decode(FriendlyByteBuf p_324220_) {
                int i = ByteBufCodecs.readCount(p_324220_, maxSize);
                C c = factory.apply(Math.min(i, 65536));

                for (int j = 0; j < i; j++) {
                    c.add(codec.decode(p_324220_));
                }

                return c;
            }

            public void encode(FriendlyByteBuf p_323874_, C p_340813_) {
                ByteBufCodecs.writeCount(p_323874_, p_340813_.size(), maxSize);

                for (V v : p_340813_) {
                    codec.encode(p_323874_, v);
                }
            }
        };
    }

    static <V, C extends Collection<V>> CodecOperation<V, C> collection(IntFunction<C> factory) {
        return p_319785_ -> collection(factory, p_319785_);
    }

    static <V> CodecOperation<V, List<V>> list() {
        return p_320272_ -> collection(ArrayList::new, p_320272_);
    }

    static <V> CodecOperation<V, List<V>> list(int maxSize) {
        return p_329871_ -> collection(ArrayList::new, p_329871_, maxSize);
    }

    static <K, V, M extends Map<K, V>> StreamCodec<M> map(
            IntFunction<? extends M> factory, StreamCodec<K> keyCodec, StreamCodec<V> valueCodec
    ) {
        return map(factory, keyCodec, valueCodec, Integer.MAX_VALUE);
    }

    static <K, V, M extends Map<K, V>> StreamCodec< M> map(
            final IntFunction<? extends M> factory, final StreamCodec<K> keyCodec, final StreamCodec<V> valueCodec, final int maxSize
    ) {
        return new StreamCodec<M>() {
            public void encode(FriendlyByteBuf p_331539_, M p_341314_) {
                ByteBufCodecs.writeCount(p_331539_, p_341314_.size(), maxSize);
                p_341314_.forEach((p_340647_, p_340648_) -> {
                    keyCodec.encode(p_331539_, (K)p_340647_);
                    valueCodec.encode(p_331539_, (V)p_340648_);
                });
            }

            public M decode(FriendlyByteBuf p_331901_) {
                int i = ByteBufCodecs.readCount(p_331901_, maxSize);
                M m = (M)factory.apply(Math.min(i, 65536));

                for (int j = 0; j < i; j++) {
                    K k = keyCodec.decode(p_331901_);
                    V v = valueCodec.decode(p_331901_);
                    m.put(k, v);
                }

                return m;
            }
        };
    }

    static <L, R> StreamCodec<Either<L, R>> either(final StreamCodec<L> leftCodec, final StreamCodec<R> rightCodec) {
        return new StreamCodec<>() {
            public Either<L, R> decode(FriendlyByteBuf p_332082_) {
                return p_332082_.readBoolean() ? Either.left(leftCodec.decode(p_332082_)) : Either.right(rightCodec.decode(p_332082_));
            }

            public void encode(FriendlyByteBuf p_331172_, Either<L, R> p_340944_) {
                p_340944_.ifLeft(p_341317_ -> {
                    p_331172_.writeBoolean(true);
                    leftCodec.encode(p_331172_, (L) p_341317_);
                }).ifRight(p_341155_ -> {
                    p_331172_.writeBoolean(false);
                    rightCodec.encode(p_331172_, (R) p_341155_);
                });
            }
        };
    }

    static <T> StreamCodec< T> idMapper(final IntFunction<T> idLookup, final ToIntFunction<T> idGetter) {
        return new StreamCodec<>() {
            public T decode(FriendlyByteBuf p_340809_) {
                int i = VAR_INT.decode(p_340809_);
                return idLookup.apply(i);
            }

            public void encode(FriendlyByteBuf p_341417_, T p_330257_) {
                int i = idGetter.applyAsInt(p_330257_);
                VAR_INT.encode(p_341417_, i);
            }
        };
    }

    static <T> StreamCodec< T> idMapper(IdMap<T> idMap) {
        return idMapper(idMap::byIdOrThrow, idMap::getId);
    }

    static <OUTPUT> StreamCodec<Holder<OUTPUT>> holderRegistry(ResourceKey<? extends Registry<OUTPUT>> registry){
        return holderRegistry(RegistryManager.ACTIVE.getRegistry(registry));
    }

    static <OUTPUT> StreamCodec<Holder<OUTPUT>> holderRegistry(IForgeRegistry<OUTPUT> registry){
        return new StreamCodec<>() {
            @Override
            public Holder<OUTPUT> decode(FriendlyByteBuf buf) {
                return registry.getDelegateOrThrow(((ForgeRegistry<OUTPUT>) registry).getValue(buf.readInt()));
            }

            @Override
            public void encode(FriendlyByteBuf buf, Holder<OUTPUT> value) {
                buf.writeInt(((ForgeRegistry<OUTPUT>) registry).getID(value.get()));
            }
        };
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

    static <OUTPUT> StreamCodec<OUTPUT> registry(ResourceKey<? extends Registry<OUTPUT>> registry){
        return registry(RegistryManager.ACTIVE.getRegistry(registry));
    }


    static <OUTPUT> StreamCodec<OUTPUT> registry(IForgeRegistry<OUTPUT> registry){
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                return ((IForgeRegistryInternal<OUTPUT>) registry).getValue(buf.readInt());
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                buf.writeInt(((ForgeRegistry<OUTPUT>) registry).getID(value));
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




    static <OUTPUT> StreamCodec<OUTPUT> byNameRegistry(ResourceKey<? extends Registry<OUTPUT>> registry){
        return byNameRegistry(RegistryManager.ACTIVE.getRegistry(registry));
    }

    static <OUTPUT> StreamCodec<OUTPUT> byNameRegistry(IForgeRegistry<OUTPUT> registry){
        return new StreamCodec<>() {
            @Override
            public OUTPUT decode(FriendlyByteBuf buf) {
                return registry.getValue(buf.readResourceLocation());
            }

            @Override
            public void encode(FriendlyByteBuf buf, OUTPUT value) {
                buf.writeResourceLocation(Objects.requireNonNull(registry.getKey(value)));
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
}
