package net.nikdo53.neobackports.io.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.registry.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class AttachmentType<T>{
    private static final Map<AdvancedCapabilityType, List<AttachmentType<?>>> DATA_ATTACHMENT_TYPES = new HashMap<>();
    private static final List<Capability<?>> REGISTERED_CAPABILITY_KEYS = new ArrayList<>();

    private static final Capability<DataAttachment<?>> IDIOT_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private Holder.Reference<AttachmentType<T>> builtInRegistryHolder = null;

    public final Builder<T> builder;
    public Supplier<DataAttachment<T>> attachmentSupplier;

    protected AttachmentType(Builder<T> builder) {
        this.builder = builder;
    }

    protected static <R> AttachmentType<R> create(Builder<R> builder) {

        if (REGISTERED_CAPABILITY_KEYS.contains(builder.capabilityKey)){
            throw new IllegalStateException("Capability key: " + builder.capabilityKey.getName() + " is already used for another attachment type, please create a new capability key for this attachment type");
        }
        REGISTERED_CAPABILITY_KEYS.add(builder.capabilityKey);

        AttachmentType<R> dataAttachment = new AttachmentType<>(builder);
        dataAttachment.attachmentSupplier = () -> DataAttachment.of(dataAttachment);

        if (builder.validHolders.isEmpty()){
            builder.validHolders.add(AdvancedCapabilityType.ALL);
            NeoBackports.LOGGER.debug("Registered data attachment: {} without specifying what it can attach to, defaulting to ALL. This might be bad for performance", builder.capabilityKey);
        }

        builder.validHolders.forEach(type -> {
            if (type == AdvancedCapabilityType.ALL){
                for (AdvancedCapabilityType subType : type.subTypes) {
                    List<AttachmentType<?>> list = DATA_ATTACHMENT_TYPES.computeIfAbsent(subType, type1 -> new ArrayList<>());
                    list.add(dataAttachment);
                }
            }
            List<AttachmentType<?>> list = DATA_ATTACHMENT_TYPES.computeIfAbsent(type, type1 -> new ArrayList<>());
            list.add(dataAttachment);
        });


        return dataAttachment;
    }

    public static List<AttachmentType<?>> getTypesForHolder(AdvancedCapabilityType type){
        return List.copyOf(DATA_ATTACHMENT_TYPES.getOrDefault(type, Collections.emptyList()));
    }

    public static StreamCodec<DataAttachment<?>> STREAM_CODEC_CODEC =
            StreamCodec.RESOURCE_LOCATION.remap(
                    loc -> NeoForgeRegistries.ATTACHMENT_TYPES.getValue(loc).getAttachment(),
                    DataAttachment::getId);


    @SuppressWarnings("unchecked")
    public <B extends DataAttachment<?>> B castAttachment(){
        return (B) getAttachment();
    }

    public DataAttachment<T> getAttachment(){
        if (id() == null)
            throw new IllegalStateException("Data attachment type not registered!");
        return attachmentSupplier.get();
    }

    public ResourceLocation id(){
        return builtInRegistryHolder().key().location();
    }

    @SuppressWarnings("unchecked, rawtypes")
    public Holder.Reference<AttachmentType<T>> builtInRegistryHolder(){
        if (this.builtInRegistryHolder == null) {
            Holder.Reference object = (NeoForgeRegistries.ATTACHMENT_TYPES.getDelegateOrThrow(this));
            Holder.Reference<AttachmentType<T>> builtInRegistryHolder = (Holder.Reference<AttachmentType<T>>) object;

            this.builtInRegistryHolder = builtInRegistryHolder;
            return builtInRegistryHolder;
        }

        return this.builtInRegistryHolder;
    }





    public static <D> Builder<D> builder(Capability<? extends DataAttachment<D>> capabilityKey ,Supplier<D> defaultValue){
        return new Builder<>(capabilityKey, defaultValue);
    }

    public static class Builder<B> {
        @Nullable StreamCodec<B> streamCodec = null;
        @Nullable Codec<B> codec = null;
        boolean copyOnDeath = false;
        Supplier<B> defaultValue;
        List<AdvancedCapabilityType> validHolders = new ArrayList<>();
        @Nullable String serializationId = null;
        Capability<? extends DataAttachment<B>> capabilityKey;

        public Builder(Capability<? extends DataAttachment<B>> capabilityKey ,Supplier<B> defaultValue) {
            this.capabilityKey = capabilityKey;
            this.defaultValue = defaultValue;

            if (capabilityKey.getName().equals(IDIOT_CAP.getName())){
                throw new IllegalStateException("Tried using a Capability<DataAttachment<?> as a capability key, this causes weird errors. Read the javadoc in DataAttachmentRegistry for more info");
            }
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

        public Builder<B> canAttachTo(AdvancedCapabilityType... holders){
            validHolders.addAll(Arrays.asList(holders));
            for (AdvancedCapabilityType holder : holders) {
                holder.validateRepeats(validHolders);
            }
            return this;
        }

        public Builder<B> canAttachTo(CapabilityType... holders){
            List<AdvancedCapabilityType> list = Arrays.stream(holders).map(CapabilityType::toAdvanced).toList();

            validHolders.addAll(list);
            for (AdvancedCapabilityType holder : list) {
                holder.validateRepeats(validHolders);
            }
            return this;
        }

        public AttachmentType<B> build(){
            return AttachmentType.create(this);
        }

    }
}
