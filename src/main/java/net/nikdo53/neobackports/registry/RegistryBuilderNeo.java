package net.nikdo53.neobackports.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.nikdo53.neobackports.mixin.RegistryBuilderAccessor;

import java.util.*;
import java.util.function.Consumer;

public class RegistryBuilderNeo<T> {
    public static final Set<ForgeBuilder<?>> REGISTRY_BUILDERS = Collections.synchronizedSet(new HashSet<>());

    private final ResourceKey<? extends Registry<T>> registryKey;
    private ResourceLocation defaultKey;
    private int maxId = -1;
    private boolean sync = false;

    public RegistryBuilderNeo(ResourceKey<? extends Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }

    public RegistryBuilderNeo<T> defaultKey(ResourceLocation key) {
        this.defaultKey = key;
        return this;
    }

    public RegistryBuilderNeo<T> defaultKey(ResourceKey<T> key) {
        this.defaultKey = key.location();
        return this;
    }

    /**
     * Sets the highest numerical id that an entry in this registry
     * is <i>allowed</i> to use.
     * Must be greater than or equal to zero.
     *
     * @param maxId the highest numerical id
     */
    public RegistryBuilderNeo<T> maxId(int maxId) {
        if (maxId < 0)
            throw new IllegalArgumentException("maxId must be greater than or equal to zero");
        this.maxId = maxId;
        return this;
    }

    /**
     * Sets whether this registry should have its numerical IDs synced to clients.
     * Default: {@code false}.
     */
    public RegistryBuilderNeo<T> sync(boolean sync) {
        this.sync = sync;
        return this;
    }

    public Registry<T> create() {
        DelayedRegistry<T> delayRegistry = new DelayedRegistry<>();

        RegistryBuilder<T> builder = new RegistryBuilder<>();
        builder.setName(registryKey.location());
        if (defaultKey != null) builder.setDefaultKey(defaultKey);
        if (maxId > 0) builder.setMaxID(maxId);
        if (!sync) builder.disableSync();
        ((RegistryBuilderAccessor) builder).setWrapper();

        REGISTRY_BUILDERS.add(new ForgeBuilder<>(builder, delayRegistry::setRegistry));

        return delayRegistry;
    }

    public record ForgeBuilder<T>(RegistryBuilder<T> builder, Consumer<IForgeRegistry<T>> fieldSetter){
        public void register(NewRegistryEvent event){
            event.create(builder, fieldSetter);
        }
    }

}
