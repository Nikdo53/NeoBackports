package net.nikdo53.neobackports.io.components;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record DataDefault<T>(DataComponent<T> component, T data) {
    private static final Map<Item, List<DataDefault<?>>> DEFAULT_DATA_COMPONENTS_REGISTERED = new HashMap<>();

    public static Map<Item, List<DataDefault<?>>> getDefaults(){
        return DEFAULT_DATA_COMPONENTS_REGISTERED;
    }

    public static <T> void registerDefault(Item item, DataComponent<T> component, T defaultData){
        registerDefault(item, new DataDefault<>(component, defaultData));
    }

    public static <T> void registerDefault(Item item, DataDefault<T> dataDefault){
        List<DataDefault<?>> listOld = DEFAULT_DATA_COMPONENTS_REGISTERED.get(item);
        List<DataDefault<?>> listNew = new ArrayList<>();

        if (listOld != null){
            if (listOld.stream().anyMatch(def -> def.component.equals(dataDefault.component()))){
                throw new IllegalArgumentException("Tried registering two different default data for the same data component: " + dataDefault.component().name() +  ", on item: " + item);
            }
            listNew.addAll(listOld);
        }

        listNew.add(dataDefault);

        registerDefaults(item, listNew);
    }

    private static void registerDefaults(Item item, List<DataDefault<?>> dataDefault){
        DEFAULT_DATA_COMPONENTS_REGISTERED.put(item, dataDefault);
    }
}
