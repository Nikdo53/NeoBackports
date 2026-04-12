package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import net.nikdo53.neobackports.screen.OptionsScreenBackports;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

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

        if (BlurShaderLoader.shouldCancelBackground()){
            return true;
        }

        return original.call(left, right);
    }

    @WrapOperation(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V"))
    void cancelTheBlackThingy(GuiGraphics instance, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, Operation<Void> original){
        if (!(((Screen) (Object) this) instanceof AbstractContainerScreen))
            if(OptionsScreenBackports.renderBlurOrPanorama(instance, x1, y1, width, height)) {
                return;
            }
        original.call(instance, x1, y1, x2, y2, colorFrom, colorTo);
    }

    @WrapOperation(method = "renderDirtBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V"))
    private void cancelAllDirtScreens(GuiGraphics guiGraphics, ResourceLocation atlasLocation, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight, Operation<Void> original){

        boolean blurLoadingScreen = true;

        if (((Screen) (Object) this) instanceof ReceivingLevelScreen){
            blurLoadingScreen = OptionsScreenBackports.PANORAMA != null;
        }

        if (BlurShaderLoader.shouldCancelBackground() && blurLoadingScreen) {
            float[] color = RenderSystem.getShaderColor();
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

            OptionsScreenBackports.renderBlurOrPanorama(guiGraphics, x, y, uWidth, vHeight);

            guiGraphics.setColor(color[0], color[1], color[2], color[3]);
            return;
        }

        original.call(guiGraphics, atlasLocation, x, y, blitOffset, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
    }

}
