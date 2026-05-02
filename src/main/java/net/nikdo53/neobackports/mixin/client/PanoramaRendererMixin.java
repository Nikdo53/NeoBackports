package net.nikdo53.neobackports.mixin.client;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.nikdo53.neobackports.screen.BlurScreenBackports;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PanoramaRenderer.class)
public class PanoramaRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    void grabPanorama(CubeMap cubeMap, CallbackInfo ci){
        BlurScreenBackports.PANORAMA = ((PanoramaRenderer) (Object) this);
    }
}
