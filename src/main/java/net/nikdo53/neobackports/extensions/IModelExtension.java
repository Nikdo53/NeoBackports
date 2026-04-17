package net.nikdo53.neobackports.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface IModelExtension {
    default void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color){
        throw new IllegalStateException("Not implemented");
    }

    default void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay){
        throw new IllegalStateException("Not implemented");
    }

}
