package net.nikdo53.neobackports.io.components;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.BackportCodecs;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.io.utils.NeoForgeExtraCodecs;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CustomData {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CustomData EMPTY = new CustomData(new CompoundTag());
/* // Ill fix this if its needed later
    public static final Codec<CustomData> CODEC = NeoForgeExtraCodecs.withAlternative(CompoundTag.CODEC, TagParser.AS_CODEC)
            .xmap(CustomData::new, p_331996_ -> p_331996_.tag);
    public static final Codec<CustomData> CODEC_WITH_ID = CODEC.validate(
            p_331848_ -> p_331848_.getUnsafe().contains("id", 8) ? DataResult.success(p_331848_) : DataResult.error(() -> "Missing id for entity in: " + p_331848_)
    );
*/
    @Deprecated
    public static final StreamCodec<CustomData> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(CustomData::new, p_331280_ -> p_331280_.tag);
    private final CompoundTag tag;

    private CustomData(CompoundTag tag) {
        this.tag = tag;
    }

    public static CustomData of(CompoundTag tag) {
        return new CustomData(tag.copy());
    }

    public static Predicate<ItemStack> itemMatcher(DataComponentType<CustomData> componentType, CompoundTag tag) {
        return p_332154_ -> {
            CustomData customdata = p_332154_.getOrDefault(componentType, EMPTY);
            return customdata.matchedBy(tag);
        };
    }

    public boolean matchedBy(CompoundTag tag) {
        return NbtUtils.compareNbt(tag, this.tag, true);
    }

    public static void update(DataComponentType<CustomData> componentType, ItemStack stack, Consumer<CompoundTag> updater) {
        CustomData customdata = stack.getOrDefault(componentType, EMPTY).update(updater);
        if (customdata.tag.isEmpty()) {
            stack.remove(componentType);
        } else {
            stack.set(componentType, customdata);
        }
    }

    public static void set(DataComponentType<CustomData> componentType, ItemStack stack, CompoundTag tag) {
        if (!tag.isEmpty()) {
            stack.set(componentType, of(tag));
        } else {
            stack.remove(componentType);
        }
    }

    public CustomData update(Consumer<CompoundTag> updater) {
        CompoundTag compoundtag = this.tag.copy();
        updater.accept(compoundtag);
        return new CustomData(compoundtag);
    }

    public void loadInto(Entity entity) {
        CompoundTag compoundtag = entity.saveWithoutId(new CompoundTag());
        UUID uuid = entity.getUUID();
        compoundtag.merge(this.tag);
        entity.load(compoundtag);
        entity.setUUID(uuid);
    }

    public <T> DataResult<CustomData> update(DynamicOps<Tag> ops, MapEncoder<T> encoder, T value) {
        return encoder.encode(value, ops, ops.mapBuilder()).build(this.tag).map(p_330397_ -> new CustomData((CompoundTag)p_330397_));
    }

    public <T> DataResult<T> read(MapDecoder<T> decoder) {
        return this.read(NbtOps.INSTANCE, decoder);
    }

    public <T> DataResult<T> read(DynamicOps<Tag> ops, MapDecoder<T> decoder) {
        MapLike<Tag> maplike = ops.getMap(this.tag).getOrThrow(false, LOGGER::error);
        return decoder.decode(ops, maplike);
    }

    public int size() {
        return this.tag.size();
    }

    public boolean isEmpty() {
        return this.tag.isEmpty();
    }

    public CompoundTag copyTag() {
        return this.tag.copy();
    }

    public boolean contains(String key) {
        return this.tag.contains(key);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            return other instanceof CustomData customdata ? this.tag.equals(customdata.tag) : false;
        }
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }

    @Deprecated
    public CompoundTag getUnsafe() {
        return this.tag;
    }
}
