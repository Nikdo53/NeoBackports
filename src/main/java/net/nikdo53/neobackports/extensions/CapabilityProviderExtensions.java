package net.nikdo53.neobackports.extensions;

import net.nikdo53.neobackports.io.attachment.DataAttachmentType;

public interface CapabilityProviderExtensions {
    default <T> void setData(DataAttachmentType<T> attachmentType, T data){}

    default <T> T getData(DataAttachmentType<T> attachmentType){
        return null;
    }

    default <T> void removeData(DataAttachmentType<T> attachmentType){}
}