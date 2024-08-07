package com.aljun.uninfectedzone.core.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

public class TagUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static <V> TagReader<V> read(CompoundTag root, VarSet<V> varSet) {
        if (root == null || varSet == null) return null;
        return new TagReader<>(root, varSet);
    }

    public static <V> V fastRead(CompoundTag root, VarSet<V> varSet) {
        if (root == null) return varSet.defaultVar();
        final CompoundTag[] tag = {root};
        try {
            varSet.NAMESPACE.forEach((var) -> {
                if (tag[0].contains(var) && tag[0].getTagType(var) == Tag.TAG_COMPOUND) {
                    tag[0] = tag[0].getCompound(var);
                } else throw new IllegalArgumentException();
            });
        } catch (IllegalArgumentException e) {
            return varSet.defaultVar();
        }
        if (tag[0].contains(varSet.KEY)) {
            try {
                return varSet.varType.readFromTag(tag[0].get(varSet.KEY));
            } catch (IllegalArgumentException e) {
                LOGGER.error(String.valueOf(e));
                return varSet.defaultVar();
            }
        } else {
            return varSet.defaultVar();
        }
    }

    public static <V> TagWriter<V> write(CompoundTag root, VarSet<V> varSet) {
        if (root == null || varSet == null) return null;
        return new TagWriter<>(root, varSet);
    }

    public static <V> void fastWrite(CompoundTag root, VarSet<V> varSet, V var) {
        if (root == null) return;
        if (!varSet.verify(var)) {
            LOGGER.error("", new IllegalArgumentException("Value : " + var.toString() + " is illegal, replaced : " + varSet.defaultVar()));
            var = varSet.defaultVar();
        }
        final CompoundTag[] tag = {root};
        varSet.NAMESPACE.forEach((var1) -> {
            if (!(tag[0].contains(var1) && tag[0].getTagType(var1) == Tag.TAG_COMPOUND))
                tag[0].put(var1, new CompoundTag());
            tag[0] = tag[0].getCompound(var1);
        });
        tag[0].put(varSet.KEY, varSet.varType.writeToTag(var));
    }

    public static CompoundTag getRoot(LivingEntity livingEntity) {
        return livingEntity.getPersistentData();
    }

    public static CompoundTag getRootOrNull(ItemStack itemStack) {
        return itemStack.getTag();
    }

    public static CompoundTag getRootOrCreate(ItemStack itemStack) {
        return itemStack.getOrCreateTag();
    }

}
