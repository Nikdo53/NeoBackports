package net.nikdo53.neobackports.extensions;

import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IDatagenConditionsExtension {

    default void registerConditions(BiConsumer<ResourceKey<?>, ICondition> consumer){

    }

    default Map<ResourceKey<?>, List<ICondition>> getConditionsMap(){
        return buildConditionsMap(this::registerConditions);
    }

    static Map<ResourceKey<?>, List<ICondition>> buildConditionsMap(Consumer<BiConsumer<ResourceKey<?>, ICondition>> conditionBuilder) {
        Map<ResourceKey<?>, List<ICondition>> conditions = new IdentityHashMap<>();
        conditionBuilder.accept((key, condition) -> conditions.computeIfAbsent(key, k -> new ArrayList<>()).add(condition));
        return conditions;
    }

}
