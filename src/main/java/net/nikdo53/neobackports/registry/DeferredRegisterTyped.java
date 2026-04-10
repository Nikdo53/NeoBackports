package net.nikdo53.neobackports.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;

public class DeferredRegisterTyped {

    public static class Items extends DeferredRegisterWrapper<Item> {
        public Items(DeferredRegister<Item> parent) {
            super(parent);
        }
    }

    public static class Blocks extends DeferredRegisterWrapper<net.minecraft.world.level.block.Block> {
        public Blocks(DeferredRegister<net.minecraft.world.level.block.Block> parent) {
            super(parent);
        }
    }
}
