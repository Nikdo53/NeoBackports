package net.nikdo53.neobackports.io.networking;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface CustomPacketPayload {
    void handle(Supplier<NetworkEvent.Context> ctx);
}
