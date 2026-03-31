package net.nikdo53.neobackports.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {


    @Inject(method = "resize", at = @At("TAIL"))
    public void resize(int width, int height, CallbackInfo ci) {
        if (BlurShaderLoader.INSTANCE.blurEffect != null) {
            BlurShaderLoader.INSTANCE.blurEffect.resize(width, height);
        }
    }

    @Inject(method = "close", at = @At("TAIL"))
    public void close(CallbackInfo ci) {
        if (BlurShaderLoader.INSTANCE.blurEffect != null) {
            BlurShaderLoader.INSTANCE.blurEffect.close();
        }
    }

}
