package net.nikdo53.neobackports.extensions;

import net.nikdo53.neobackports.io.attachment.AttachmentType;

public interface CapabilityProviderExtensions {
    default <T> void setData(AttachmentType<T> attachmentType, T data){}

    default  <T> T getData(AttachmentType<T> attachmentType){
        throw new IllegalStateException("not implemented");
    };

    default <T> void removeData(AttachmentType<T> attachmentType){}
}