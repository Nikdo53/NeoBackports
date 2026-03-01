package net.nikdo53.neobackports.io.components;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.nikdo53.neobackports.io.components.DataDefault.getDefaults;

public class DataComponentRegistry {
    public static final List<String> NAMES = new ArrayList<>();

    //smithing templates
    public static final DataComponent<Boolean> BOOLEAN = register("test_boolean", Codec.BOOL);
    public static final DataComponent<String> STRING = register("test_string", Codec.STRING);

    public static <T> DataComponent<T> register(String name, Codec<T> codec) {
        return register(name, codec, false);
    }

    /**
     * Registers a new DataComponent of the given type
     * @param name should be a ResourceLocation to avoid conflicts with other mods
     * @param codec the codec of the given type
     * @param deepScan whether the component should check for its whole contents, instead of just the name when
     *                 using the has method. It's worse for performance, but prevents conflicts with different data
     * @return the data component
     * @param <T> type of data the component holds
     */
    public static <T> DataComponent<T> register(String name, Codec<T> codec, boolean deepScan) {
        if (NAMES.contains(name)){
            throw new IllegalArgumentException("Tried registering a DataComponent with a duplicate name " + name);
        }

        NAMES.add(name);
        return new DataComponent<>(name, codec, deepScan);
    }


    public static  <T> void set(ItemStack stack, DataComponent<T> component, T data){
        component.setOn(stack, data);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T get(ItemStack stack, DataComponent<T> component){
        T ret = component.getOn(stack);

        if (ret == null && getDefaults().containsKey(stack.getItem())){
            List<DataDefault<?>> dataDefaults = getDefaults().get(stack.getItem());

            for (DataDefault<?> def : dataDefaults) {
                if (def.component().equals(component)){
                    ret = (T) def.data();
                    break;
                }
            }
        }

        return ret;
    }

    public static <T> boolean has(ItemStack stack, DataComponent<T> component){
        if (getDefaults().containsKey(stack.getItem())){

            if (getDefaults().get(stack.getItem()).stream().anyMatch(def -> def.component().equals(component))){
                return true;
            }
        }

        return component.isOn(stack);
    }

    public static  <T> void remove(ItemStack stack, DataComponent<T> component){
        component.removeFrom(stack);
    }

    @Nonnull
    public static <T> T getOrDefault(ItemStack stack, DataComponent<T> component, T defaultValue) {
        T value = get(stack, component);
        return value == null ? defaultValue : value;
    }

}
