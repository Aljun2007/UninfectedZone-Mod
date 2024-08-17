package com.aljun.uninfectedzone.core.client.gui;

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
public class BaseScreen extends Screen {
    public Screen lastScreen = null;
    protected Button backOrCloseButton = null;


    public BaseScreen(BaseScreen configBaseScreen) {
        this();
        this.lastScreen = configBaseScreen;
    }


    public BaseScreen() {
        this(ComponentUtils.translate("screen.uninfectedzone.base.title"));
    }

    protected BaseScreen(Component component) {
        super(component);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
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
