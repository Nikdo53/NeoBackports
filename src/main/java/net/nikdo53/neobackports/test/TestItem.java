package net.nikdo53.neobackports.test;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.nikdo53.neobackports.io.attachment.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.networking.PacketDistributorNeo;

public class TestItem extends Item {
    public TestItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            String data = player.getData(DataAttachmentRegistry.TEST_ATTACHMENT.get());
            System.out.println(data);

            player.setData(DataAttachmentRegistry.TEST_ATTACHMENT.get(), data + "works");
        }
        PacketDistributorNeo.sendToAllPlayers(new TestPacket("Hello from the server!"));

        return super.useOn(context);
    }
}
