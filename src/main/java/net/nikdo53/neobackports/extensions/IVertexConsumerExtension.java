package net.nikdo53.neobackports.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface IVertexConsumerExtension {
   default VertexConsumer addVertex(float x, float y, float z){
       throw new IllegalStateException("Not implemented");
   }


    default VertexConsumer setColor(int red, int green, int blue, int alpha){
       throw new IllegalStateException("Not implemented");
   }


    default VertexConsumer setUv(float u, float v){
       throw new IllegalStateException("Not implemented");
   }


    default VertexConsumer setUv1(int u, int v){
       throw new IllegalStateException("Not implemented");
   }

   default VertexConsumer setUv2(int u, int v){
       throw new IllegalStateException("Not implemented");
   }


    default VertexConsumer setNormal(float normalX, float normalY, float normalZ){
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer setColor(float red, float green, float blue, float alpha) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer setColor(int color) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer setWhiteAlpha(int alpha) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer setLight(int packedLight) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer setOverlay(int packedOverlay) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer addVertex(Vector3f pos) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer addVertex(PoseStack.Pose pose, Vector3f pos) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer addVertex(Matrix4f pose, float x, float y, float z) {
        throw new IllegalStateException("Not implemented");
    }

    default VertexConsumer setNormal(PoseStack.Pose pose, float normalX, float normalY, float normalZ) {
        throw new IllegalStateException("Not implemented");
    }

}
