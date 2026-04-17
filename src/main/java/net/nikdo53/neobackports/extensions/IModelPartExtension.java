package net.nikdo53.neobackports.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface IModelPartExtension {
    default void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {

    }

}
