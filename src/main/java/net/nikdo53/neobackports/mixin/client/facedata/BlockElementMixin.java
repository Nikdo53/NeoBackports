package net.nikdo53.neobackports.mixin.client.facedata;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.ForgeFaceData;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(BlockElement.Deserializer.class)
public class BlockElementMixin {

    @WrapOperation(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/BlockElement;",
            at = @At(value = "NEW", target = "(Lorg/joml/Vector3f;Lorg/joml/Vector3f;Ljava/util/Map;Lnet/minecraft/client/renderer/block/model/BlockElementRotation;ZLnet/minecraftforge/client/model/ForgeFaceData;)Lnet/minecraft/client/renderer/block/model/BlockElement;"))
    public BlockElement readNeoData(Vector3f from, Vector3f _to, Map faces, BlockElementRotation rotation, boolean shade, ForgeFaceData faceData, Operation<BlockElement> original, @Local JsonObject json) {
        if (faceData != null) {
            return original.call(from, _to, faces, rotation, shade, faceData);
        }
        return original.call(from, _to, faces, rotation, shade, ForgeFaceData.read(json.get("neoforge_data"), ForgeFaceData.DEFAULT));
    }

}
