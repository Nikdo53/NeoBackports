package net.nikdo53.neobackports.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.resources.ResourceLocation;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static net.nikdo53.neobackports.screen.OptionsScreenBackports.*;

@Mixin(AbstractSelectionList.class)
public abstract class AbstractSelectionListMixin {

    @Shadow
    protected int x0;

    @Shadow
    protected int y0;

    @Shadow
    protected int x1;

    @Shadow
    protected int y1;

    @Shadow
    protected abstract void enableScissor(GuiGraphics guiGraphics);

    @Shadow
    public abstract double getScrollAmount();

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    public abstract int getBottom();

    @Shadow
    public abstract int getWidth();

    @Definition(id = "renderTopAndBottom", field = "Lnet/minecraft/client/gui/components/AbstractSelectionList;renderTopAndBottom:Z")
    @Expression("this.renderTopAndBottom")
    @WrapOperation(method = "render", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    public boolean cancelTopAndBottom(AbstractSelectionList instance, Operation<Boolean> original, @Local(argsOnly = true) GuiGraphics guiGraphics) {

        Boolean call = original.call(instance);

        if (call && BlurShaderLoader.shouldCancelBackground()) {

            RenderSystem.enableBlend();
            guiGraphics.blit(INWORLD_HEADER_SEPARATOR, x0, y0 - 2, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
            guiGraphics.blit(INWORLD_FOOTER_SEPARATOR, x0, this.getBottom(), 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
            RenderSystem.disableBlend();

            return false;
        }

        return call;
    }

    @Definition(id = "renderBackground", field = "Lnet/minecraft/client/gui/components/AbstractSelectionList;renderBackground:Z")
    @Expression("this.renderBackground")
    @WrapOperation(method = "render", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    public boolean cancelBackground(AbstractSelectionList instance, Operation<Boolean> original, @Local(argsOnly = true) GuiGraphics guiGraphics) {
        Boolean call = original.call(instance);

        if (call && BlurShaderLoader.shouldCancelBackground()) {
            RenderSystem.enableBlend();
            ResourceLocation resourcelocation = this.minecraft.level == null ? MENU_LIST_BACKGROUND : INWORLD_MENU_LIST_BACKGROUND;
            guiGraphics.blit(
                    resourcelocation,
                    x0,
                    this.y0,
                    (float)this.x1,
                    (float)(this.y1 + (int)this.getScrollAmount()),
                    this.x1 - this.x0,
                    this.y1 - this.y0,
                    32,
                    32
            );
            RenderSystem.disableBlend();

            return false;
        }

        return call;
    }
}
