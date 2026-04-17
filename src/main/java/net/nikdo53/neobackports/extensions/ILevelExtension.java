package net.nikdo53.neobackports.extensions;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface ILevelExtension {
    default void playSound(@Nullable Player player, double x, double y, double z, SoundEvent sound, SoundSource category) {

    }

}
