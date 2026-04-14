package net.nikdo53.neobackports.utils;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.nikdo53.neobackports.extensions.IPlayerExtension;

import java.util.function.Consumer;

public interface IMenuTypeExtension {
    /**
     * Ironically, not an extension
     */
    static <T extends AbstractContainerMenu> MenuType<T> create(IContainerFactory<T> factory) {
        return IForgeMenuType.create(factory);
    }
}
