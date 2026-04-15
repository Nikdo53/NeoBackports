package net.nikdo53.neobackports.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import net.nikdo53.neobackports.extensions.ITooltipFlagExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TooltipFlag.class)
public interface TooltipFlagMixin extends ITooltipFlagExtension {
    @Override
    default boolean hasControlDown() {
        return FMLLoader.getDist().isClient() && ClientThingy.hasControlDown();
    }

    @Override
    default boolean hasShiftDown() {
        return FMLLoader.getDist().isClient() && ClientThingy.hasShiftDown();
    }

    @Override
    default boolean hasAltDown() {
        return FMLLoader.getDist().isClient() && ClientThingy.hasAltDown();
    }

    @OnlyIn(Dist.CLIENT)
    interface ClientThingy{
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
}
