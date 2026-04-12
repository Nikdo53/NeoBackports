package net.nikdo53.neobackports.mixin;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.nikdo53.neobackports.extensions.IMutableComponentExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MutableComponent.class)
public abstract class MutableComponentMixin implements IMutableComponentExtension {
    @Shadow
    public abstract MutableComponent setStyle(Style style);

    @Shadow
    public abstract Style getStyle();

    @Override
    public MutableComponent withColor(int color) {
        setStyle(this.getStyle().withColor(color));
        return (MutableComponent) ((Object) this);
    }
}
