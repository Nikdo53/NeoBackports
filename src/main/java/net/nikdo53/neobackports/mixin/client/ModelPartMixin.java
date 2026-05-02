package net.nikdo53.neobackports.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.nikdo53.neobackports.extensions.IModelPartExtension;
import net.nikdo53.neobackports.utils.FastColorNeo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements IModelPartExtension {
    @Shadow
    public abstract void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha);

    @Override
    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        float[] floatRGBA = FastColorNeo.ARGB32.toFloatRGBA(color);
        render(poseStack, vertexConsumer, packedLight, packedOverlay, floatRGBA[0], floatRGBA[1], floatRGBA[2], floatRGBA[3]);
    }
}
