package net.nikdo53.neobackports.extensions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;
import org.stringtemplate.v4.ST;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IDeferredRegisterExtension<T> {
   default   <I extends T> RegistryObject<I> register(final String name, final Function<ResourceLocation, ? extends I> func){
     throw new IllegalStateException("Not implemented");
   }

    /**
     * Factory for a specialized {@link DeferredRegister} for {@link Item Items}.
     *
     * @param modid The namespace for all objects registered to this {@link DeferredRegister}
     * @see #createBlocks(String)
     */
    static DeferredRegisterTyped.Items createItems(String modid) {
        return new DeferredRegisterTyped.Items(modid);
    }

    /**
     * Factory for a specialized DeferredRegister for {@link Block Blocks}.
     *
     * @param modid The namespace for all objects registered to this DeferredRegister
     * @see #createItems(String)
     */
    static DeferredRegisterTyped.Blocks createBlocks(String modid) {
        return new DeferredRegisterTyped.Blocks(modid);
    }

    default String neobackports$getModId(){
        throw new IllegalStateException("Not implemented");
    }
}
