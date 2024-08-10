package com.aljun.uninfectedzone.core.client.gui.config;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigFileBrowserScreen extends ConfigBaseScreen {
    private static final String ID = "unknown_screen";

    protected ConfigFileBrowserScreen() {
        super(ComponentUtils.translate("screen.uninfectedzone." + ID + ".title"));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack,mouseX,mouseY,partialTick);
    }

    @Override
    protected void init() {

    }

    @Override
    public void tick() {

    }
}
