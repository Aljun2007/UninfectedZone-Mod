package com.aljun.uninfectedzone.core.client.gui.config.modify;

import com.aljun.uninfectedzone.core.client.gui.config.ConfigBaseScreen;
import com.aljun.uninfectedzone.core.client.gui.config.ConfigSetList;
import com.aljun.uninfectedzone.core.config.ConfigSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractConfigModifyScreen<T> extends ConfigBaseScreen {
    protected final T origin;
    protected final ConfigSet<T> configSet;
    private final ConfigSetList.ConfigSetEntry<T> configSetEntry;
    protected boolean isDirty = false;
    private T present;

    protected AbstractConfigModifyScreen(T origin, ConfigSetList.ConfigSetEntry<T> configSetEntry) {
        super();
        this.configSetEntry = configSetEntry;
        this.configSet = configSetEntry.configSet;
        this.origin = origin;
        this.present = origin;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    protected final boolean setValue(T value) {
        if (this.origin.equals(value)) {
            if (configSet.VAR_SET.verify(value)) {
                this.present = value;
                return true;
            } else {
                return false;
            }
        } else {
            this.isDirty = false;
            return true;
        }
    }

    @Override
    public void removed() {
        super.removed();
        if (isDirty) {
            this.configSetEntry.setValue(present);
        }
    }
}
