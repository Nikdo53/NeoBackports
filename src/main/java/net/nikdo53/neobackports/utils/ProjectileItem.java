package net.nikdo53.neobackports.utils;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

//doesnt do anything rn but i might add the dispenser interactions later
public interface ProjectileItem {
    Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction);

    default void shoot(Projectile projectile, double x, double y, double z, float velocity, float inaccuracy) {
        projectile.shoot(x, y, z, velocity, inaccuracy);
    }
}
