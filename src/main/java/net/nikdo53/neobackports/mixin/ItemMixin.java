package net.nikdo53.neobackports.mixin;

import net.minecraft.world.item.Item;
import net.nikdo53.neobackports.extensions.ItemPropertiesComponentExtension;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;
import net.nikdo53.neobackports.io.components.DataDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Item.Properties properties, CallbackInfo ci){
        for (DataDefault<?> defaultComponent : ((ItemPropertiesComponentExtension) properties).getDefaultComponents()) {
            DataDefault.registerDefault(((Item)(Object) this), defaultComponent);
        }
    }

}
