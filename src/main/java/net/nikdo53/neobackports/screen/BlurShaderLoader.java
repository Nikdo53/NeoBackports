package net.nikdo53.neobackports.screen;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.nikdo53.neobackports.NeoBackports;

import javax.annotation.Nullable;
import java.io.IOException;

public class BlurShaderLoader {
    public static final ResourceLocation BLUR_LOCATION = NeoBackports.loc("shaders/post/new_blur.json");
    public static final BlurShaderLoader INSTANCE = new BlurShaderLoader();
    public static boolean GL_DEPTH_STATE = true;

    @Nullable
    public PostChain blurEffect;
    Minecraft minecraft = Minecraft.getInstance();

    public final OptionInstance<Integer> menuBackgroundBlurriness = new OptionInstance<>("options.accessibility.menu_background_blurriness",
            OptionInstance.cachedConstantTooltip(Component.translatable("options.accessibility.menu_background_blurriness.tooltip")),
            (component, value) -> value == 0 ? Options.genericValueLabel(component, CommonComponents.OPTION_OFF) : Options.genericValueLabel(component, value),
            new OptionInstance.IntRange(0, 10), 5,
            (p_268254_) -> {
    });

    public void loadBlurEffect(ResourceProvider resourceProvider) {
        if (this.blurEffect != null) {
            this.blurEffect.close();
        }

        try {
            this.blurEffect = new PostChain(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getMainRenderTarget(), BLUR_LOCATION);
            this.blurEffect.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
        } catch (IOException ioexception) {
            NeoBackports.LOGGER.warn("Failed to load shader: {}", BLUR_LOCATION, ioexception);
        } catch (JsonSyntaxException jsonsyntaxexception) {
            NeoBackports.LOGGER.warn("Failed to parse shader: {}", BLUR_LOCATION, jsonsyntaxexception);
        }
    }

    protected void processBlurEffect(float partialTick) {
        float f = menuBackgroundBlurriness.get();
        if (this.blurEffect != null && f >= 1.0F) {
            setUniform(blurEffect ,"Radius", f);
            this.blurEffect.process(partialTick);
        }
    }

    public void renderBlurredBackground(float partialTick) {
        // Neo: fix blur effect rendered at high z with depth test breaking subsequent rendering of screen elements (https://github.com/neoforged/NeoForge/issues/1504)
        RenderSystem.disableDepthTest();
        processBlurEffect(partialTick);
        this.minecraft.getMainRenderTarget().bindWrite(true);
    }

    public static boolean isEnabled() {
        return INSTANCE.menuBackgroundBlurriness.get() >= 1;
    }

    public static int getMenuBackgroundBlurriness() {
        return INSTANCE.menuBackgroundBlurriness.get();
    }

    public static boolean shouldCancelBackground() {
        return shouldCancelBackground(false);
    }

    public static boolean shouldCancelBackground(boolean isInGame) {
        boolean resourcePack = BlurScreenBackports.hasResourcePack();
        boolean enabled = isEnabled();
        return (enabled || (BlurScreenBackports.PANORAMA != null && resourcePack)) // Makes sure you can use the RP without the blur
                && (Minecraft.getInstance().level != null || BlurScreenBackports.PANORAMA != null) // Cancels the in-between-loading screen
                && ((resourcePack || isInGame) && enabled)// Main menu only works with the RP
        ;
    }


    public void setUniform(PostChain postChain, String name, float backgroundBlurriness) {
        for (PostPass postpass : postChain.passes) {
            postpass.getEffect().safeGetUniform(name).set(backgroundBlurriness);
        }
    }


}
