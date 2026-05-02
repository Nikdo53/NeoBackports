package net.nikdo53.neobackports.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import net.nikdo53.neobackports.screen.OptionsScreenBackports;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"))
    public void useNewTexture(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
        RenderSystem.enableBlend();
        original.call(instance, BlurShaderLoader.isEnabled() ? OptionsScreenBackports.INWORLD_HEADER_SEPARATOR : atlasLocation, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);
        RenderSystem.disableBlend();

    }
}
