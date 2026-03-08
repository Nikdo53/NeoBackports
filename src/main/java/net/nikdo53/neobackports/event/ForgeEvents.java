package net.nikdo53.neobackports.event;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.attachment.CapabilityType;
import net.nikdo53.neobackports.io.attachment.DataAttachment;
import net.nikdo53.neobackports.io.attachment.DataAttachmentType;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NeoBackports.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        DataAttachmentType.REGISTERED_DATA_ATTACHMENTS.forEach((key, type) -> {
            if (!type.attachment().isCopyOnDeath()) return;

            LazyOptional<? extends DataAttachment<?>> capNew = newPlayer.getCapability(type.attachment().getCapabilityKey());
            LazyOptional<? extends DataAttachment<?>> capOld = oldPlayer.getCapability(type.attachment().getCapabilityKey());
            if (!capNew.isPresent() || !capOld.isPresent()) return;

            //avoids messing with the generics
            CompoundTag tag = capOld.orElseThrow(IllegalStateException::new).serializeNBT();
            capNew.orElseThrow(IllegalStateException::new).deserializeNBT(tag);

            DataAttachmentRegistry.sync(newPlayer, type);
        });

        oldPlayer.invalidateCaps();
    }


    @SubscribeEvent
    public static void attachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            DataAttachmentType.DATA_ATTACHMENT_TYPES.getOrDefault(CapabilityType.PLAYER, List.of())
                    .forEach(type -> event.addCapability(type.name(), type.attachment()));

        }
        if (event.getObject() instanceof LivingEntity) {
            DataAttachmentType.DATA_ATTACHMENT_TYPES.getOrDefault(CapabilityType.LIVING_ENTITY, List.of())
                    .forEach(type -> event.addCapability(type.name(), type.attachment()));
        } else {
            DataAttachmentType.DATA_ATTACHMENT_TYPES.getOrDefault(CapabilityType.NON_LIVING_ENTITY, List.of())
                    .forEach(type -> event.addCapability(type.name(), type.attachment()));
        }

        DataAttachmentType.DATA_ATTACHMENT_TYPES.getOrDefault(CapabilityType.ENTITY, List.of())
                .forEach(type -> event.addCapability(type.name(), type.attachment()));
    }


    @SubscribeEvent
    public static void attachCapabilitiesChunk(AttachCapabilitiesEvent<LevelChunk> event) {
        DataAttachmentType.DATA_ATTACHMENT_TYPES.getOrDefault(CapabilityType.CHUNK, List.of())
                .forEach(type -> event.addCapability(type.name(), type.attachment()));
    }

    @SubscribeEvent
    public static void attachCapabilitiesBlockEntity(AttachCapabilitiesEvent<BlockEntity> event) {
        DataAttachmentType.DATA_ATTACHMENT_TYPES.getOrDefault(CapabilityType.BLOCK_ENTITY, List.of())
                .forEach(type -> event.addCapability(type.name(), type.attachment()));
    }

    @SubscribeEvent
    public static void attachCapabilitiesLevel(AttachCapabilitiesEvent<Level> event) {
        DataAttachmentType.DATA_ATTACHMENT_TYPES.getOrDefault(CapabilityType.LEVEL, List.of())
                .forEach(type -> event.addCapability(type.name(), type.attachment()));
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        DataAttachmentType.REGISTERED_DATA_ATTACHMENTS.forEach((loc, attachments) -> event.register(attachments.attachment().getClass()));
    }

}
