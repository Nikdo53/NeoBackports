package net.nikdo53.neobackports.extensions;

import net.nikdo53.neobackports.io.attachment.AttachmentType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public interface ICapabilityProviderExtension {
    default <T> void setData(AttachmentType<T> attachmentType, T data){}

    default  <T> T getData(AttachmentType<T> attachmentType){
        throw new IllegalStateException("not implemented");
    };

    default <T> void removeData(AttachmentType<T> attachmentType){}


    default <T> void syncData(AttachmentType<T> attachmentType){}

    default <T> boolean hasData(AttachmentType<T> type) {
        throw new IllegalStateException("not implemented");
    }

    default <T> Optional<T> getExistingData(AttachmentType<T> type) {
        throw new IllegalStateException("not implemented");
    }

    default <T> Optional<T> getExistingData(Supplier<AttachmentType<T>> type) {
        throw new IllegalStateException("not implemented");
    }

    default <T> @Nullable T getExistingDataOrNull(AttachmentType<T> type) {
        throw new IllegalStateException("not implemented");
    }

    default <T> @Nullable T getExistingDataOrNull(Supplier<AttachmentType<T>> type) {
        throw new IllegalStateException("not implemented");
    }



    default <T> void setData(Supplier<AttachmentType<T>> attachmentType, T data){}

    default  <T> T getData(Supplier<AttachmentType<T>> attachmentType){
        throw new IllegalStateException("not implemented");
    };

    default <T> void removeData(Supplier<AttachmentType<T>> attachmentType){}

    default <T> void syncData(Supplier<AttachmentType<T>> attachmentType){}

    default <T> boolean hasData(Supplier<AttachmentType<T>> type) {
        throw new IllegalStateException("not implemented");
    }
}