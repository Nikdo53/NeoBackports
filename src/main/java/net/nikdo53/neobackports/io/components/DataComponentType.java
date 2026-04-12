package net.nikdo53.neobackports.io.components;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.NeoBackports;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record DataComponentType<T>(String name, Either<Codec<T>, TagCodec<T>> eitherCodec, boolean deepScan){

    public void setOn(ItemStack stack, T data){
        CompoundTag compoundTag = stack.getOrCreateTag();
        if (eitherCodec.left().isPresent()) {
            eitherCodec.left().get().encodeStart(NbtOps.INSTANCE, data).resultOrPartial(NeoBackports.LOGGER::warn).ifPresent(tag -> compoundTag.put(name, tag));
        } else {
            eitherCodec.right().get().encoder().accept(compoundTag, data);
        }
    }

    public void removeFrom(ItemStack stack){
        CompoundTag compoundTag = stack.getOrCreateTag();

        if (compoundTag.contains(name)){
            compoundTag.remove(name);
        }
    }

    public boolean isOn(ItemStack stack){
        CompoundTag tag = stack.getTag();
        if (tag == null) return false;

        if (!tag.contains(name)) return false;
        if (!deepScan) return true;

        //this might not be very good for performance but its fail proof
        return getOn(stack) != null;
    }

    @Nullable
    public T getOn(ItemStack stack){
        if (!stack.hasTag()) return null;
        CompoundTag compoundTag = stack.getTag();

        if (eitherCodec.left().isPresent()) {

            Tag tag = compoundTag.get(name);
            DataResult<Pair<T, Tag>> decode = eitherCodec.left().get().decode(NbtOps.INSTANCE, tag);

            return decode.result()
                    .map(Pair::getFirst)
                    .orElse(null);
        } else {
            return eitherCodec.right().get().decoder().apply(compoundTag);
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataComponentType<?> dataComponent){
            return dataComponent.name.equals(name);
        }
        return false;
    }

    public static <T> Builder<T> builder(){
        return new Builder<>();
    }

    @FunctionalInterface
    public interface TagEncoder<T> extends BiConsumer<CompoundTag, T> {}

    @FunctionalInterface
    public interface TagDecoder<T> extends Function<CompoundTag, T> {}

    public record TagCodec<T>(TagEncoder<T> encoder, TagDecoder<T> decoder) {}

    public static class Builder<T> {
        private String name = null;
        private Either<Codec<T>, TagCodec<T>> codec = null;
        private boolean deepScan = false;

        /**
         * Sets the serialization id of the component.
         */
        public Builder<T> setName(String name){
            this.name = name;
            return this;
        }

        /**
         * Sets the serialization id of the component.
         */
        public Builder<T> setName(ResourceLocation name){
            return setName(name.toString());
        }

        /**
         * Whether the component should check for its whole contents instead of just the name when
         * using the has method. It's worse for performance but prevents conflicts with different data
         */
        public Builder<T> enableDeepScan(){
            deepScan = true;
            return this;
        }

        /**
         * Codec for the data component.
         */
        public Builder<T> persistent(Codec<T> codec){
            this.codec = Either.left(codec);
            return this;
        }

        /**
         * Reader and writer for the CompoundTag directly, useful for compatibility with existing data (like updating a mod from native 1.20.1).
         * Unlike the codec method, it does not use the name for serialization, causing potential conflicts with other mods.
         * @see #persistent(Codec) the codec method which should be used instead
         */
        public Builder<T> persistent(TagEncoder<T> encoder, TagDecoder<T> decoder){
            this.codec = Either.right(new TagCodec<>(encoder, decoder));
            return this;
        }

        public DataComponentType<T> build(){
            if (name == null) throw new IllegalStateException("Tried registering a DataComponent without a name!");
            if (codec == null) throw new IllegalStateException("Tried registering the DataComponent " + name + " without a codec!");

            return new DataComponentType<>(name, codec, deepScan);
        }
    }
}
