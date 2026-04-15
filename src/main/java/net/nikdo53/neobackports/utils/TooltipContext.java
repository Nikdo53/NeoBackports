package net.nikdo53.neobackports.utils;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;

public interface TooltipContext {
    TooltipContext EMPTY = new TooltipContext() {
        @Nullable
        @Override
        public HolderLookup.Provider registries() {
            return null;
        }

        @Nullable
        @Override
        public MapItemSavedData mapData(String string) {
            return null;
        }
    };

    @Nullable
    HolderLookup.Provider registries();

   default float tickRate(){
     return 20.0F;
   }

    @Nullable
    MapItemSavedData mapData(String string);

    /**
     * Neo: Returns the level if it's available.
     */
    @Nullable
    default Level level() {
        return null;
    }

    static TooltipContext of(@Nullable final Level level) {
        return level == null ? EMPTY : new TooltipContext() {
            @Override
            public HolderLookup.Provider registries() {
                return level.registryAccess();
            }

            @Override
            public MapItemSavedData mapData(String string) {
                return level.getMapData(string);
            }

            @Override
            public Level level() {
                return level;
            }
        };
    }

    static TooltipContext of(final HolderLookup.Provider registries) {
        return new TooltipContext() {
            @Override
            public HolderLookup.Provider registries() {
                return registries;
            }

            @Nullable
            @Override
            public MapItemSavedData mapData(String string) {
                return null;
            }
        };
    }
}