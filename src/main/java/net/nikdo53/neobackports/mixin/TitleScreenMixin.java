package net.nikdo53.neobackports.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.nikdo53.neobackports.screen.OptionsScreenBackports;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Shadow
    @Final
    private PanoramaRenderer panorama;

    @Inject(method = "init", at = @At("HEAD"))
    void grabPanorama(CallbackInfo ci) {
        OptionsScreenBackports.PANORAMA = panorama;
    }
}
