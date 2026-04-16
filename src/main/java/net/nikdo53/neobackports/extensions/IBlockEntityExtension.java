package net.nikdo53.neobackports.extensions;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface IBlockEntityExtension {
    default void saveAdditional(CompoundTag tag, HolderLookup.Provider registries){

    }
    default void loadAdditional(CompoundTag tag, HolderLookup.Provider registries){

    }

    default CompoundTag getUpdateTag(HolderLookup.Provider registries){
        return new CompoundTag();
    }
}
