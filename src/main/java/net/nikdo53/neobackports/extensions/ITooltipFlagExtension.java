package net.nikdo53.neobackports.extensions;

import net.minecraftforge.fml.loading.FMLLoader;

public interface ITooltipFlagExtension {
    /**
     * Neo: Returns the state of the Control key (as reported by Screen) on the client, or {@code false} on the server.
     */
    default boolean hasControlDown() {
        return false;
    }

    /**
     * Neo: Returns the state of the Shift key (as reported by Screen) on the client, or {@code false} on the server.
     */
    default boolean hasShiftDown() {
        return false;
    }

    /**
     * Neo: Returns the state of the Alt key (as reported by Screen) on the client, or {@code false} on the server.
     */
    default boolean hasAltDown() {
        return false;
    }
}
