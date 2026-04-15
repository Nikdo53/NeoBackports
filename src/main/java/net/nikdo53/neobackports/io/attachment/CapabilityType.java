package net.nikdo53.neobackports.io.attachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikdo53.neobackports.io.StreamCodec;

public enum CapabilityType {
    ENTITY,
    BLOCK_ENTITY,
    LEVEL,
    CHUNK,

    ;

    public static final StreamCodec<CapabilityType> STREAM_CODEC = StreamCodec.enumCodec(CapabilityType.class);

    public static CapabilityType fromHolder(ICapabilityProvider holder){
        if (holder instanceof Entity){
            return ENTITY;
        }

        if (holder instanceof BlockEntity){
            return BLOCK_ENTITY;
        }

        if (holder instanceof Level){
            return LEVEL;
        }

        if (holder instanceof LevelChunk){
            return CHUNK;
        }

        throw new RuntimeException("Invalid capability provider capabilityType");
    }

    public AdvancedCapabilityType toAdvanced(){
        switch (this) {
            case ENTITY -> {
                return AdvancedCapabilityType.ENTITY;
            }
            case BLOCK_ENTITY -> {
                return AdvancedCapabilityType.BLOCK_ENTITY;
            }
            case LEVEL -> {
                return AdvancedCapabilityType.LEVEL;
            }
            case CHUNK -> {
                return AdvancedCapabilityType.CHUNK;
            }
        }

        throw new RuntimeException("Invalid CapabilityType");
    }


}
