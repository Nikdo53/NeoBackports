package net.nikdo53.neobackports.io.networking;

import net.minecraft.network.ConnectionProtocol;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import net.nikdo53.neobackports.io.StreamCodec;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PayloadRegistrar {
    private final String version;
    private boolean optional = false;
    SimpleChannel channel;
    int id = 0;
    String namespace;

    public PayloadRegistrar(String version, SimpleChannel channel, String namespace) {
        this.version = version;
        this.channel = channel;
        this.namespace = namespace;
    }

    private PayloadRegistrar(PayloadRegistrar source) {
        this.version = source.version;
        this.optional = source.optional;
        this.channel = source.channel;
        this.id = source.id;
        this.namespace = source.namespace;
    }

    /**
     * Registers a client-bound payload for the play phase.
     */
    public <T extends CustomPacketPayload> PayloadRegistrar playToClient(CustomPacketPayload.Type<T> type, StreamCodec<T> reader, IPayloadHandler<T> handler) {
        register(type, reader, handler, List.of(ConnectionProtocol.PLAY), Optional.of(NetworkDirection.PLAY_TO_CLIENT), version, optional);
        return this;
    }

    /**
     * Registers a server-bound payload for the play phase.
     */
    public <T extends CustomPacketPayload> PayloadRegistrar playToServer(CustomPacketPayload.Type<T> type, StreamCodec<T> reader, IPayloadHandler<T> handler) {
        register(type, reader, handler, List.of(ConnectionProtocol.PLAY), Optional.of(NetworkDirection.PLAY_TO_SERVER), version, optional);
        return this;
    }

    /**
     * Registers a bidirectional payload for the play phase.
     * <p>
     */
    public <T extends CustomPacketPayload> PayloadRegistrar playBidirectional(CustomPacketPayload.Type<T> type, StreamCodec<T> reader, IPayloadHandler<T> handler) {
        register(type, reader, handler, List.of(ConnectionProtocol.PLAY), Optional.empty(), version, optional);
        return this;
    }

    private <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<T> codec, IPayloadHandler<T> handler, List<ConnectionProtocol> protocols, Optional<NetworkDirection> flow, String version, boolean optional) {
        if (!type.id().getNamespace().equals(namespace)) {
            throw new IllegalArgumentException("Payload capabilityType " + type.id() + " does not belong to the same namespace as the channel (" + namespace + ")");
        }
        channel.registerMessage(id++, type.type(), codec::encode, codec::decode, handler::handle, flow);
    }

    @FunctionalInterface
    public interface IPayloadHandler<T extends CustomPacketPayload> {
        void handle(T payload, Supplier<NetworkEvent.Context> context);
    }
}
