package net.nikdo53.neobackports.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

public class ForgeRegistryHelper<T> {
    ResourceKey<Registry<T>> key;

    // an attempt at making forge registries suck less
    public ForgeRegistryHelper(ResourceKey<Registry<T>> key) {
        this.key = key;
    }

    public static <T1> ForgeRegistryHelper<T1> getInstance(ResourceKey<Registry<T1>> key){
        return new ForgeRegistryHelper<>(key);
    }

    @SuppressWarnings("unchecked")
    public void registerRegistry(Consumer<IForgeRegistry<T>> fieldSetter, IForgeRegistry<?> registeredRegistry){
        fieldSetter.accept((IForgeRegistry<T>) registeredRegistry);
    }

    public void create(NewRegistryEvent event, Consumer<IForgeRegistry<T>> fieldSetter, Function<RegistryBuilder<T>, RegistryBuilder<T>> builder){
        event.create(builder.apply(new RegistryBuilder<T>().setName(key.location())), fieldSetter);
    }

    public void create(NewRegistryEvent event, Consumer<IForgeRegistry<T>> fieldSetter){
        create(event, fieldSetter, Function.identity());
    }

}
