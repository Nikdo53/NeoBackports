package net.nikdo53.neobackports.extensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.components.DataComponentType;
import net.nikdo53.neobackports.io.components.DataComponentRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.item.ItemStack.matches;

public interface ItemStackBackportExtension {

    default ItemStack consumeAndReturn(int amount, @Nullable LivingEntity entity) {
        ItemStack itemstack = neoBackports$selfDontUseThis().copyWithCount(amount);
        consume(amount, entity);
        return itemstack;
    }

    default void consume(int amount, @Nullable LivingEntity entity) {
        if (entity == null || !(entity instanceof Player player && player.getAbilities().instabuild)) {
            neoBackports$selfDontUseThis().shrink(amount);
        }
    }

    default ItemStack transmuteCopy(ItemLike item) {
        throw new IllegalStateException("Not implemented");
    }

    default ItemStack transmuteCopy(ItemLike item, int count) {
        throw new IllegalStateException("Not implemented");
    }

    @Nullable
    default <T> T get(DataComponentType<? extends T> component) {
        return DataComponentRegistry.get(neoBackports$selfDontUseThis(), component);
    }

    default <T> T getOrDefault(DataComponentType<T> component, T defaultValue) {
        return DataComponentRegistry.getOrDefault(neoBackports$selfDontUseThis(), component, defaultValue);
    }

    default boolean has(DataComponentType<?> component) {
        return DataComponentRegistry.has(neoBackports$selfDontUseThis(), component);
    }

    default <T> void set(DataComponentType<? super T> componentType, @Nullable T value){
        DataComponentRegistry.set(neoBackports$selfDontUseThis(), componentType, value);
    }

    default <T> void remove(DataComponentType<? super T> componentType){
        DataComponentRegistry.remove(neoBackports$selfDontUseThis(), componentType);
    }

    @Nullable
    default <T> T get(Supplier<DataComponentType<? extends T>> component) {
        return DataComponentRegistry.get(neoBackports$selfDontUseThis(), component.get());
    }

    default <T> T getOrDefault(Supplier<DataComponentType<T>> component, T defaultValue) {
        return DataComponentRegistry.getOrDefault(neoBackports$selfDontUseThis(), component.get(), defaultValue);
    }

    default boolean has(Supplier<DataComponentType<?>> component) {
        return DataComponentRegistry.has(neoBackports$selfDontUseThis(), component.get());
    }

    default <T> void set(Supplier<DataComponentType<T>> component, @Nullable T value){
        DataComponentRegistry.set(neoBackports$selfDontUseThis(), component.get(), value);
    }

    default <T> void remove(Supplier<DataComponentType<? extends T>> component){
        DataComponentRegistry.remove(neoBackports$selfDontUseThis(), component.get());
    }


    private ItemStack neoBackports$selfDontUseThis(){
        return (ItemStack) ((Object) this);
    }



    static int hashItemAndComponents(@Nullable ItemStack stack) {
        if (stack != null) {
            int i = 31 + stack.getItem().hashCode();
            return 31 * i + (stack.hasTag() ? stack.getTag().hashCode() : 0);
        } else {
            return 0;
        }
    }

    static int hashStackList(List<ItemStack> list) {
        int i = 0;

        for (ItemStack itemstack : list) {
            i = i * 31 + hashItemAndComponents(itemstack);
        }

        return i;
    }

    static boolean listMatches(List<ItemStack> list, List<ItemStack> other) {
        if (list.size() != other.size()) {
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!matches(list.get(i), other.get(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    StreamCodec<ItemStack> OPTIONAL_STREAM_CODEC = StreamCodec.ITEM_STACK;
    StreamCodec<ItemStack> STREAM_CODEC = StreamCodec.ITEM_STACK;

}
