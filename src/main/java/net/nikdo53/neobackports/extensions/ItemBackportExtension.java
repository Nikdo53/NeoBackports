package net.nikdo53.neobackports.extensions;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.nikdo53.neobackports.utils.TooltipContext;

import java.util.List;

public interface ItemBackportExtension {
    default void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {

    }

}
