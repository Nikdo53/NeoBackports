package net.nikdo53.neobackports.mixin.client.facedata;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.ForgeFaceData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockElementFace.Deserializer.class)
public class BlockElementFaceMixin {

    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/BlockElementFace;",
        at = @At(value = "NEW", target = "(Lnet/minecraft/core/Direction;ILjava/lang/String;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;Lnet/minecraftforge/client/model/ForgeFaceData;)Lnet/minecraft/client/renderer/block/model/BlockElementFace;"))
    public BlockElementFace readNeoData(Direction cullForDirection, int tintIndex, String texture, BlockFaceUV uv, ForgeFaceData faceData, Operation<BlockElementFace> original, @Local JsonObject json) {
        if (faceData != null) {
            return original.call(cullForDirection, tintIndex, texture, uv, faceData);
        }
        return original.call(cullForDirection, tintIndex, texture, uv, ForgeFaceData.read(json.get("neoforge_data"), null));
    }

}
