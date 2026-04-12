package net.nikdo53.neobackports.extensions;

import net.minecraft.network.chat.MutableComponent;

public interface IMutableComponentExtension {
    default MutableComponent withColor(int color) {
        throw new IllegalStateException("Not implemented");
    }
}
