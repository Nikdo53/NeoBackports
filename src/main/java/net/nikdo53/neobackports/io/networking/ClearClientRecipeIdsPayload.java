package net.nikdo53.neobackports.io.networking;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.utils.recipe.RecipeIdHolder;
import org.jetbrains.annotations.Nullable;

public record ClearClientRecipeIdsPayload() implements ToClientPacket{
    public static final StreamCodec<ClearClientRecipeIdsPayload> STREAM_CODEC = StreamCodec.of((buf, clearClientRecipeIdsPayload) -> {} , buf -> new ClearClientRecipeIdsPayload());
    @Override
    public void handleClient(IPayloadContext context, Level level, Player player) {
        RecipeIdHolder.RECIPE_IDS.clear();
    }

    @Override
    public @Nullable Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
