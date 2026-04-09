package net.nikdo53.neobackports.io.attachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CapabilityType {
    PLAYER,
    LIVING_ENTITY(PLAYER),
    NON_LIVING_ENTITY,
    ENTITY(LIVING_ENTITY, PLAYER, NON_LIVING_ENTITY),
    BLOCK_ENTITY,
    LEVEL,
    CHUNK,
    ALL(ENTITY, BLOCK_ENTITY, LEVEL, CHUNK),

    ;

    /**
     * For example, all players are entities
     */
    public final List<CapabilityType> subTypes;

    CapabilityType(CapabilityType... subTypes){
        this.subTypes = Arrays.asList(subTypes);
    }

    public static final StreamCodec<CapabilityType> STREAM_CODEC = StreamCodec.enumCodec(CapabilityType.class);

    public static CapabilityType fromHolderSimple(ICapabilityProvider holder){
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

        throw new RuntimeException("Invalid capability provider type");
    }

    public static CapabilityType fromHolderAll(ICapabilityProvider holder){
        if (holder instanceof Player){
            return PLAYER;
        }

        if (holder instanceof LivingEntity){
            return  LIVING_ENTITY;
        } else if ( holder instanceof Entity){
            return NON_LIVING_ENTITY;
        }

        return fromHolderSimple(holder);
    }

    public CapabilityType getSimple(){
        return switch (this) {
            case PLAYER, NON_LIVING_ENTITY, LIVING_ENTITY -> ENTITY;
            default -> this;
        };
    }

    public static boolean checkWithHolder(ICapabilityProvider holder, DataAttachmentType<?> dataAttachmentType){
        CapabilityType holderType = CapabilityType.fromHolderAll(holder);

        if (dataAttachmentType.getAttachment()
                .getPotentialHolders().stream()
                .noneMatch(type -> type.getWithSubtypes().contains(holderType))) {

            NeoBackports.LOGGER.error("Tried getting attachment: {} on holder of type: {} which it isn't attached to", dataAttachmentType.name(),  holderType.name());

            return true;
        }

        return false;
    }


    public List<CapabilityType> getWithSubtypes(){
        var list = new ArrayList<>(this.subTypes);
        list.add(this);

        return list;
    }

    public void validateRepeats(List<CapabilityType> list){
        list.forEach(type -> {
           if (type.subTypes.contains(this)) {
               throw new IllegalArgumentException("CapabilityType: " + type + " already contains type " + this + " as its subtype, dummy");
           }
        });
    }
}
