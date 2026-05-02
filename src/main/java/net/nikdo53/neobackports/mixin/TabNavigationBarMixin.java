package net.nikdo53.neobackports.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import net.nikdo53.neobackports.screen.OptionsScreenBackports;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TabNavigationBar.class)
public class TabNavigationBarMixin {

    @Shadow
    @Final
    private GridLayout layout;

    @Shadow
    @Final
    private ImmutableList<TabButton> tabButtons;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"))
    public void useNewTexture(GuiGraphics guiGraphics, ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight, Operation<Void> original){
        if (BlurShaderLoader.isEnabled()){
            RenderSystem.enableBlend();

            original.call(guiGraphics, OptionsScreenBackports.INWORLD_HEADER_SEPARATOR, x, y, uOffset, vOffset, this.tabButtons.get(0).getX(), height, textureWidth, textureHeight);
            int i = this.tabButtons.get(this.tabButtons.size() - 1).getRight();
            original.call(guiGraphics, OptionsScreenBackports.INWORLD_HEADER_SEPARATOR, i, y, uOffset, vOffset, width, height, textureWidth, textureHeight);

            RenderSystem.disableBlend();
        } else {
            original.call(guiGraphics, atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
    public void cancelBlackBar(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color, Operation<Void> original){
        if (!BlurShaderLoader.isEnabled()){
            original.call(instance, minX, minY, maxX, maxY, color);
        }
    }

    @Mixin(TabButton.class)
    public static class TabButtonMixin {
        @WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIIIIII)V"))
        public void renderWidget(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, int width, int height, int leftSliceWidth, int topSliceHeight, int rightSliceWidth, int bottomSliceHeight, int uWidth, int vHeight, int textureX, int textureY, Operation<Void> original) {
            boolean enabled = BlurShaderLoader.isEnabled();
            if (enabled) RenderSystem.enableBlend();
            original.call(instance, enabled ? OptionsScreenBackports.TAB_BUTTON_NEO : atlasLocation, x, y, width, height, leftSliceWidth, topSliceHeight, rightSliceWidth, bottomSliceHeight, uWidth, vHeight, textureX, textureY);
            if (enabled) RenderSystem.disableBlend();

        }

    }

}
