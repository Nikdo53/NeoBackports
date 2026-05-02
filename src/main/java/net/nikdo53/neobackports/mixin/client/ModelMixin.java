package net.nikdo53.neobackports.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.nikdo53.neobackports.extensions.IModelExtension;
import net.nikdo53.neobackports.utils.FastColorNeo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Model.class)
public abstract class ModelMixin implements IModelExtension {

    @Shadow
    public abstract void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha);

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        float[] floatRGBA = FastColorNeo.ARGB32.toFloatRGBA(color);
        renderToBuffer(poseStack, buffer, packedLight, packedOverlay, floatRGBA[0], floatRGBA[1], floatRGBA[2], floatRGBA[3]);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        renderToBuffer(poseStack, buffer, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
    }
}
