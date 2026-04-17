package net.nikdo53.neobackports.mixin;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.nikdo53.neobackports.extensions.IBlockEntityRendererExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntityRenderer.class)
public interface BlockEntityRendererMixin<T extends BlockEntity> extends IBlockEntityRendererExtension<T> {
    @Override
    default AABB getRenderBoundingBox(T blockEntity) {
        return null;
    }
}
