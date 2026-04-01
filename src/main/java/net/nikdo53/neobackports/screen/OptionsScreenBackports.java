package net.nikdo53.neobackports.screen;

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




}
