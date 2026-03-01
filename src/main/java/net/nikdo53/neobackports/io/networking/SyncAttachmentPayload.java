package net.nikdo53.neobackports.io.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.attachment.CapabilityType;
import net.nikdo53.neobackports.io.attachment.DataAttachment;
import net.nikdo53.neobackports.io.attachment.DataAttachmentType;

public record SyncAttachmentPayload(CapabilityType type, long holderId, DataAttachment<?> capability) implements ToClientPacket{

    @SuppressWarnings({"unchecked"})
    public static final StreamCodec<DataAttachment<?>> CAPABILITY_STREAM_CODEC =
            StreamCodec.of((buf, cap) -> {
                DataAttachmentType.STREAM_CODEC_CODEC.encode(buf,  cap);

                ((DataAttachment<Object>)cap).getStreamCodec().encode(buf, cap.getData());

            }, buf -> {
                DataAttachment<Object> attachment = (DataAttachment<Object>) DataAttachmentType.STREAM_CODEC_CODEC.decode(buf);
                Object data = attachment.getStreamCodec().decode(buf);

                attachment.setData(data);

                return attachment;
            });


    public static final StreamCodec<SyncAttachmentPayload> STREAM_CODEC = StreamCodec.composite(
            CapabilityType.STREAM_CODEC, SyncAttachmentPayload::type,
            StreamCodec.LONG, SyncAttachmentPayload::holderId,
            CAPABILITY_STREAM_CODEC, SyncAttachmentPayload::capability,
            SyncAttachmentPayload::new
    );


    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context, Level level, Player player) {
        CapabilityType simpleType = type.getSimple();

        switch (simpleType) {
            case ENTITY -> {
                int id = Math.toIntExact(holderId);
                Entity entity = level.getEntity(id);

                if (entity != null)
                    DataAttachmentRegistry.setFrom(entity, capability);
            }

            case BLOCK_ENTITY -> {
                BlockEntity entity = level.getBlockEntity(BlockPos.of(holderId));

                if (entity != null)
                    DataAttachmentRegistry.setFrom(entity, capability);
            }

            case CHUNK -> {

                LevelChunk chunk = level.getChunkAt(BlockPos.of(holderId));

                DataAttachmentRegistry.setFrom(chunk, capability);
            }

            case LEVEL -> {
                DataAttachmentRegistry.setFrom(level, capability);
            }

        }
    }
}
