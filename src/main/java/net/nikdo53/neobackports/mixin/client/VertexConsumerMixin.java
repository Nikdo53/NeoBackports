package net.nikdo53.neobackports.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;
import net.nikdo53.neobackports.extensions.IVertexConsumerExtension;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin extends IVertexConsumerExtension {
    @Shadow
    VertexConsumer vertex(double x, double y, double z);

    @Shadow
    VertexConsumer normal(float x, float y, float z);

    @Shadow
    VertexConsumer uv(float u, float v);


    @Shadow
    VertexConsumer color(float red, float green, float blue, float alpha);

    @Shadow
    VertexConsumer overlayCoords(int u, int v);

    @Shadow
    VertexConsumer color(int colorARGB);

    @Shadow
    VertexConsumer uv2(int lightmapUV);

    @Shadow
    VertexConsumer overlayCoords(int overlayUV);

    @Shadow
    void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int overlayUV, int lightmapUV, float normalX, float normalY, float normalZ);

    @Shadow
    VertexConsumer vertex(Matrix4f matrix, float x, float y, float z);

    @Shadow
    VertexConsumer normal(Matrix3f matrix, float x, float y, float z);

    @Override
    default VertexConsumer addVertex(float x, float y, float z) {
        return vertex(x, y, z);
    }

    @Override
    default VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
        return normal(normalX, normalY, normalZ);
    }

    @Override
    default VertexConsumer setUv2(int u, int v) {
        return setUv2(u, v);
    }

    @Override
    default VertexConsumer setUv1(int u, int v) {
        return overlayCoords(u, v);
    }

    @Override
    default VertexConsumer setColor(int red, int green, int blue, int alpha) {
        return color(red / 255f, green / 255f, blue / 255f, alpha / 255f);
    }

    @Override
    default VertexConsumer setUv(float u, float v) {
        return uv(u, v);
    }

    @Override
    default VertexConsumer setColor(float red, float green, float blue, float alpha) {
        return color(red, green, blue, alpha);
    }

    @Override
    default VertexConsumer setColor(int color) {
        return color(color);
    }

    @Override
    default VertexConsumer setWhiteAlpha(int alpha) {
        return setColor(FastColor.ARGB32.color(alpha, 255, 255, 255));
    }

    @Override
    default VertexConsumer setLight(int packedLight) {
        return uv2(packedLight);
    }

    @Override
    default VertexConsumer setOverlay(int packedOverlay) {
        return overlayCoords(packedOverlay);
    }

    @Override
    default VertexConsumer addVertex(Vector3f pos) {
        return vertex(pos.x, pos.y, pos.z);
    }

    @Override
    default VertexConsumer addVertex(PoseStack.Pose pose, Vector3f pos) {
        return vertex(pose.pose(), pos.x, pos.y, pos.z);
    }

    @Override
    default VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
        return vertex(pose.pose(), x, y, z);
    }

    @Override
    default VertexConsumer addVertex(Matrix4f pose, float x, float y, float z) {
        return vertex(pose, x, y, z);
    }

    @Override
    default VertexConsumer setNormal(PoseStack.Pose pose, float normalX, float normalY, float normalZ) {
        return normal(pose.normal(), normalX, normalY, normalZ);
    }
}
