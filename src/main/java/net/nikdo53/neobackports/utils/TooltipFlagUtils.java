package net.nikdo53.neobackports.utils;

import net.minecraft.client.gui.screens.Screen;

public interface TooltipFlagUtils {
    static boolean hasControlDown() {
        return Screen.hasControlDown();
    }

    static boolean hasShiftDown() {
        return Screen.hasShiftDown();
    }

    static boolean hasAltDown() {
        return Screen.hasAltDown();
    }

}
