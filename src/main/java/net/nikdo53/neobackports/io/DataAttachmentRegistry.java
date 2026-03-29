package net.nikdo53.neobackports.io;


import com.mojang.serialization.Codec;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.attachment.*;
import org.jetbrains.annotations.NotNull;

public class DataAttachmentRegistry {
    /**
     * For some ungodly reason, forge doesn't actually register capabilities under the resourceLocation you give them.
     * Instead, it registers them as their "real name" - meaning the name of the data type they hold.
     * <p>
     * For example, my DataAttachment type would always be named as nikdo53/neobackports/DataAttachment, and they would conflict (generics get ignored too)
     */
    public static abstract class TestDataAttachment extends DataAttachment<String>{}
    public static final Capability<TestDataAttachment> TEST_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    public static final DataAttachmentType<String> TEST_ATTACHMENT = DataAttachmentType.register(
            TEST_CAP, NeoBackports.loc("test_attachment"),
            DataAttachmentType.builder(() -> "default")
                    .sync(StreamCodec.STRING)
                    .serialize(Codec.STRING)
                    .canAttachTo(CapabilityType.ENTITY));


    // sets the value to default
    public static <T> void remove(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        if (holder == null) return;
        if (CapabilityType.checkWithHolder(holder, attachmentType)) return;

        LazyOptional<? extends DataAttachment<T>> capability = holder.getCapability(attachmentType.attachment().getCapabilityKey());
        if (!capability.isPresent())
            NeoBackports.LOGGER.error("can't remove capability: {} as it is not present for some reason?", attachmentType.name().toString());

        capability.ifPresent(cap -> cap.setDefault(holder));
    }

    public static <T> void set(ICapabilityProvider holder, DataAttachmentType<T> attachmentType, T data)
    {
        if (holder == null) return;
        if (CapabilityType.checkWithHolder(holder, attachmentType)) return;

        LazyOptional<? extends DataAttachment<T>> capability = holder.getCapability(attachmentType.attachment().getCapabilityKey());
        if (!capability.isPresent())
            NeoBackports.LOGGER.error("can't set capability: {} as it is not present for some reason?", attachmentType.name().toString());

        capability.ifPresent(cap -> cap.setAndSync(holder, data));
    }

    @NotNull
    public static <T> T get(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        if (holder == null)
            throw new NullPointerException("tried to get capability: " + attachmentType.name().toString() + " for a null holder");

        //Removed the error checking since just getting the default value should be fine
        return holder.getCapability(attachmentType.attachment().getCapabilityKey()).orElseGet(attachmentType::getAttachment).getData();
    }

    public static <T> void sync(ICapabilityProvider holder, DataAttachmentType<T> attachmentType)
    {
        if (holder == null) return;

        holder.getCapability(attachmentType.attachment().getCapabilityKey()).ifPresent(cap -> cap.sync(holder));
    }

    public static <T> void setFrom(ICapabilityProvider holder, DataAttachment<T> attachment){
        holder.getCapability(attachment.getCapabilityKey()).ifPresent(cap -> cap.setNoSync(attachment.getData()));
    }

    public static void init(){

    }
}
