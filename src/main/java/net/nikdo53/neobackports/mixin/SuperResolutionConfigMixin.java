package net.nikdo53.neobackports.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.homo.superresolution.common.config.SuperResolutionConfig;
import net.nikdo53.neobackports.screen.BlurShaderLoader;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(value = SuperResolutionConfig.class, remap = false)
public class SuperResolutionConfigMixin {
    @WrapMethod(method = "getInjectPostChainBlackList", expect = -1)
    private static List<String> getInjectPostChainBlackListInject(Operation<List<String>> original){
        List<String> call = original.call();
        call.add(BlurShaderLoader.BLUR_LOCATION.toString());
        return call;
    }
}
