package com.aljun.uninfectedzone.common.client.render;

import com.aljun.uninfectedzone.common.client.utils.TextureUtils;
import com.aljun.uninfectedzone.common.minecraft.entity.CustomZombie;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CustomZombieRender extends HumanoidMobRenderer<CustomZombie, CustomZombieModel> {


    private boolean slim = false;

    public CustomZombieRender(EntityRendererProvider.Context context, boolean slim) {
        this(context, slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER, slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR, slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR, slim);
        this.slim = slim;
    }

    public CustomZombieRender(EntityRendererProvider.Context p_174458_, ModelLayerLocation p_174459_, ModelLayerLocation p_174460_, ModelLayerLocation p_174461_, boolean slim) {
        this(p_174458_, new CustomZombieModel(p_174458_.bakeLayer(p_174459_), slim), new CustomZombieModel(p_174458_.bakeLayer(p_174460_), slim), new CustomZombieModel(p_174458_.bakeLayer(p_174461_), slim));
    }

    protected CustomZombieRender(EntityRendererProvider.Context p_173910_, CustomZombieModel p_173911_, CustomZombieModel p_173912_, CustomZombieModel p_173913_) {
        super(p_173910_, p_173911_, 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, p_173912_, p_173913_));
    }

    protected boolean isShaking(@NotNull CustomZombie customZombie) {
        return super.isShaking(customZombie) || customZombie.isUnderWaterConverting();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CustomZombie customZombie) {
        return randomSkinFromUUID(customZombie.getUUID());
    }

    private ResourceLocation randomSkinFromUUID(UUID uuid) {
        int id = Math.abs(uuid.hashCode());
        if (this.slim) {
            return TextureUtils.slimCustomZombieFromSeed(id);
        } else {
            return TextureUtils.defaultCustomZombieFromSeed(id);
        }
    }
}

