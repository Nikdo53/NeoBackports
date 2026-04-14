package net.nikdo53.neobackports.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.network.NetworkHooks;

import java.util.OptionalInt;
import java.util.function.Consumer;

public interface IPlayerExtension {


    default void openMenu(MenuProvider menuProvider, BlockPos pos) {
        throw new RuntimeException("Not implemented");
    }


    default void openMenu(MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
        throw new RuntimeException("Not implemented");
    }
}
