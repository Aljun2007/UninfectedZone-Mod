package com.aljun.uninfectedzone.core.client.gui.config;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigBaseScreen extends Screen {
    protected Screen lastScreen = null;
    protected Button backOrCloseButton = null;
    protected Button openButton = null;


    public ConfigBaseScreen(ConfigBaseScreen configBaseScreen) {
        this();
        this.lastScreen = configBaseScreen;
    }


    public ConfigBaseScreen() {
        this(ComponentUtils.translate(getNameSpace() + ".base.title"));
    }

    protected ConfigBaseScreen(Component component) {
        super(component);
    }

    protected static String getNameSpace() {
        return "screen.uninfectedzone";
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);

    }

    @Override
    protected void init() {
        this.backOrCloseButton = new Button(this.width / 2, this.height / 2, 100, 20, ComponentUtils.translate(getNameSpace() + ".base.back"), (button) -> {
            if (this.lastScreen != null) {
                Minecraft.getInstance().setScreen(lastScreen);
            }
            this.onClose();
        });
        this.openButton = new Button(this.width / 2, this.height / 2, 100, 20, ComponentUtils.translate(getNameSpace() + ".base.back"), (button) -> {
            if (this.lastScreen != null) {
                Minecraft.getInstance().setScreen(new ConfigBaseScreen(this));
            }
        });
        this.addRenderableWidget(backOrCloseButton);
        this.addRenderableWidget(openButton);
    }

    @Override
    public void tick() {

    }

    @Override
    public void removed() {

    }

}
