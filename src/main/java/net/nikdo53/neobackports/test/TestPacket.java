package net.nikdo53.neobackports.test;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.IPayloadContext;
import net.nikdo53.neobackports.io.networking.RegistryDataMapSyncPayload;
import net.nikdo53.neobackports.io.networking.ToClientPacket;
import org.jetbrains.annotations.Nullable;

public record TestPacket(String text) implements ToClientPacket {
    public static final Type<TestPacket> TYPE = new Type<>(NeoBackports.loc("registry_data_map_sync"), TestPacket.class);
    public static final StreamCodec<TestPacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.STRING,
            TestPacket::text,
            TestPacket::new
    );

    @Override
    public void handleClient(IPayloadContext context, Level level, Player player) {
        player.displayClientMessage(Component.literal(text), true);
    }

    @Override
    public @Nullable Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
