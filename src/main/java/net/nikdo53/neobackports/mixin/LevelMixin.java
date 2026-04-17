package net.nikdo53.neobackports.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.extensions.ILevelExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Level.class)
public abstract class LevelMixin implements ILevelExtension {


    @Shadow
    public abstract void playSound(@Nullable Player player, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch);

    @Override
    public void playSound(@Nullable Player player, double x, double y, double z, SoundEvent sound, SoundSource category) {
        playSound(player, x, y, z, sound, category, 1.0f, 1.0f);
    }
}
