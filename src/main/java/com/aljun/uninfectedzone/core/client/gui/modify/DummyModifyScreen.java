package com.aljun.uninfectedzone.core.client.gui.modify;

import com.aljun.uninfectedzone.core.utils.VarSet;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class DummyModifyScreen<T> extends AbstractConfigModifyScreen<T> {
    protected DummyModifyScreen(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
        super(origin, varSet, consumer, displayName);
    }

    public static <T> DummyModifyScreen<T> create(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
        return new DummyModifyScreen<>(origin, varSet, consumer, displayName);
    }
}
