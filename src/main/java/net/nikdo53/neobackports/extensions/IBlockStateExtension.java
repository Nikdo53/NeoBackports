package net.nikdo53.neobackports.extensions;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IBlockStateExtension {
    default boolean is(Supplier<Block> supplier) {
        throw new IllegalStateException("not implemented");
    }
}
