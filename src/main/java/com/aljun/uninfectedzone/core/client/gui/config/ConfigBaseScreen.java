package com.aljun.uninfectedzone.core.client.gui.config;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigBaseScreen extends Screen {
    protected Screen lastScreen = null;
    protected Button backOrCloseButton = null;


    public ConfigBaseScreen(ConfigBaseScreen configBaseScreen) {
        this();
        this.lastScreen = configBaseScreen;
    }


    public ConfigBaseScreen() {
        this(ComponentUtils.translate("screen.uninfectedzone.base.title"));
    }

    protected ConfigBaseScreen(Component component) {
        super(component);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        Style style = Minecraft.getInstance().gui.getChat().getClickedComponentStyleAt((double) mouseX, (double) mouseY);
        if (style != null && style.getHoverEvent() != null) {
            this.renderComponentHoverEffect(poseStack, style, mouseX, mouseY);
        }
    }

    @Override
    protected void init() {
        this.backOrCloseButton = new Button(5, this.height - 25, 60, 20, ComponentUtils.translate("screen.uninfectedzone.base." + (this.lastScreen != null ? "back" : "close")), b -> backOrClose());
        this.addRenderableWidget(backOrCloseButton);
    }

    protected void backOrClose() {
        if (this.lastScreen != null) {
            Minecraft.getInstance().setScreen(lastScreen);
        } else {
            this.onClose();
        }
    }
}
