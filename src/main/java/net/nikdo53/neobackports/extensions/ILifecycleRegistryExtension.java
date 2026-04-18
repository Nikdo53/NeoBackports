package net.nikdo53.neobackports.extensions;

import com.mojang.serialization.Lifecycle;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

public interface ILifecycleRegistryExtension<T> {
   default Map<ResourceKey<T>, Lifecycle> getLifecycleKeyMap(){
       return Map.of();
   }
}
