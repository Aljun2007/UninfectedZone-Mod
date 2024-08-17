package com.aljun.uninfectedzone.core.mixin.client;

import com.aljun.uninfectedzone.common.client.render.ZombieEyeLayer;
import com.aljun.uninfectedzone.common.client.utils.TextureUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@OnlyIn(Dist.CLIENT)
@Mixin(ZombieRenderer.class)
public class ZombieRendererMixin {
    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", at = @At("TAIL"))
    public void init(EntityRendererProvider.Context context, CallbackInfo ci) {
        ZombieRenderer renderer = (ZombieRenderer) (Object) this;
        if (TextureUtils.useEye()) {
            renderer.addLayer(new ZombieEyeLayer<>(renderer, context.getModelSet()));
        }
    }
}
