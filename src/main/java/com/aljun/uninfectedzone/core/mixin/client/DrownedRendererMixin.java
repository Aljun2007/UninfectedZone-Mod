package com.aljun.uninfectedzone.core.mixin.client;

import com.aljun.uninfectedzone.common.client.utils.TextureUtils;
import net.minecraft.client.renderer.entity.DrownedRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(DrownedRenderer.class)
public class DrownedRendererMixin {
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Zombie;)Lnet/minecraft/resources/ResourceLocation;"
            , at = @At("RETURN"), cancellable = true)
    public void getTextureLocation(Zombie zombie, CallbackInfoReturnable<ResourceLocation> cir) {
        if (TextureUtils.usePack()) {
            cir.setReturnValue(TextureUtils.randomDrownedPackSkin(zombie.getUUID().hashCode()));
        }
    }
}
