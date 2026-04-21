package net.nikdo53.neobackports.extensions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ILifecycleRegistryExtension<T> {
   default Map<ResourceKey<T>, Lifecycle> getLifecycleKeyMap(){
       return Map.of();
   }

   default @NotNull Codec<Holder<T>> holderByNameCodecNeo(){
       throw new IllegalStateException("not implemented");
   }
}
