package net.nikdo53.neobackports.io;


import com.mojang.serialization.Codec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.attachment.*;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataAttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.Keys.ATTACHMENT_TYPES, NeoBackports.MOD_ID);

    /**
     * For some ungodly reason, forge doesn't actually register capabilities under the resourceLocation you give them.
     * Instead, it registers them as their "real name" - meaning the name of the data type they hold.
     * <p>
     * For example, my DataAttachment type would always be named as nikdo53/neobackports/DataAttachment, and they would conflict (generics get ignored too)
     */
    public static class TestDataAttachment extends DataAttachment<String>{}

    public static final Capability<TestDataAttachment> TEST_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    public static final RegistryObject<AttachmentType<String>> TEST_ATTACHMENT = ATTACHMENT_TYPES.register(
            "test_attachment",
            () -> AttachmentType.builder(TEST_CAP, () -> "default")
                    .sync(StreamCodec.STRING)
                    .serialize(Codec.STRING)
                    .canAttachTo(AdvancedCapabilityType.ENTITY)
                    .build()
    );


    // sets the value to default
    public static <T> void remove(ICapabilityProvider holder, AttachmentType<T> attachmentType) {
        if (holder == null) return;
        if (AdvancedCapabilityType.checkWithHolder(holder, attachmentType)) return;

        LazyOptional<? extends DataAttachment<T>> capability = holder.getCapability(attachmentType.getAttachment().getCapabilityKey());
        if (!capability.isPresent())
            NeoBackports.LOGGER.error("can't remove capability: {} as it is not present for some reason?", attachmentType.id().toString());

        capability.ifPresent(cap -> cap.setDefault(holder));
    }

    public static <T> void set(ICapabilityProvider holder, AttachmentType<T> attachmentType, T data) {
        if (holder == null) return;
        if (AdvancedCapabilityType.checkWithHolder(holder, attachmentType)) return;

        LazyOptional<? extends DataAttachment<T>> capability = holder.getCapability(attachmentType.getAttachment().getCapabilityKey());
        if (!capability.isPresent())
            NeoBackports.LOGGER.error("can't set capability: {} as it is not present for some reason?", attachmentType.id().toString());

        capability.ifPresent(cap -> cap.setAndSync(holder, data));
    }

    @NotNull
    public static <T> T getOrDefault(ICapabilityProvider holder, AttachmentType<T> attachmentType) {
        T t = get(holder, attachmentType);
        if (t == null){
            return attachmentType.getAttachment().getDefault().get();
        }
        return t;
    }

    @Nullable
    public static <T> T get(ICapabilityProvider holder, AttachmentType<T> attachmentType) {
        if (holder == null)
            throw new NullPointerException("tried to get capability: " + attachmentType.id().toString() + " for a null holder");

        //Removed the error checking since just getting the default value should be fine
        return holder.getCapability(attachmentType.getAttachment().getCapabilityKey()).orElseGet(attachmentType::castAttachment).getData();
    }


    public static <T> void sync(ICapabilityProvider holder, AttachmentType<T> attachmentType) {
        if (holder == null) return;

        holder.getCapability(attachmentType.getAttachment().getCapabilityKey()).ifPresent(cap -> cap.sync(holder));
    }

    public static <T> boolean has(ICapabilityProvider holder, AttachmentType<T> attachmentType) {
        if (holder == null)
            throw new NullPointerException("tried to get capability: " + attachmentType.id().toString() + " for a null holder");

        LazyOptional<? extends DataAttachment<T>> capability = holder.getCapability(attachmentType.getAttachment().getCapabilityKey());
        if (!capability.isPresent())
            return false;

        return capability.orElseGet(attachmentType::castAttachment).hasData();
    }

    @ApiStatus.Internal
    public static <T> void setFrom(ICapabilityProvider holder, DataAttachment<T> attachment){
        holder.getCapability(attachment.getCapabilityKey()).ifPresent(cap -> cap.set(attachment.getOrDefault()));
    }

}
