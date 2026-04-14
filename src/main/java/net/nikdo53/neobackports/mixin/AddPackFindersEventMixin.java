package net.nikdo53.neobackports.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.nikdo53.neobackports.extensions.IAddPackFindersEventExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(value = AddPackFindersEvent.class, remap = false)
public abstract class AddPackFindersEventMixin implements IAddPackFindersEventExtension {
    @Shadow
    public abstract PackType getPackType();

    @Shadow
    public abstract void addRepositorySource(RepositorySource source);

    @Override
    public void addPackFinders(ResourceLocation packLocation, PackType packType, Component packNameDisplay, PackSource packSource, boolean alwaysActive, Pack.Position packPosition) {
        if (getPackType() == packType) {
            IModInfo modInfo = ModList.get().getModContainerById(packLocation.getNamespace()).orElseThrow(() -> new IllegalArgumentException("Mod not found: " + packLocation.getNamespace())).getModInfo();

            var resourcePath = modInfo.getOwningFile().getFile().findResource(packLocation.getPath());

            var version = modInfo.getVersion();

            var pack = Pack.readMetaAndCreate(
                    "mod/" + packLocation,
                    packNameDisplay,
                    alwaysActive,
                    (path) -> new PathPackResources(path, resourcePath, true),
                    packType,
                    packPosition,
                    packSource
            );

            addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }
}
