package net.nikdo53.neobackports.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.attachment.AttachmentType;

public class NeoForgeRegistries {
    public static IForgeRegistry<AttachmentType<?>> ATTACHMENT_TYPES_REAL;

    // This tricks other mods to register their own attachments using the key, which does not crash
    public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES = Keys.ATTACHMENT_TYPES;

    public static final class Keys {
        public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");


        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(new ResourceLocation(NeoBackports.MOD_ID, name));
        }

    }
}
