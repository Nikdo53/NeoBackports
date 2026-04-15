package net.nikdo53.neobackports.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.extensions.ItemBackportExtension;
import net.nikdo53.neobackports.extensions.ItemPropertiesComponentExtension;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;
import net.nikdo53.neobackports.io.components.DataDefault;
import net.nikdo53.neobackports.utils.TooltipContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin implements ItemBackportExtension {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Item.Properties properties, CallbackInfo ci){
        for (DataDefault<?> defaultComponent : ((ItemPropertiesComponentExtension) properties).getDefaultComponents()) {
            DataDefault.registerDefault(((Item)(Object) this), defaultComponent);
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {

    }


    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
        appendHoverText(stack, level == null ? TooltipContext.EMPTY : TooltipContext.of(level), tooltipComponents, isAdvanced);
    }


}
