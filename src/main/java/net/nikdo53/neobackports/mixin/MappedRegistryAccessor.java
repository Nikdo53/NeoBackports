package net.nikdo53.neobackports.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MappedRegistry.class)
public interface MappedRegistryAccessor<T> {
    @Accessor
    void setLookup(HolderLookup.RegistryLookup<T> lookup);

}
