package net.nikdo53.neobackports.extensions;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public interface IForgeRegistryExtension<T> {
    default HolderLookup.RegistryLookup<T> getRegistryLookup(){
        throw new IllegalStateException("not implemented");
    }

    default Registry<T> getVanillaRegistry(){
        throw new IllegalStateException("not implemented");
    }
}
