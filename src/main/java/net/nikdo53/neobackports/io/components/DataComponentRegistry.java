package net.nikdo53.neobackports.io.components;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static net.nikdo53.neobackports.io.components.DataDefault.getDefaults;

public class DataComponentRegistry {
    public static final List<String> NAMES = new ArrayList<>();

    public static final DataComponentType<Boolean> BOOLEAN = register("test_boolean", builder -> builder.persistent(Codec.BOOL));
    public static final DataComponentType<String> STRING = register("test_string", builder -> builder.persistent(Codec.STRING));

    public static synchronized <T> DataComponentType<T> register(ResourceLocation loc, UnaryOperator<DataComponentType.Builder<T>> func) {
        return register(loc.toString(), func);
    }

    public static synchronized <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> func) {
        if (NAMES.contains(name)){
            throw new IllegalArgumentException("Tried registering a DataComponent with a duplicate name " + name);
        }

        NAMES.add(name);

        DataComponentType.Builder<T> builder = DataComponentType.builder();
        builder.setName(name);

        return func.apply(builder).build();
    }


    public static  <T> void set(ItemStack stack, DataComponentType<T> component, T data){
        component.setOn(stack, data);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T get(ItemStack stack, DataComponentType<T> component){
        T ret = component.getOn(stack);

        if (ret == null && getDefaults().containsKey(stack.getItem())){
            ret = (T) getDefaults().get(stack.getItem()).get(component);
        }

        return ret;
    }

    public static <T> boolean has(ItemStack stack, DataComponentType<T> component){
        if (getDefaults().containsKey(stack.getItem())){

            if (getDefaults().get(stack.getItem()).containsKey(component)){
                return true;
            }
        }

        return component.isOn(stack);
    }

    public static  <T> void remove(ItemStack stack, DataComponentType<T> component){
        component.removeFrom(stack);
    }

    @Nonnull
    public static <T> T getOrDefault(ItemStack stack, DataComponentType<T> component, T defaultValue) {
        T value = get(stack, component);
        return value == null ? defaultValue : value;
    }

}
