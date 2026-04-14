package net.nikdo53.neobackports.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkHooks;
import net.nikdo53.neobackports.extensions.IPlayerExtension;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerExtension {
    @Override
    public void openMenu(MenuProvider menuProvider, BlockPos pos) {
        if ((Player) (Object) this instanceof ServerPlayer player) {
            NetworkHooks.openScreen(player, menuProvider, pos);;
        }
    }

    @Override
    public void openMenu(MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataWriter) {
        if ((Player) (Object) this instanceof ServerPlayer player) {
            NetworkHooks.openScreen(player, menuProvider, extraDataWriter);;
        }
    }
}
