package net.nikdo53.neobackports.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DataPackRegistriesHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(DataPackRegistriesHooks.class)
public interface DataPackRegistriesHooksAccessor {


    @Accessor(value = "NETWORKABLE_REGISTRIES", remap = false)
    static Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> getNetworkableRegistries(){
        throw new AssertionError();
    }

}
