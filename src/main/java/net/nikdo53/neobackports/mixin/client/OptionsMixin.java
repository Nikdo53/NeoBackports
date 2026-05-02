package net.nikdo53.neobackports.mixin.client;

import net.minecraft.client.Options;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {

    @Inject(method = "processOptions", at = @At("TAIL"))
    private void processOptions(Options.FieldAccess accessor, CallbackInfo ci) {
        accessor.process("menuBackgroundBlurriness", BlurShaderLoader.INSTANCE.menuBackgroundBlurriness);
    }

}
