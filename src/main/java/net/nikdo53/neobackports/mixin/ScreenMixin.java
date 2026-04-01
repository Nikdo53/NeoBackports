package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import net.nikdo53.neobackports.screen.OptionsScreenBackports;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(Screen.class)
public abstract class ScreenMixin  {

    @Shadow
    @Nullable
    protected Minecraft minecraft;

    @Shadow
    public int width;

    @Shadow
    public int height;


    @Definition(id = "minecraft", field = "Lnet/minecraft/client/gui/screens/Screen;minecraft:Lnet/minecraft/client/Minecraft;")
    @Definition(id = "level", field = "Lnet/minecraft/client/Minecraft;level:Lnet/minecraft/client/multiplayer/ClientLevel;")
    @Expression("this.minecraft.level != null")
    @WrapOperation(method = "renderBackground", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    boolean renderPanoramaOptionsBackground(Object left, Object right, Operation<Boolean> original){

        if (BlurShaderLoader.isEnabled()){
            return true;
        }

        return original.call(left, right);
    }

    @WrapOperation(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V"))
    void cancelTheBlackThingy(GuiGraphics instance, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, Operation<Void> original){
        if (BlurShaderLoader.isEnabled()){

            PanoramaRenderer panoramaRenderer = OptionsScreenBackports.PANORAMA;
            if (minecraft.level == null && panoramaRenderer != null) {
                panoramaRenderer.render(minecraft.getDeltaFrameTime(), 1.0f);
            }

            BlurShaderLoader.INSTANCE.renderBlurredBackground(minecraft.getDeltaFrameTime());

            RenderSystem.enableBlend();
            instance.blit(this.minecraft.level == null ? OptionsScreenBackports.MENU_BACKGROUND : OptionsScreenBackports.INWORLD_MENU_BACKGROUND, x1, y1, 0, 0f, 0f, width, height, 32, 32);
            RenderSystem.disableBlend();

            return;
        }
        original.call(instance, x1, y1, x2, y2, colorFrom, colorTo);
    }

}
