package net.nikdo53.neobackports.mixin;

import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikdo53.neobackports.extensions.CapabilityProviderExtensions;
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.attachment.DataAttachmentType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ICapabilityProvider.class, remap = false)
public interface CapabilityProviderMixin extends CapabilityProviderExtensions {

    @Override
    public default <T> void setData(DataAttachmentType<T> attachmentType, T data) {
        DataAttachmentRegistry.set(((ICapabilityProvider) (Object) this), attachmentType, data);
    }

    @Override
    public default <T> T getData(DataAttachmentType<T> attachmentType) {
        return DataAttachmentRegistry.get(((ICapabilityProvider) (Object) this), attachmentType);
    }

    @Override
    public default <T> void removeData(DataAttachmentType<T> attachmentType) {
        DataAttachmentRegistry.remove(((ICapabilityProvider) (Object) this), attachmentType);
    }
}
