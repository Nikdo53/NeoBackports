package net.nikdo53.neobackports.io.attachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AdvancedCapabilityType {
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
    public final List<AdvancedCapabilityType> subTypes;

    AdvancedCapabilityType(AdvancedCapabilityType... subTypes){
        this.subTypes = Arrays.asList(subTypes);
    }

    public static final StreamCodec<AdvancedCapabilityType> STREAM_CODEC = StreamCodec.enumCodec(AdvancedCapabilityType.class);

    public static AdvancedCapabilityType fromHolder(ICapabilityProvider holder){
        if (holder instanceof Player){
            return PLAYER;
        }

        if (holder instanceof LivingEntity){
            return  LIVING_ENTITY;
        } else if ( holder instanceof Entity){
            return NON_LIVING_ENTITY;
        }

        return CapabilityType.fromHolder(holder).toAdvanced();
    }

    public static boolean checkWithHolder(ICapabilityProvider holder, AttachmentType<?> attachmentType){
        AdvancedCapabilityType holderType = AdvancedCapabilityType.fromHolder(holder);

        if (attachmentType.castAttachment()
                .getPotentialHolders().stream()
                .noneMatch(type -> type.getWithSubtypes().contains(holderType))) {

            NeoBackports.LOGGER.error("Tried getting attachment: {} on holder of type: {} which it isn't attached to", attachmentType.id(),  holderType.name());

            return true;
        }

        return false;
    }


    public List<AdvancedCapabilityType> getWithSubtypes(){
        var list = new ArrayList<>(this.subTypes);
        list.add(this);

        return list;
    }

    public void validateRepeats(List<AdvancedCapabilityType> list){
        list.forEach(type -> {
           if (type.subTypes.contains(this)) {
               throw new IllegalArgumentException("CapabilityType: " + type + " already contains type " + this + " as its subtype, dummy");
           }
        });
    }
}
