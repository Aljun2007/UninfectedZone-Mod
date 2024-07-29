package com.aljun.uninfectedzone.core.client.gui;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class ConfigMenuScreen extends Screen {
    private static final String ID = "unknown_screen";

    protected ConfigMenuScreen() {
        super(ComponentUtils.translate("screen.uninfectedzone." + ID + ".title"));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
    }

    @Override
    protected void init() {
        
    }

    @Override
    public void tick() {

    }
}
