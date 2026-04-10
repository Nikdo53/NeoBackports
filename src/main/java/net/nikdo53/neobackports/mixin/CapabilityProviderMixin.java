package net.nikdo53.neobackports.mixin;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikdo53.neobackports.extensions.CapabilityProviderExtensions;
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.attachment.AttachmentType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ICapabilityProvider.class, remap = false)
public interface CapabilityProviderMixin extends CapabilityProviderExtensions {

    @Override
    public default <T> void setData(AttachmentType<T> attachmentType, T data) {
        DataAttachmentRegistry.set(((ICapabilityProvider) (Object) this), attachmentType, data);
    }

    @Override
    public default <T> T getData(AttachmentType<T> attachmentType) {
        return DataAttachmentRegistry.get(((ICapabilityProvider) (Object) this), attachmentType);
    }

    @Override
    public default <T> void removeData(AttachmentType<T> attachmentType) {
        DataAttachmentRegistry.remove(((ICapabilityProvider) (Object) this), attachmentType);
    }
}
