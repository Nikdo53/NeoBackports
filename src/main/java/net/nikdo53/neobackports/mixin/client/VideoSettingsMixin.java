package net.nikdo53.neobackports.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(VideoSettingsScreen.class)
public class VideoSettingsMixin {

    @WrapMethod(method = "options")
    private static OptionInstance<?>[] addMenuBlurOption(Options options, Operation<OptionInstance<?>[]> original) {
        OptionInstance<?>[] call = original.call(options);
        List<OptionInstance<?>> tempList = new ArrayList<>(Arrays.stream(call).toList());
        tempList.add(BlurShaderLoader.INSTANCE.menuBackgroundBlurriness);

        return tempList.toArray(new OptionInstance[0]);
    }

}
