package net.nikdo53.neobackports.utils;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public class BlockEntityRenderHelper {
    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> AABB getRenderBoundingBox(BlockEntityRenderDispatcher dispatcher, BlockEntity blockEntity, Supplier<AABB> original){
        BlockEntityRenderer<T> renderer = (BlockEntityRenderer<T>) dispatcher.getRenderer(blockEntity);
        if (renderer == null)
            return original.get();
        AABB boundingBox = renderer.getRenderBoundingBox((T) blockEntity);
        if (boundingBox == null)
            return original.get();

        return boundingBox;
    }
}
