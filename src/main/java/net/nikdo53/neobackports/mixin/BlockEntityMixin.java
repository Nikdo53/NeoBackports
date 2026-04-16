package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.nikdo53.neobackports.extensions.IBlockEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements IBlockEntityExtension {
    @Shadow
    @Nullable
    public abstract Level getLevel();

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {

    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return new CompoundTag();
    }

    @WrapMethod(method = "saveAdditional")
    public void save(CompoundTag tag, Operation<Void> original){
        original.call(tag);
        saveAdditional(tag, getLevel() == null ? null : getLevel().registryAccess());
    }


    @WrapMethod(method = "load")
    public void load(CompoundTag tag, Operation<Void> original){
        original.call(tag);
        loadAdditional(tag, getLevel() == null ? null : getLevel().registryAccess());
    }


    @WrapMethod(method = "getUpdateTag")
    public CompoundTag update(Operation<CompoundTag> original){
        CompoundTag call = original.call();
        if (!call.isEmpty()) {
            return call;
        }

        return getUpdateTag(getLevel() == null ? null : getLevel().registryAccess());
    }
}
