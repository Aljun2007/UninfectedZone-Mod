package com.aljun.uninfectedzone.core.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class TagReader<V> {
    public final VarSet<V> varSet;
    protected final CompoundTag root;
    protected Tag lastVarTag;

    TagReader(CompoundTag root, VarSet<V> varSet) {
        this.varSet = varSet;
        this.root = root;
        this.updateLastVarTag();
    }

    public void updateLastVarTag() {
        final CompoundTag[] tag = {this.root};
        try {
            varSet.NAMESPACE.forEach((var) -> {
                if (tag[0].contains(var)) {
                    tag[0] = tag[0].getCompound(var);
                } else throw new IllegalArgumentException();
            });
        } catch (IllegalArgumentException e) {
            this.lastVarTag = varSet.tagType.writeToTag(varSet.defaultVar());
            return;
        }
        if (tag[0].contains(varSet.KEY)) {
            this.lastVarTag = tag[0].get(varSet.KEY);
        } else {
            this.lastVarTag = varSet.tagType.writeToTag(varSet.defaultVar());
        }
    }

    public V updateThenGet() {
        this.updateLastVarTag();
        return get();
    }

    public V get() {
        return varSet.tagType.readFromTag(this.lastVarTag);
    }


}
