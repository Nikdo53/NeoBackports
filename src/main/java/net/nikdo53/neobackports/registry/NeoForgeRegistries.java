package net.nikdo53.neobackports.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.attachment.AttachmentType;

import java.util.function.Supplier;

public class NeoForgeRegistries {
   // public static final Registry<AttachmentType<?>> ATTACHMENT_TYPES = new RegistryBuilder<>(Keys.ATTACHMENT_TYPES).create();

    public static IForgeRegistry<AttachmentType<?>> ATTACHMENT_TYPES;

    public static final class Keys {
        public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");


        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(new ResourceLocation(NeoBackports.MOD_ID, name));
        }

    }
}
