package net.nikdo53.neobackports.event;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.networking.CustomPacketPayload;
import net.nikdo53.neobackports.io.networking.PayloadRegistrar;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class RegisterPayloadHandlersEvent extends Event implements IModBusEvent {
    public static final Map<String, SimpleChannel> REGISTERED_CHANNELS = new HashMap<>();

    @ApiStatus.Internal
    public RegisterPayloadHandlersEvent() {}

    /**
     * Creates a new {@link PayloadRegistrar}, a utility for registering payloads using a builder-style format.
     *
     * @param version The network version. May not be empty
     */
    public PayloadRegistrar registrar(String version, String namespace) {
        SimpleChannel channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(namespace, "channel"),
                () -> version,
                version::equals,
                version::equals
        );

        REGISTERED_CHANNELS.put(namespace, channel);

        return new PayloadRegistrar(version, channel, namespace);
    }

    /**
     * Registers an existing simple channel
     */
    public PayloadRegistrar registrar(SimpleChannel channel, String namespace) {
        REGISTERED_CHANNELS.put(namespace, channel);

        //the version doesn't actually do anything lol
        return new PayloadRegistrar("1", channel, namespace);
    }

}
