package com.aljun.uninfectedzone.core.client.gui.modify;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class EnumModifyScreen<T> extends AbstractConfigModifyScreen<T> {


    protected final List<T> allValues;
    protected int index;

    protected Button modifyButton;

    protected EnumModifyScreen(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName, List<T> allValues) {
        super(origin, varSet, consumer, displayName);
        this.allValues = allValues;
        this.index = this.allValues.indexOf(origin);
    }

    public static <T> AbstractConfigModifyScreen<T> create(T origin, VarSet<T> varSet, Consumer<T> consumer, List<T> allValues, MutableComponent displayName) {
        return new EnumModifyScreen<>(origin, varSet, consumer, displayName, allValues);
    }

    @Override
    protected void init() {
        super.init();
        this.modifyButton = this.addRenderableWidget(new Button(this.width / 2 - 30, this.height / 2 - 10, 60, 20, ComponentUtils.EMPTY.copy(), b -> {
            this.nextValue();
        }));
        this.updateButton();
    }

    protected void nextValue() {
        this.index++;
        if (index >= this.allValues.size()) {
            index = 0;
        }
        this.setValue(allValues.get(this.index));
        this.updateButton();
    }

    protected void updateButton() {
        Component buttonMessage = this.varSet.varType.asText(this.getValue());
        this.modifyButton.setWidth(Math.max(60, this.font.width(buttonMessage) + 10));
        this.modifyButton.setMessage(buttonMessage);
        this.modifyButton.x = this.width / 2 - this.modifyButton.getWidth() / 2;
    }

}
