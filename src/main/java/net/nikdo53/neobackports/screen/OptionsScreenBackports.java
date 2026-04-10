package net.nikdo53.neobackports.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;


public class OptionsScreenBackports {
    public static final ResourceLocation INWORLD_MENU_LIST_BACKGROUND = new ResourceLocation("textures/gui/inworld_menu_list_background.png");
    public static final ResourceLocation MENU_LIST_BACKGROUND = new ResourceLocation("textures/gui/menu_list_background.png");

    public static final ResourceLocation INWORLD_HEADER_SEPARATOR = new ResourceLocation("textures/gui/inworld_header_separator.png");
    public static final ResourceLocation INWORLD_FOOTER_SEPARATOR = new ResourceLocation("textures/gui/inworld_footer_separator.png");

    public static final ResourceLocation MENU_BACKGROUND = new ResourceLocation("textures/gui/menu_background.png");
    public static final ResourceLocation INWORLD_MENU_BACKGROUND = new ResourceLocation("textures/gui/inworld_menu_background.png");

    @Nullable
    public static PanoramaRenderer PANORAMA = null;


    public static boolean renderBlurOrPanorama(GuiGraphics instance, int x1, int y1, int width, int height) {
        if (BlurShaderLoader.shouldCancelBackground()){
            Minecraft minecraft = Minecraft.getInstance();

            PanoramaRenderer panoramaRenderer = OptionsScreenBackports.PANORAMA;
            if (minecraft.level == null && panoramaRenderer != null) {
                panoramaRenderer.render(minecraft.getDeltaFrameTime(), 1.0f);
            }

            BlurShaderLoader.INSTANCE.renderBlurredBackground(minecraft.getDeltaFrameTime());

            RenderSystem.enableBlend();
            instance.blit(minecraft.level == null ? OptionsScreenBackports.MENU_BACKGROUND : OptionsScreenBackports.INWORLD_MENU_BACKGROUND, x1, y1, 0, 0f, 0f, width, height, 32, 32);
            RenderSystem.disableBlend();

            return true;
        }
        return false;
    }


}
