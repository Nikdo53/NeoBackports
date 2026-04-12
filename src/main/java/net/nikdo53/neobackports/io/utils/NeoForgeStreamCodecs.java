package net.nikdo53.neobackports.io.utils;

import com.mojang.datafixers.util.Function7;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.util.Lazy;
import net.nikdo53.neobackports.io.StreamCodec;

import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeStreamCodecs {

    public static final StreamCodec<byte[]> UNBOUNDED_BYTE_ARRAY = new StreamCodec<>() {
        public byte[] decode(FriendlyByteBuf buf) {
            return buf.readByteArray();
        }

        public void encode(FriendlyByteBuf buf, byte[] data) {
            buf.writeByteArray(data);
        }
    };

    public static final StreamCodec<ChunkPos> CHUNK_POS = new StreamCodec<>() {
        @Override
        public ChunkPos decode(FriendlyByteBuf buf) {
            return buf.readChunkPos();
        }

        @Override
        public void encode(FriendlyByteBuf buf, ChunkPos pos) {
            buf.writeChunkPos(pos);
        }
    };

    public static < V> StreamCodec< V> lazy(Supplier<StreamCodec< V>> streamCodecSupplier) {
        return new LazyStreamCodec<>(streamCodecSupplier);
    }

    private static class LazyStreamCodec< V> implements StreamCodec< V> {
        private final Lazy<StreamCodec< V>> delegate;

        public LazyStreamCodec(Supplier<StreamCodec< V>> streamCodecSupplier) {
            delegate = Lazy.of(streamCodecSupplier);
        }

        @Override
        public void encode(FriendlyByteBuf buf, V value) {
            delegate.get().encode(buf, value);
        }

        @Override
        public V decode(FriendlyByteBuf buf) {
            return delegate.get().decode(buf);
        }
    }

    public static <V extends Enum<V>> StreamCodec< V> enumCodec(Class<V> enumClass) {
        return new StreamCodec<>() {
            @Override
            public V decode(FriendlyByteBuf buf) {
                return buf.readEnum(enumClass);
            }

            @Override
            public void encode(FriendlyByteBuf buf, V value) {
                buf.writeEnum(value);
            }
        };
    }

    /**
     * Creates a stream codec to encode and decode a {@link ResourceKey} that identifies a registry.
     */
    public static StreamCodec< ResourceKey<? extends Registry<?>>> registryKey() {
        return new StreamCodec<>() {
            @Override
            public ResourceKey<? extends Registry<?>> decode(FriendlyByteBuf buf) {
                return ResourceKey.createRegistryKey(buf.readResourceLocation());
            }

            @Override
            public void encode(FriendlyByteBuf buf, ResourceKey<? extends Registry<?>> value) {
                buf.writeResourceLocation(value.location());
            }
        };
    }

    /**
     * Similar to {@link StreamCodec#unit(Object)}, but without checks for the value to be encoded.
     */
    public static < V> StreamCodec< V> uncheckedUnit(final V defaultValue) {
        return new StreamCodec<>() {
            @Override
            public V decode(FriendlyByteBuf buf) {
                return defaultValue;
            }

            @Override
            public void encode(FriendlyByteBuf buf, V value) {}
        };
    }

    public static <C, T1, T2, T3, T4, T5, T6, T7> StreamCodec< C> composite(
            final StreamCodec< T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec< T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec< T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec< T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec< T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec< T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec< T7> codec7,
            final Function<C, T7> getter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> p_331335_) {
        return new StreamCodec<>() {
            @Override
            public C decode(FriendlyByteBuf p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                return p_331335_.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(FriendlyByteBuf p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
            }
        };
    }
}
