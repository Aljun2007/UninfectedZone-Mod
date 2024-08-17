package com.aljun.uninfectedzone.core.client.gui.modify;

import com.aljun.uninfectedzone.core.client.gui.BaseScreen;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractConfigModifyScreen<T> extends BaseScreen {
    protected final T origin;
    protected final VarSet<T> varSet;
    protected final MutableComponent displayName;
    private final Consumer<T> consumer;
    protected Button saveButton;
    protected Button defaultButton;
    protected Button originButton;

    protected boolean isDirty = false;
    private T present;

    protected AbstractConfigModifyScreen(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
        super();
        this.consumer = consumer;
        this.varSet = varSet;
        this.origin = origin;
        this.present = origin;
        this.displayName = displayName;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        drawCenteredString(poseStack, this.font, this.displayName, this.width / 2, 22, 16777215);
    }

    @Override
    protected void init() {
        this.saveButton = this.addRenderableWidget(new Button(this.width - 65, this.height - 25, 60, 20, ComponentUtils.translate("screen.uninfectedzone.base.save_button.name"), b -> {
            this.save();
        }));
        this.defaultButton = this.addRenderableWidget(new Button(5, this.height - 50, 60, 20, ComponentUtils.translate("screen.uninfectedzone.base.default_button.name"), b -> {
            this.defaultValue();
        }));
        this.originButton = this.addRenderableWidget(new Button(5, this.height - 75, 60, 20, ComponentUtils.translate("screen.uninfectedzone.base.origin_button.name"), b -> {
            this.originValue();
        }));
        super.init();
        this.backOrCloseButton.setMessage(ComponentUtils.translate("screen.uninfectedzone.base.cancel"));
    }

    private void save() {
        if (isDirty) {
            consumer.accept(this.present);
        }
    }

    protected void defaultValue() {
        this.setValue(this.varSet.defaultVar());
    }

    protected void originValue() {
        this.setValue(this.origin);
    }

    @Override
    public void tick() {
        this.saveButton.active = this.isDirty;
        this.originButton.active = !this.isDirty;
    }

    protected T getValue() {
        return this.isDirty ? this.present : this.origin;
    }

    protected final void setValue(T value) {
        if (this.origin.equals(value)) {
            if (varSet.verify(value)) {
                this.present = value;
                this.defaultButton.active = value.equals(this.varSet.defaultVar());
                this.isDirty = true;
            }
        } else {
            this.isDirty = false;
        }
    }
}
