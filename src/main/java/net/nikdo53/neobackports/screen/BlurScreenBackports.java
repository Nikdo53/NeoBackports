package net.nikdo53.neobackports.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.NeoBackports;
import org.jetbrains.annotations.Nullable;


public class BlurScreenBackports {
    public static final ResourceLocation INWORLD_MENU_LIST_BACKGROUND = new ResourceLocation("textures/gui/inworld_menu_list_background.png");
    public static final ResourceLocation MENU_LIST_BACKGROUND = new ResourceLocation("textures/gui/menu_list_background.png");

    public static final ResourceLocation INWORLD_HEADER_SEPARATOR = new ResourceLocation("textures/gui/inworld_header_separator.png");
    public static final ResourceLocation INWORLD_FOOTER_SEPARATOR = new ResourceLocation("textures/gui/inworld_footer_separator.png");

    public static final ResourceLocation MENU_BACKGROUND = new ResourceLocation("textures/gui/menu_background.png");
    public static final ResourceLocation INWORLD_MENU_BACKGROUND = new ResourceLocation("textures/gui/inworld_menu_background.png");

    public static final ResourceLocation TAB_BUTTON_NEO = new ResourceLocation("textures/gui/tab_button_neo.png");
    public static final ResourceLocation RESOURCE_PACK_ID = NeoBackports.loc("resourcepacks/blurred_menus");

    @Nullable
    public static PanoramaRenderer PANORAMA = null;

    public static void renderBlurOrPanorama(GuiGraphics instance, int width, int height) {
        renderBlurOrPanorama(instance, 0, 0, width, height);
    }

    public static void renderBlurOrPanorama(GuiGraphics guiGraphics, int x1, int y1, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();

        PanoramaRenderer panoramaRenderer = BlurScreenBackports.PANORAMA;
        if (minecraft.level == null && panoramaRenderer != null) {
            panoramaRenderer.render(minecraft.getDeltaFrameTime(), 1.0f);
        }

        BlurShaderLoader.INSTANCE.renderBlurredBackground(minecraft.getDeltaFrameTime());

        RenderSystem.enableBlend();
        guiGraphics.blit(minecraft.level == null ? BlurScreenBackports.MENU_BACKGROUND : BlurScreenBackports.INWORLD_MENU_BACKGROUND, x1, y1, 0, 0f, 0f, width, height, 32, 32);
        RenderSystem.disableBlend();
    }


    public static boolean hasResourcePack(){
        return Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream().anyMatch(r -> {
            if (r != null) {
               return r.getId().equals("mod/" + RESOURCE_PACK_ID);
            }
            return false;
        });
    }
}
