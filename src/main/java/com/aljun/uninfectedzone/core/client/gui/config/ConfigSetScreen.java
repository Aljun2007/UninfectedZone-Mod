package com.aljun.uninfectedzone.core.client.gui.config;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigSetScreen extends ConfigBaseScreen {
    public static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);
    public boolean isDirty = false;
    private ConfigSetList list = null;
    private EditBox searchBox = null;
    private Button modifyButton = null;
    private Button saveButton = null;

    public ConfigSetScreen() {
        super(ComponentUtils.translate("screen.uninfectedzone.config_set_screen.title"));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.list.render(poseStack, mouseX, mouseY, partialTick);
        this.searchBox.render(poseStack, mouseX, mouseY, partialTick);
        super.render(poseStack, mouseX, mouseY, partialTick);

    }

    @Override
    protected void init() {
        super.init();

        this.searchBox = new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, ComponentUtils.translate("screen.uninfectedzone.config.search_box.description"));
        this.list = new ConfigSetList(this, this.minecraft, this.width, this.height, 48, this.height - 64);
        this.addWidget(this.searchBox);
        this.addWidget(this.list);

        this.modifyButton = this.addRenderableWidget(new Button(this.width - 60 - 5, this.height - 50, 60, 20, ComponentUtils.translate("screen.uninfectedzone.config.modify_button.name"), (b) -> {
            this.modify();
        }));

        this.saveButton = this.addRenderableWidget(new Button(this.width - 60 - 5, this.height - 25, 60, 20, ComponentUtils.translate("screen.uninfectedzone.config.save_button.name"), (b) -> {
            this.saveAndQuit();
        }));
    }

    private void modify() {
        if (this.list.getSelected() != null) {
            ConfigBaseScreen screen = this.list.getSelected().modifyScreen();
            screen.lastScreen = this;
            Minecraft.getInstance().setScreen(screen);
        }
    }

    private void saveAndQuit() {
        this.backOrClose();
    }

    public void setSelected(ConfigSetList.ConfigSetEntry<?> entry) {

    }

    @Override
    public void tick() {
        this.searchBox.tick();
        this.modifyButton.active = this.list.getSelected() != null;
        this.saveButton.active = this.isDirty;
    }

    @Override
    public void removed() {

    }

    public Font font() {
        return this.font;
    }
}
