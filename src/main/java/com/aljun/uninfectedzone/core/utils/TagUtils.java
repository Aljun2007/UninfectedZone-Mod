package com.aljun.uninfectedzone.core.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

public class TagUtils {
    private static final Logger logger = LogUtils.getLogger();


    public static <V> TagReader<V> read(CompoundTag root, VarSet<V> varSet) {
        return new TagReader<>(root, varSet);
    }

    public static <V> V fastRead(CompoundTag root, VarSet<V> varSet) {
        final CompoundTag[] tag = {root};
        try {
            varSet.NAMESPACE.forEach((var) -> {
                if (tag[0].contains(var)) {
                    tag[0] = tag[0].getCompound(var);
                } else throw new IllegalArgumentException();
            });
        } catch (IllegalArgumentException e) {
            return varSet.defaultVar();
        }
        if (tag[0].contains(varSet.KEY)) {
            return varSet.tagType.readFromTag(tag[0].get(varSet.KEY));
        } else {
            return varSet.defaultVar();
        }
    }

    public static <V> TagWriter<V> write(CompoundTag root, VarSet<V> varSet) {
        return new TagWriter<>(root, varSet);
    }

    public static <V> void fastWrite(CompoundTag root, VarSet<V> varSet, V var) {
        final CompoundTag[] tag = {root};
        varSet.NAMESPACE.forEach((var1) -> {
            if (!tag[0].contains(var1))
                tag[0].put(var1, new CompoundTag());
            tag[0] = tag[0].getCompound(var1);
        });
        if (!tag[0].contains(varSet.KEY))
            tag[0].put(varSet.KEY, varSet.tagType.writeToTag(varSet.defaultVarSup.get()));
        if (!varSet.verify(var)) {
            logger.error("", new IllegalArgumentException("Value : " + var.toString() + " is illegal, replaced : " + varSet.defaultVar()));
            var = varSet.defaultVar();
        }
        tag[0].put(varSet.KEY, varSet.tagType.writeToTag(var));

    }

    public static CompoundTag getRoot(LivingEntity livingEntity) {
        return livingEntity.getPersistentData();
    }

    public static CompoundTag getRoot(ItemStack itemStack) {
        return itemStack.getTag();
    }

    public abstract static class TagType<T> {
        public static final TagType<Boolean> BOOLEAN = new TagType<>() {
            @Override
            public Tag writeToTag(Boolean aBoolean) {
                return ByteTag.valueOf(aBoolean);
            }

            @Override
            public Boolean readFromTag(Tag tag) {
                if (tag instanceof ByteTag) {
                    return ((ByteTag) tag).getAsByte() == 1;
                } else throw new IllegalArgumentException();
            }
        };

        public static final TagType<Integer> INTEGER = new TagType<Integer>() {
            @Override
            public Tag writeToTag(Integer integer) {
                return IntTag.valueOf(integer);
            }

            @Override
            public Integer readFromTag(Tag tag) {
                if (tag instanceof IntTag) {
                    return ((IntTag) tag).getAsInt();
                } else throw new IllegalArgumentException();
            }
        };

        public static final TagType<Short> SHORT = new TagType<Short>() {
            @Override
            public Tag writeToTag(Short aShort) {
                return IntTag.valueOf(aShort);
            }

            @Override
            public Short readFromTag(Tag tag) {
                if (tag instanceof ShortTag) {
                    return ((ShortTag) tag).getAsShort();
                } else throw new IllegalArgumentException();
            }
        };

        public static final TagType<Long> LONG = new TagType<Long>() {
            @Override
            public Tag writeToTag(Long aLong) {
                return LongTag.valueOf(aLong);
            }

            @Override
            public Long readFromTag(Tag tag) {
                if (tag instanceof LongTag) {
                    return ((LongTag) tag).getAsLong();
                } else throw new IllegalArgumentException();
            }
        };
        public static final TagType<Double> DOUBLE = new TagType<Double>() {
            @Override
            public Tag writeToTag(Double aDouble) {
                return DoubleTag.valueOf(aDouble);
            }

            @Override
            public Double readFromTag(Tag tag) {
                if (tag instanceof DoubleTag) {
                    return ((DoubleTag) tag).getAsDouble();
                } else throw new IllegalArgumentException();
            }
        };

        public static final TagType<Float> FLOAT = new TagType<Float>() {
            @Override
            public Tag writeToTag(Float aFloat) {
                return FloatTag.valueOf(aFloat);
            }

            @Override
            public Float readFromTag(Tag tag) {
                if (tag instanceof FloatTag) {
                    return ((FloatTag) tag).getAsFloat();
                } else throw new IllegalArgumentException();
            }
        };

        public static final TagType<String> STRING = new TagType<String>() {
            @Override
            public Tag writeToTag(String aString) {
                return StringTag.valueOf(aString);
            }

            @Override
            public String readFromTag(Tag tag) {
                if (tag instanceof StringTag) {
                    return tag.getAsString();
                } else throw new IllegalArgumentException();
            }
        };

        public abstract Tag writeToTag(T t);

        public abstract T readFromTag(Tag tag);
    }
}
