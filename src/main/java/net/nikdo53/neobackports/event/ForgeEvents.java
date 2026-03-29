package net.nikdo53.neobackports.event;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.loot.LootModifierManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.DataPackRegistriesHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.attachment.CapabilityType;
import net.nikdo53.neobackports.io.attachment.DataAttachment;
import net.nikdo53.neobackports.io.attachment.DataAttachmentType;
import net.nikdo53.neobackports.io.networking.NBNetworking;
import net.nikdo53.neobackports.io.networking.RegistryDataMapSyncPayload;
import net.nikdo53.neobackports.mixin.DataPackRegistriesHooksAccessor;
import net.nikdo53.neobackports.registry.datamaps.DataMapLoader;
import net.nikdo53.neobackports.registry.datamaps.DataMapsManager;
import net.nikdo53.neobackports.registry.datamaps.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.test.NBDataMaps;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = NeoBackports.MOD_ID)
public class ForgeEvents {
    private static DataMapLoader DATA_MAPS;

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        DATA_MAPS = new DataMapLoader(event.getConditionContext(), event.getRegistryAccess());
        event.addListener(DATA_MAPS);
    }

    @SubscribeEvent
    public static void tagsUpdated(TagsUpdatedEvent event) {
        if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
            DATA_MAPS.apply();
        }
    }

    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(NBDataMaps.TEST_DATA_MAP);
    }

    @SubscribeEvent
    public static void onDpSync(final OnDatapackSyncEvent event) {
        DataMapsManager.getDataMaps().forEach((registry, values) -> {
            final var regOpt = RegistryManager.ACTIVE.getRegistry((ResourceKey) registry);
            if (regOpt.isEmpty()) return;

            Stream<ServerPlayer> relevantPlayers = event.getPlayer() == null ? event.getPlayerList().getPlayers().stream() : Stream.of(event.getPlayer());
            relevantPlayers.forEach(player -> {

                // Note: don't send data maps over in-memory connections for normal registries, else the client-side handling will wipe non-synced data maps.
                // Sending them for synced datapack registries is fine and required as those registries are recreated on the client
                if (player.connection.connection.isMemoryConnection() && getSyncedRegistry((ResourceKey) registry) == null) {
                    return;
                }
                final var playerMaps = player.connection.connection.channel().attr(DataMapsManager.ATTRIBUTE_KNOWN_DATA_MAPS).get();
                if (playerMaps == null) return; // Skip gametest players for instance
                handleSync(player, regOpt, playerMaps.getOrDefault(registry, List.of()));
            });
        });
    }

    @Nullable
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static <T> RegistrySynchronization.NetworkedRegistryData<T> getSyncedRegistry(final ResourceKey<? extends Registry<T>> registry) {
        return (RegistrySynchronization.NetworkedRegistryData<T>) DataPackRegistriesHooksAccessor.getNetworkableRegistries().get(registry);
    }

    private static <T> void handleSync(ServerPlayer player, IForgeRegistry<T> registry, Collection<ResourceLocation> attachments) {
        if (attachments.isEmpty()) return;
        final Map<ResourceLocation, Map<ResourceKey<T>, ?>> att = new HashMap<>();
        attachments.forEach(key -> {
            final var attach = DataMapsManager.getDataMap(registry.getRegistryKey(), key);
            if (attach == null || attach.networkCodec() == null) return;
            att.put(key, registry.getDataMap(attach));
        });
        if (!att.isEmpty()) {
            NBNetworking.sendToPlayer(player, new RegistryDataMapSyncPayload<>(registry.getRegistryKey(), att));
        }
    }

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
