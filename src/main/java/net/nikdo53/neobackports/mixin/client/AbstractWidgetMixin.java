package net.nikdo53.neobackports.mixin.client;

import net.minecraft.client.gui.components.AbstractWidget;
import net.nikdo53.neobackports.extensions.IAbstractWidgetExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractWidget.class)
public abstract class AbstractWidgetMixin implements IAbstractWidgetExtension {

    @Shadow
    public abstract int getX();

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract int getY();

    @Shadow
    public abstract int getHeight();

    @Override
    public int getBottom() {
        return this.getY() + this.getHeight();
    }

    @Override
    public int getRight() {
        return this.getX() + this.getWidth();
    }
}
