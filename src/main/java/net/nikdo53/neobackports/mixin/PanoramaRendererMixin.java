package net.nikdo53.neobackports.mixin;

import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.nikdo53.neobackports.screen.OptionsScreenBackports;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PanoramaRenderer.class)
public class PanoramaRendererMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    void grabPanorama(CubeMap cubeMap, CallbackInfo ci){
        OptionsScreenBackports.PANORAMA = ((PanoramaRenderer) (Object) this);
    }
}
