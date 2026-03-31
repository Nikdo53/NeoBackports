package net.nikdo53.neobackports.screen;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.nikdo53.neobackports.NeoBackports;

import javax.annotation.Nullable;
import java.io.IOException;

public class BlurShaderLoader {
    public static final ResourceLocation BLUR_LOCATION = NeoBackports.loc("shaders/post/new_blur.json");
    public static final BlurShaderLoader INSTANCE = new BlurShaderLoader();

    @Nullable
    public PostChain blurEffect;
    Minecraft minecraft = Minecraft.getInstance();


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

        //TODO: add the setting

        // float f = (float)minecraft.options.getMenuBackgroundBlurriness();
        float f = 5;
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


    public void setUniform(PostChain postChain, String name, float backgroundBlurriness) {
        for (PostPass postpass : postChain.passes) {
            postpass.getEffect().safeGetUniform(name).set(backgroundBlurriness);
        }
    }


}
