package com.aljun.uninfectedzone.core.client.gui.modify;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class EditBoxModifyScreen<T> extends AbstractConfigModifyScreen<T> {
    protected EditBox editBox;
    private boolean isValid = false;


    protected EditBoxModifyScreen(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
        super(origin, varSet, consumer, displayName);
    }

    public static <T> EditBoxModifyScreen<T> create(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
        return new EditBoxModifyScreen<>(origin, varSet, consumer, displayName);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.editBox.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();
        this.editBox = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 15, 200, 20, this.editBox, ComponentUtils.literature(""));
        this.editBox.setResponder(this::respond);
        this.reload();
    }

    protected void respond(String string) {
        T t = this.varSet.varType.fromString(string);
        if (t != null) {
            if (varSet.verify(t)) {
                this.setValue(t);
                this.reload();
                this.isValid = true;
            } else {
                this.isValid = false;
            }
        }
    }

    protected void reload() {
        this.editBox.setValue(this.varSet.varType.asString(this.getValue()));
    }

    @Override
    protected void defaultValue() {
        super.defaultValue();
        this.reload();
    }

    @Override
    protected void originValue() {
        super.originValue();
        this.reload();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isDirty) {
            if (this.isValid) {
                this.editBox.setTextColor(5635925);
            } else {
                this.editBox.setTextColor(16733525);
            }
        } else {
            this.editBox.setTextColor(16777215);
        }
    }
}
