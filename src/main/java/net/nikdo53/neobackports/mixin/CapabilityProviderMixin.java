package net.nikdo53.neobackports.mixin;

import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.nikdo53.neobackports.extensions.CapabilityProviderExtensions;
import net.nikdo53.neobackports.io.DataAttachmentRegistry;
import net.nikdo53.neobackports.io.attachment.DataAttachmentType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CapabilityProvider.class, remap = false)
public abstract class CapabilityProviderMixin implements CapabilityProviderExtensions {

    @Override
    public <T> void setData(DataAttachmentType<T> attachmentType, T data) {
        DataAttachmentRegistry.set(((ICapabilityProvider) (Object) this), attachmentType, data);
    }

    @Override
    public <T> T getData(DataAttachmentType<T> attachmentType) {
        return DataAttachmentRegistry.get(((ICapabilityProvider) (Object) this), attachmentType);
    }

    @Override
    public <T> void removeData(DataAttachmentType<T> attachmentType) {
        DataAttachmentRegistry.remove(((ICapabilityProvider) (Object) this), attachmentType);
    }
}
