package net.nikdo53.neobackports.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class LayeredDraw {


    @OnlyIn(Dist.CLIENT)
    public interface Layer extends IGuiOverlay {
        @Override
        default void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            if (Minecraft.getInstance().level == null) {
                return;
            }

            render(guiGraphics, PartialTickHelper.INSTANCE.getPartialTicks(Minecraft.getInstance().level));
        }

        // made to match the 1.21 method, the other method can be overridden if needed
        void render(GuiGraphics guiGraphics, float deltaTracker);
    }
}