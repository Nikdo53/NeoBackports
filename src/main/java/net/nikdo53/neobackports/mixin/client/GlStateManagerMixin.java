package net.nikdo53.neobackports.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {
    @Inject(method = "_disableDepthTest", at = @At("TAIL"), remap = false)
    private static void _disableDepthTest(CallbackInfo ci) {
        BlurShaderLoader.GL_DEPTH_STATE = false;
    }

    @Inject(method = "_enableDepthTest", at = @At("TAIL"), remap = false)
    private static void _enableDepthTest(CallbackInfo ci) {
        BlurShaderLoader.GL_DEPTH_STATE = true;
    }

}
