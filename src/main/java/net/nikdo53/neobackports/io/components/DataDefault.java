package net.nikdo53.neobackports.io.components;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public record DataDefault<T>(DataComponentType<T> component, T data) {
    private static final Map<Item, Map<DataComponentType<?>, ?>> DEFAULT_DATA_COMPONENTS_REGISTERED = new HashMap<>();

    public static Map<Item, Map<DataComponentType<?>, ?>> getDefaults(){
        return ImmutableMap.copyOf(DEFAULT_DATA_COMPONENTS_REGISTERED);
    }

    public static <T> void registerDefault(Item item, DataComponentType<T> component, T defaultData){
        registerDefault(item, new DataDefault<>(component, defaultData));
    }

    public static <T> void overrideDefault(Item item, DataComponentType<T> component, T defaultData){
        overrideDefault(item, new DataDefault<>(component, defaultData));
    }


    public void register(Item item){
        registerDefault(item, this);
    }

    public void override(Item item){
        overrideDefault(item, this);
    }

    public static void registerDefault(Item item, DataDefault<?> dataDefault){

        if (DEFAULT_DATA_COMPONENTS_REGISTERED.containsKey(item)){
            if (DEFAULT_DATA_COMPONENTS_REGISTERED.get(item).containsKey(dataDefault.component())){
                throw new IllegalArgumentException("Tried registering two different default data for the same data component: " + dataDefault.component().name() +  ", on item: " + item);
            };
        }

        overrideDefault(item, dataDefault);
    }

    @SuppressWarnings("unchecked")
    private static void overrideDefault(Item item, DataDefault<?> dataDefault){
        Map<DataComponentType<?>, Object> map = (Map<DataComponentType<?>, Object>) DEFAULT_DATA_COMPONENTS_REGISTERED.computeIfAbsent(item, i -> new HashMap<>());
        map.put(dataDefault.component(), dataDefault.data());
    }
}
