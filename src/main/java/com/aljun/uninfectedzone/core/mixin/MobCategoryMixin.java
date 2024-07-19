package com.aljun.uninfectedzone.core.mixin;


import com.aljun.uninfectedzone.core.event.provider.UninfectedZoneEventFactory;
import com.aljun.uninfectedzone.core.event.provider.world.MobMaxCountEvent;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobCategory.class)
public class MobCategoryMixin {
    @Inject(method = "getMaxInstancesPerChunk", at = @At("RETURN"), cancellable = true)
    public void getMaxInstancesPerChunk(CallbackInfoReturnable<Integer> cir) {
        MobMaxCountEvent event = UninfectedZoneEventFactory.onGettingMobMaxCount((MobCategory) (Object) this);
        if (!event.isCanceled()) {
            if (event.isDirty()) {
                cir.setReturnValue(event.getMaxCount());
            }
        }
    }
}
