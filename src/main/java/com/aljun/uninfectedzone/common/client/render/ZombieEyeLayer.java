package com.aljun.uninfectedzone.common.client.render;

import com.aljun.uninfectedzone.common.client.utils.TextureUtils;
import com.aljun.uninfectedzone.common.minecraft.entity.UninfectedZoneEntityTypes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import org.jetbrains.annotations.NotNull;

public class ZombieEyeLayer<T extends Zombie, M extends HumanoidModel<T>> extends EyesLayer<T, M> {
    private static final ResourceLocation ZOMBIE_EYE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie_e.png");
    private static final RenderType ZOMBIE_EYES = RenderType.eyes(ZOMBIE_EYE_LOCATION);


    public ZombieEyeLayer(RenderLayerParent<T, M> parent, EntityModelSet set) {
        super(parent);
    }

    @Override
    public @NotNull RenderType renderType() {
        return ZOMBIE_EYES;
    }

    @Override
    protected @NotNull ResourceLocation getTextureLocation(@NotNull T zombie) {
        if (TextureUtils.usePack()) {
            if (zombie.getType().equals(EntityType.ZOMBIE) ||
                    zombie.getType().equals(UninfectedZoneEntityTypes.CUSTOM_ZOMBIE.get())) {
                return TextureUtils.randomZombieEye(zombie.getUUID().hashCode());
            } else if (zombie.getType().equals(EntityType.HUSK)) {
                return TextureUtils.randomHuskEye(zombie.getUUID().hashCode());
            }
        }
        return super.getTextureLocation(zombie);

    }
}
