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
import java.util.logging.Level;

public class BlurShaderLoader {
    public static final ResourceLocation BLUR_LOCATION = NeoBackports.loc("shaders/post/new_blur.json");
    public static final BlurShaderLoader INSTANCE = new BlurShaderLoader();

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
        this.minecraft.getMainRenderTarget().bindWrite(false);
    }

    public static boolean isEnabled() {
        return INSTANCE.menuBackgroundBlurriness.get() >= 1;
    }

    public static int getMenuBackgroundBlurriness() {
        return INSTANCE.menuBackgroundBlurriness.get();
    }

    public static boolean shouldCancelBackground() {
        return isEnabled()
                && (Minecraft.getInstance().level != null || OptionsScreenBackports.PANORAMA != null)
        ;
    }

    public void setUniform(PostChain postChain, String name, float backgroundBlurriness) {
        for (PostPass postpass : postChain.passes) {
            postpass.getEffect().safeGetUniform(name).set(backgroundBlurriness);
        }
    }


}
