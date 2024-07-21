package com.aljun.uninfectedzone.core.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.slf4j.Logger;

public class TagReader<V> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public final VarSet<V> varSet;
    protected final CompoundTag root;
    protected Tag lastVarTag;

    TagReader(CompoundTag root, VarSet<V> varSet) {
        this.varSet = varSet;
        this.root = root;
        this.updateLastVarTag();
    }

    public void updateLastVarTag() {
        if (root == null) {
            return;
        }
        final CompoundTag[] tag = {this.root};
        try {
            varSet.NAMESPACE.forEach((var) -> {
                if (tag[0].contains(var)) {
                    tag[0] = tag[0].getCompound(var);
                } else throw new IllegalArgumentException();
            });
        } catch (IllegalArgumentException e) {
            this.lastVarTag = varSet.varType.writeToTag(varSet.defaultVar());
            return;
        }
        if (tag[0].contains(varSet.KEY)) {
            this.lastVarTag = tag[0].get(varSet.KEY);
        } else {
            this.lastVarTag = varSet.varType.writeToTag(varSet.defaultVar());
        }
    }

    public V updateThenGet() {
        this.updateLastVarTag();
        return get();
    }

    public V get() {
        if (this.root == null) {
            return varSet.defaultVar();
        }
        try {
            return varSet.varType.readFromTag(this.lastVarTag);
        } catch (IllegalArgumentException e) {
            LOGGER.error(String.valueOf(e));
            return this.varSet.defaultVarSup.get();
        }

    }


}
