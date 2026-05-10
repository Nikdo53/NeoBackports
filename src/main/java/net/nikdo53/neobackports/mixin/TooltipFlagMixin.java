package net.nikdo53.neobackports.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import net.nikdo53.neobackports.extensions.ITooltipFlagExtension;
import net.nikdo53.neobackports.utils.TooltipFlagUtils;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TooltipFlag.class)
public interface TooltipFlagMixin extends ITooltipFlagExtension {
    @Override
    default boolean hasControlDown() {
        if (FMLLoader.getDist().isClient()) {
            return TooltipFlagUtils.hasControlDown();
        }
        return false;
    }

    @Override
    default boolean hasShiftDown() {
        if (FMLLoader.getDist().isClient()) {
            return TooltipFlagUtils.hasShiftDown();
        }
        return false;
    }

    @Override
    default boolean hasAltDown() {
        if (FMLLoader.getDist().isClient()) {
            return TooltipFlagUtils.hasAltDown();
        }
        return false;
    }
}
