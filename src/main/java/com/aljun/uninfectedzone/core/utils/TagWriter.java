package com.aljun.uninfectedzone.core.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;

public class TagWriter<V> extends TagReader<V> {

    private static final Logger logger = LogUtils.getLogger();
    protected CompoundTag lastParentTag;

    TagWriter(CompoundTag root, VarSet<V> varSet) {
        super(root, varSet);
    }

    public void set(V var) {
        if (!varSet.verify(var)) {
            logger.error("", new IllegalArgumentException("Value : " + var.toString() + " is illegal, replaced : " + varSet.defaultVar()));
            var = varSet.defaultVar();
        }
        this.lastParentTag.put(varSet.KEY, varSet.tagType.writeToTag(var));
    }

    @Override
    public void updateLastVarTag() {
        final CompoundTag[] tag = {this.root};
        varSet.NAMESPACE.forEach((var) -> {
            if (!tag[0].contains(var))
                tag[0].put(var, new CompoundTag());
            tag[0] = tag[0].getCompound(var);
        });
        if (!tag[0].contains(varSet.KEY))
            tag[0].put(varSet.KEY, varSet.tagType.writeToTag(varSet.defaultVarSup.get()));
        this.lastParentTag = tag[0];
        this.lastVarTag = tag[0].get(varSet.KEY);
    }

}
