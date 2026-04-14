package net.nikdo53.neobackports.extensions;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.Optional;

public interface IAddPackFindersEventExtension {

    /**
     * Helper method to register a pack found under the `resources/` folder.
     *
     * @param packLocation    Location of the pack to load. Namespace should be the modid of the pack owner and path is the location under `resources/` folder
     * @param packType        Whether pack is a resourcepack or datapack
     * @param packNameDisplay The text that shows for the pack on the pack selection screen
     * @param packSource      Controls whether the datapack is enabled or disabled by default. Resourcepacks are always disabled by default unless you set alwaysActive to true.
     * @param alwaysActive    Whether the pack is forced active always. If false, players have to manually activate the pack themselves
     * @param packPosition    Where the pack goes for determining pack applying order
     */
    default void addPackFinders(ResourceLocation packLocation, PackType packType, Component packNameDisplay, PackSource packSource, boolean alwaysActive, Pack.Position packPosition) {
        throw new RuntimeException("not implemented");
    }

}
