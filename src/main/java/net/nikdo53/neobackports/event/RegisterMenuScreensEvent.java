package net.nikdo53.neobackports.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

public class RegisterMenuScreensEvent extends Event implements IModBusEvent {
    @ApiStatus.Internal
    public RegisterMenuScreensEvent() {
    }

    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(
            MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor) {

        MenuScreens.register(menuType, screenConstructor);
    }
}
