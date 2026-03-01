package net.nikdo53.neobackports.io.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.nikdo53.neobackports.io.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public record DataAttachmentType<T>(
        ResourceLocation name,
        Supplier<DataAttachment<T>> attachmentSupplier
) {

    public static final Map<ResourceLocation, DataAttachmentType<?>> REGISTERED_DATA_ATTACHMENTS = new HashMap<>();
    public static final Map<CapabilityType, List<DataAttachmentType<?>>> DATA_ATTACHMENT_TYPES = new HashMap<>();

    public static final List<String> NAMES = new ArrayList<>();

    public static StreamCodec<DataAttachment<?>> STREAM_CODEC_CODEC =
            StreamCodec.RESOURCE_LOCATION.remap(
                    loc -> DataAttachmentType.REGISTERED_DATA_ATTACHMENTS.get(loc).attachment(),
                    DataAttachment::getId);


    @SuppressWarnings("unchecked")
    public <B extends DataAttachment<?>> B getAttachment(){
        return (B) attachmentSupplier.get();
    }

    public DataAttachment<T> attachment(){
        return attachmentSupplier.get();
    }

    public static <R> DataAttachmentType<R> register(
            Capability<? extends DataAttachment<R>> capability,
            ResourceLocation name,
            Builder<R> builder
    ) {

        if (builder.validHolders.isEmpty()){
            throw new IllegalStateException("Tried registering a DataAttachmentType without a any Holders!");
        }

        DataAttachmentType<R> dataAttachment = new DataAttachmentType<>(name, () -> new DataAttachment<>() {
            @Override
            public @NotNull Supplier<R> getDefault() {
                return builder.defaultValue;
            }

            @Override
            public @Nullable StreamCodec<R> getStreamCodec() {
                return builder.streamCodec;
            }

            @Override
            public @Nullable Codec<R> getCodec() {
                return builder.codec;
            }

            @Override
            public boolean isCopyOnDeath() {
                return builder.copyOnDeath;
            }

            @Override
            public ResourceLocation getId() {
                return name;
            }

            @Override
            public Capability<? extends DataAttachment<R>> getCapabilityKey() {
                return capability;
            }

            @Override
            public List<CapabilityType> getPotentialHolders() {
                return builder.validHolders;
            }

            @Override
            public String getSerializationId() {
                if (builder.serializationId != null) {
                    return builder.serializationId;
                }
                return super.getSerializationId();
            }
        });

        if (NAMES.contains(name.getPath())){
            // not even 2 mods with different namespaces can have the same name (for easier saving/loading)
            throw new IllegalStateException("Duplicate name for data attachment type " + name.getPath());
        }

        NAMES.add(name.getPath());

        builder.validHolders.forEach(type -> {
            List<DataAttachmentType<?>> list = DATA_ATTACHMENT_TYPES.computeIfAbsent(type, type1 -> new ArrayList<>());
            list.add(dataAttachment);
        });

        REGISTERED_DATA_ATTACHMENTS.put(name, dataAttachment);

        return dataAttachment;
    }

    public static <D> Builder<D> builder(Supplier<D> defaultValue){
        Builder<D> builder = new Builder<>();
        builder.defaultValue = defaultValue;
        return builder;
    }


    public static class Builder<B> {
        @Nullable StreamCodec<B> streamCodec = null;
        @Nullable Codec<B> codec = null;
        boolean copyOnDeath = false;
        Supplier<B> defaultValue;
        List<CapabilityType> validHolders = new ArrayList<>();
        @Nullable String serializationId = null;

        public static <D> Builder<D> of(Supplier<D> defaultValue){
            Builder<D> builder = new Builder<>();
            builder.defaultValue = defaultValue;
            return builder;
        }

        public Builder<B> serialize(Codec<B> codec){
            this.codec = codec;
            return this;
        }

        public Builder<B> sync(StreamCodec<B> streamCodec){
            this.streamCodec = streamCodec;
            return this;
        }

        public Builder<B> copyOnDeath(){
            this.copyOnDeath = true;
            return this;
        }

        /**
         * Changes the name used to serialize to compound tag, defaults to the attachments ResourceLocation
         */
        public Builder<B> serializationId(String id){
            this.serializationId = id;
            return this;
        }

        public Builder<B> canAttachTo(CapabilityType... holders){
            validHolders.addAll(Arrays.asList(holders));
            for (CapabilityType holder : holders) {
                holder.validateRepeats(validHolders);
            }
            return this;
        }

    }
}
