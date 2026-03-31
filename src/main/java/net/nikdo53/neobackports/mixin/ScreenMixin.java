package net.nikdo53.neobackports.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import net.nikdo53.neobackports.screen.PartialTickHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Screen.class)
public class ScreenMixin {

    @Shadow
    @Nullable
    protected Minecraft minecraft;

    @Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V", shift =  At.Shift.AFTER))
    void onRenderBackground(GuiGraphics guiGraphics, CallbackInfo ci){
        BlurShaderLoader.INSTANCE.renderBlurredBackground(PartialTickHelper.INSTANCE.getPartialTicks(this.minecraft.level));
    }
}
