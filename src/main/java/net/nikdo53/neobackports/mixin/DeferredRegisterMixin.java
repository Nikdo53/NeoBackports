package net.nikdo53.neobackports.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nikdo53.neobackports.extensions.IDeferredRegisterExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(value = DeferredRegister.class, remap = false)
public abstract class DeferredRegisterMixin<T> implements IDeferredRegisterExtension<T> {
    @Shadow
    @Final
    private String modid;

    @Shadow
    public abstract <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> sup);

    @Override
    public <I extends T> RegistryObject<I> register(String name, Function<ResourceLocation, ? extends I> func) {
        final ResourceLocation key = new ResourceLocation(modid, name);

        return register(name, () -> func.apply(key));
    }

    @Override
    public String neobackports$getModId() {
        return modid;
    }
}
