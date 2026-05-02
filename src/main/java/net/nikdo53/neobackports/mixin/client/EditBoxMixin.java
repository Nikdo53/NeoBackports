package net.nikdo53.neobackports.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.nikdo53.neobackports.extensions.IEditBoxExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EditBox.class)
public class EditBoxMixin implements IEditBoxExtension {
    @Unique
    private boolean neoBackports$textShadow = true;

    @Override
    public void setTextShadow(boolean textShadow) {
        neoBackports$textShadow = textShadow;
    }

    @Override
    public boolean getTextShadow() {
        return neoBackports$textShadow;
    }

    @WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"))
    public int cancelDropShadow1(GuiGraphics instance, Font font, String text, int x, int y, int color, Operation<Integer> original) {
        if (neoBackports$textShadow) {
            return original.call(instance, font, text, x, y, color);
        }
        return instance.drawString(font, text, x, y, color, false);
    }

    @WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"))
    public int cancelDropShadow2(GuiGraphics instance, Font font, Component text, int x, int y, int color, Operation<Integer> original) {
        if (neoBackports$textShadow) {
            return original.call(instance, font, text, x, y, color);
        }
        return instance.drawString(font, text, x, y, color, false);
    }


    @WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)I"))
    public int cancelDropShadow3(GuiGraphics instance, Font font, FormattedCharSequence text, int x, int y, int color, Operation<Integer> original) {
        if (neoBackports$textShadow) {
            return original.call(instance, font, text, x, y, color);
        }
        return instance.drawString(font, text, x, y, color, false);
    }


}
