package net.nikdo53.neobackports.io.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.LockCode;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.registry.DeferredHolder;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.List;

public class DataComponents {
    public static final DeferredRegisterTyped.DataComponents VANILLA_COMPONENTS =
            DeferredRegisterTyped.createDataComponents(ResourceLocation.DEFAULT_NAMESPACE);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAX_STACK_SIZE = VANILLA_COMPONENTS.registerComponentType(
            "max_stack_size", p_335179_ -> p_335179_.persistent(ExtraCodecs.intRange(1, 99))
    );
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> MAX_DAMAGE = VANILLA_COMPONENTS.registerComponentType(
            "max_damage", p_335177_ -> p_335177_.persistent(ExtraCodecs.POSITIVE_INT));
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> DAMAGE = VANILLA_COMPONENTS.registerComponentType(
            "damage", p_331382_ -> p_331382_.persistent(ExtraCodecs.NON_NEGATIVE_INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Component>> CUSTOM_NAME = VANILLA_COMPONENTS.registerComponentType(
            "custom_name",
            p_341853_ -> p_341853_.persistent(ItemStack::setHoverName, ItemStack::getHoverName)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Component>> ITEM_NAME = CUSTOM_NAME;
    /*
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unbreakable>> UNBREAKABLE = VANILLA_COMPONENTS.registerComponentType(
            "unbreakable", p_330880_ -> p_330880_.persistent(Unbreakable.CODEC).networkSynchronized(Unbreakable.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemLore>> LORE = VANILLA_COMPONENTS.registerComponentType(
            "lore", p_341842_ -> p_341842_.persistent(ItemLore.CODEC).networkSynchronized(ItemLore.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Rarity>> RARITY = VANILLA_COMPONENTS.registerComponentType(
            "rarity", p_335176_ -> p_335176_.persistent(Rarity.CODEC).networkSynchronized(Rarity.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemEnchantments>> ENCHANTMENTS = VANILLA_COMPONENTS.registerComponentType(
            "enchantments", p_341840_ -> p_341840_.persistent(ItemEnchantments.CODEC).networkSynchronized(ItemEnchantments.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AdventureModePredicate>> CAN_PLACE_ON = VANILLA_COMPONENTS.registerComponentType(
            "can_place_on",
            p_341861_ -> p_341861_.persistent(AdventureModePredicate.CODEC).networkSynchronized(AdventureModePredicate.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AdventureModePredicate>> CAN_BREAK = VANILLA_COMPONENTS.registerComponentType(
            "can_break", p_341837_ -> p_341837_.persistent(AdventureModePredicate.CODEC).networkSynchronized(AdventureModePredicate.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemAttributeModifiers>> ATTRIBUTE_MODIFIERS = VANILLA_COMPONENTS.registerComponentType(
            "attribute_modifiers",
            p_341845_ -> p_341845_.persistent(ItemAttributeModifiers.CODEC).networkSynchronized(ItemAttributeModifiers.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomModelData>> CUSTOM_MODEL_DATA = VANILLA_COMPONENTS.registerComponentType(
            "custom_model_data", p_330559_ -> p_330559_.persistent(CustomModelData.CODEC).networkSynchronized(CustomModelData.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> HIDE_ADDITIONAL_TOOLTIP = VANILLA_COMPONENTS.registerComponentType(
            "hide_additional_tooltip", p_344188_ -> p_344188_.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> HIDE_TOOLTIP = VANILLA_COMPONENTS.registerComponentType(
            "hide_tooltip", p_331610_ -> p_331610_.persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> REPAIR_COST = VANILLA_COMPONENTS.registerComponentType(
            "repair_cost", p_331555_ -> p_331555_.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> CREATIVE_SLOT_LOCK = VANILLA_COMPONENTS.registerComponentType(
            "creative_slot_lock", p_331031_ -> p_331031_.networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ENCHANTMENT_GLINT_OVERRIDE = VANILLA_COMPONENTS.registerComponentType(
            "enchantment_glint_override", p_330231_ -> p_330231_.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> INTANGIBLE_PROJECTILE = VANILLA_COMPONENTS.registerComponentType("intangible_projectile", p_344189_ -> p_344189_.persistent(Unit.CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FoodProperties>> FOOD = VANILLA_COMPONENTS.registerComponentType(
            "food", p_341858_ -> p_341858_.persistent(FoodProperties.DIRECT_CODEC).networkSynchronized(FoodProperties.DIRECT_STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit> FIRE_RESISTANT = VANILLA_COMPONENTS.registerComponentType(
            "fire_resistant", p_344190_ -> p_344190_.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Tool>> TOOL = VANILLA_COMPONENTS.registerComponentType(
            "tool", p_341839_ -> p_341839_.persistent(Tool.CODEC).networkSynchronized(Tool.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemEnchantments>> STORED_ENCHANTMENTS = VANILLA_COMPONENTS.registerComponentType(
            "stored_enchantments", p_341841_ -> p_341841_.persistent(ItemEnchantments.CODEC).networkSynchronized(ItemEnchantments.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyedItemColor>> DYED_COLOR = VANILLA_COMPONENTS.registerComponentType(
            "dyed_color", p_331088_ -> p_331088_.persistent(DyedItemColor.CODEC).networkSynchronized(DyedItemColor.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MapItemColor>> MAP_COLOR = VANILLA_COMPONENTS.registerComponentType(
            "map_color", p_330449_ -> p_330449_.persistent(MapItemColor.CODEC).networkSynchronized(MapItemColor.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MapId>> MAP_ID = VANILLA_COMPONENTS.registerComponentType(
            "map_id", p_330363_ -> p_330363_.persistent(MapId.CODEC).networkSynchronized(MapId.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MapDecorations> MAP_DECORATIONS = VANILLA_COMPONENTS.registerComponentType(
            "map_decorations", p_341862_ -> p_341862_.persistent(MapDecorations.CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MapPostProcessing> MAP_POST_PROCESSING = VANILLA_COMPONENTS.registerComponentType(
            "map_post_processing", p_331962_ -> p_331962_.networkSynchronized(MapPostProcessing.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ChargedProjectiles>> CHARGED_PROJECTILES = VANILLA_COMPONENTS.registerComponentType(
            "charged_projectiles", p_341859_ -> p_341859_.persistent(ChargedProjectiles.CODEC).networkSynchronized(ChargedProjectiles.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BundleContents>> BUNDLE_CONTENTS = VANILLA_COMPONENTS.registerComponentType(
            "bundle_contents", p_341857_ -> p_341857_.persistent(BundleContents.CODEC).networkSynchronized(BundleContents.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PotionContents>> POTION_CONTENTS = VANILLA_COMPONENTS.registerComponentType(
            "potion_contents", p_341836_ -> p_341836_.persistent(PotionContents.CODEC).networkSynchronized(PotionContents.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SuspiciousStewEffects>> SUSPICIOUS_STEW_EFFECTS = VANILLA_COMPONENTS.registerComponentType(
            "suspicious_stew_effects",
            p_341847_ -> p_341847_.persistent(SuspiciousStewEffects.CODEC).networkSynchronized(SuspiciousStewEffects.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WritableBookContent>> WRITABLE_BOOK_CONTENT = VANILLA_COMPONENTS.registerComponentType(
            "writable_book_content",
            p_341848_ -> p_341848_.persistent(WritableBookContent.CODEC).networkSynchronized(WritableBookContent.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WrittenBookContent>> WRITTEN_BOOK_CONTENT = VANILLA_COMPONENTS.registerComponentType(
            "written_book_content",
            p_341852_ -> p_341852_.persistent(WrittenBookContent.CODEC).networkSynchronized(WrittenBookContent.STREAM_CODEC).cacheEncoding()
    );

*/
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ArmorTrim>> TRIM = VANILLA_COMPONENTS.registerComponentType(
            "trim", p_341838_ -> p_341838_.persistent(ArmorTrim.CODEC)
    );

/*
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DebugStickState>> DEBUG_STICK_STATE = VANILLA_COMPONENTS.registerComponentType(
            "debug_stick_state", p_341865_ -> p_341865_.persistent(DebugStickState.CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> ENTITY_DATA = VANILLA_COMPONENTS.registerComponentType(
            "entity_data", p_332024_ -> p_332024_.persistent(CustomData.CODEC_WITH_ID).networkSynchronized(CustomData.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> BUCKET_ENTITY_DATA = VANILLA_COMPONENTS.registerComponentType(
            "bucket_entity_data", p_331109_ -> p_331109_.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> BLOCK_ENTITY_DATA = VANILLA_COMPONENTS.registerComponentType(
            "block_entity_data", p_330408_ -> p_330408_.persistent(CustomData.CODEC_WITH_ID).networkSynchronized(CustomData.STREAM_CODEC)
    );
    */

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Instrument>> INSTRUMENT = VANILLA_COMPONENTS.registerComponentType(
            "instrument", p_341855_ -> p_341855_.persistent(Instrument.CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> OMINOUS_BOTTLE_AMPLIFIER = VANILLA_COMPONENTS.registerComponentType(
            "ominous_bottle_amplifier", p_337458_ -> p_337458_.persistent(ExtraCodecs.intRange(0, 4))
    );


/*

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<JukeboxPlayable>> JUKEBOX_PLAYABLE = VANILLA_COMPONENTS.registerComponentType(
            "jukebox_playable", p_349913_ -> p_349913_.persistent(JukeboxPlayable.CODEC).networkSynchronized(JukeboxPlayable.STREAM_CODEC)
    );

*/

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceLocation>>> RECIPES = VANILLA_COMPONENTS.registerComponentType(
            "recipes", p_341850_ -> p_341850_.persistent(ResourceLocation.CODEC.listOf())
    );


/*
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LodestoneTracker>> LODESTONE_TRACKER = VANILLA_COMPONENTS.registerComponentType(
            "lodestone_tracker", p_341854_ -> p_341854_.persistent(LodestoneTracker.CODEC).networkSynchronized(LodestoneTracker.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FireworkExplosion>> FIREWORK_EXPLOSION = VANILLA_COMPONENTS.registerComponentType(
            "firework_explosion", p_341843_ -> p_341843_.persistent(FireworkExplosion.CODEC).networkSynchronized(FireworkExplosion.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Fireworks>> FIREWORKS = VANILLA_COMPONENTS.registerComponentType(
            "fireworks", p_341860_ -> p_341860_.persistent(Fireworks.CODEC).networkSynchronized(Fireworks.STREAM_CODEC).cacheEncoding()
    );
    */
/*
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResolvableProfile>> PROFILE = VANILLA_COMPONENTS.registerComponentType(
            "profile", p_341851_ -> p_341851_.persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding()
    );
    */

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> NOTE_BLOCK_SOUND = VANILLA_COMPONENTS.registerComponentType(
            "note_block_sound", p_330798_ -> p_330798_.persistent(ResourceLocation.CODEC)
    );

/*
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BannerPatternLayers>> BANNER_PATTERNS = VANILLA_COMPONENTS.registerComponentType(
            "banner_patterns", p_341863_ -> p_341863_.persistent(BannerPatternLayers.CODEC).networkSynchronized(BannerPatternLayers.STREAM_CODEC).cacheEncoding()
    );
    */

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DyeColor>> BASE_COLOR = VANILLA_COMPONENTS.registerComponentType(
            "base_color", p_331467_ -> p_331467_.persistent(DyeColor.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> CONTAINER = VANILLA_COMPONENTS.registerComponentType(
            "container", p_341846_ -> p_341846_.persistent(ItemContainerContents.CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SeededContainerLoot>> CONTAINER_LOOT = VANILLA_COMPONENTS.registerComponentType(
            "container_loot", p_331929_ -> p_331929_.persistent(SeededContainerLoot.CODEC)
    );
/*
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PotDecorations>> POT_DECORATIONS = VANILLA_COMPONENTS.registerComponentType(
            "pot_decorations", p_341864_ -> p_341864_.persistent(PotDecorations.CODEC).networkSynchronized(PotDecorations.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockItemStateProperties>> BLOCK_STATE = VANILLA_COMPONENTS.registerComponentType(
            "block_state",
            p_341856_ -> p_341856_.persistent(BlockItemStateProperties.CODEC).networkSynchronized(BlockItemStateProperties.STREAM_CODEC).cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<BeehiveBlockEntity.Occupant>>> BEES = VANILLA_COMPONENTS.registerComponentType(
            "bees",
            p_341849_ -> p_341849_.persistent(BeehiveBlockEntity.Occupant.LIST_CODEC)
                    .networkSynchronized(BeehiveBlockEntity.Occupant.STREAM_CODEC.apply(ByteBufCodecs.list()))
                    .cacheEncoding()
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LockCode>> LOCK = VANILLA_COMPONENTS.registerComponentType("lock", p_330909_ -> p_330909_.persistent(LockCode.CODEC));
*/



/*
    public static final DataComponentMap COMMON_ITEM_COMPONENTS = DataComponentMap.builder()
            .set(MAX_STACK_SIZE, 64)
            .set(LORE, ItemLore.EMPTY)
            .set(ENCHANTMENTS, ItemEnchantments.EMPTY)
            .set(REPAIR_COST, 0)
            .set(ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
            .set(RARITY, Rarity.COMMON)
            .build();
*/

}
