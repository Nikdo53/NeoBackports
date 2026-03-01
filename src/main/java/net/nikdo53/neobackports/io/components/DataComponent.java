package net.nikdo53.neobackports.io.components;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.NeoBackports;
import org.jetbrains.annotations.Nullable;

public record DataComponent<T>(String name, Codec<T> codec, boolean deepScan){

    public void setOn(ItemStack stack, T data){
        CompoundTag compoundTag = stack.getOrCreateTag();
        codec.encodeStart(NbtOps.INSTANCE, data).resultOrPartial(NeoBackports.LOGGER::warn).ifPresent(tag -> compoundTag.put(name, tag));
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

        //this might not be very good for performance but its fail proof
        return getOn(stack) != null;
    }

    @Nullable
    public T getOn(ItemStack stack){
        Tag tag = stack.getOrCreateTag().get(name);
        DataResult<Pair<T, Tag>> decode = codec.decode(NbtOps.INSTANCE, tag);

        return decode.result()
                .map(Pair::getFirst)
                .orElse(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataComponent<?> dataComponent){
            return dataComponent.name.equals(name);
        }
        return false;
    }
}
