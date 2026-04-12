package net.nikdo53.neobackports.mixin;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikdo53.neobackports.extensions.ICapabilityProviderExtension;
import net.nikdo53.neobackports.io.attachment.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(value = ICapabilityProvider.class, remap = false)
public interface ICapabilityProviderMixin extends ICapabilityProviderExtension {

    @Override
    default <T> void setData(AttachmentType<T> attachmentType, T data) {
        DataAttachmentRegistry.set(((ICapabilityProvider) (Object) this), attachmentType, data);
    }

    @Override
    default <T> T getData(AttachmentType<T> attachmentType) {
        return DataAttachmentRegistry.getOrDefault(((ICapabilityProvider) this), attachmentType);
    }

    @Override
    default <T> void removeData(AttachmentType<T> attachmentType) {
        DataAttachmentRegistry.remove((ICapabilityProvider) this, attachmentType);
    }

    @Override
    default <T> void setData(Supplier<AttachmentType<T>> attachmentType, T data) {
        setData(attachmentType.get(), data);
    }

    @Override
    default <T> T getData(Supplier<AttachmentType<T>> attachmentType) {
        return getData(attachmentType.get());
    }

    @Override
    default <T> void removeData(Supplier<AttachmentType<T>> attachmentType) {
        removeData(attachmentType.get());
    }

    @Override
    default <T> void syncData(AttachmentType<T> attachmentType) {
        DataAttachmentRegistry.sync((ICapabilityProvider) this, attachmentType);
    }

    @Override
    default <T> boolean hasData(AttachmentType<T> type) {
        return DataAttachmentRegistry.has((ICapabilityProvider) this, type);
    }

    @Override
    default <T> Optional<T> getExistingData(AttachmentType<T> type) {
        return Optional.ofNullable(DataAttachmentRegistry.get((ICapabilityProvider) this, type));
    }

    @Override
    default <T> Optional<T> getExistingData(Supplier<AttachmentType<T>> type) {
        return getExistingData(type.get());
    }

    @Override
    @Nullable
    default <T> T getExistingDataOrNull(AttachmentType<T> type) {
        return DataAttachmentRegistry.get((ICapabilityProvider) this, type);
    }

    @Override
    @Nullable
    default <T> T getExistingDataOrNull(Supplier<AttachmentType<T>> type) {
        return getExistingDataOrNull(type.get());
    }

    @Override
    default <T> void syncData(Supplier<AttachmentType<T>> attachmentType) {
        syncData(attachmentType.get());
    }

    @Override
    default <T> boolean hasData(Supplier<AttachmentType<T>> type) {
        return hasData(type.get());
    }
}
