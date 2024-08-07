package com.aljun.uninfectedzone.core.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class VarSet<V> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public final String ID;
    public final VarType<V> varType;
    final ArrayList<String> NAMESPACE;
    final String KEY;
    final Function<V, Boolean> verifyFunc;
    final Supplier<V> defaultVarSup;

    private VarSet(Builder<V> builder, String key) {
        this.NAMESPACE = new ArrayList<>(builder.NAMESPACE);
        this.KEY = key;
        this.verifyFunc = builder.verifyFunc;
        this.defaultVarSup = builder.defaultVarSup;
        this.varType = builder.varType;
        final String[] id = {""};
        NAMESPACE.forEach((a) -> {
            id[0] = id[0] + a + "\\";
        });
        id[0] = id[0] + key;
        this.ID = id[0];
    }

    public static <T> VarSet.Builder<T> builder(String root, VarType<T> varType) {
        return new VarSet.Builder<>(varType).next(root);
    }

    public boolean verify(V var) {
        return this.verifyFunc.apply(var);
    }

    public V defaultVar() {
        return this.defaultVarSup.get();
    }


    public static class Builder<V> {
        private final ArrayList<String> NAMESPACE = new ArrayList<>();
        private final VarType<V> varType;
        private Function<V, Boolean> verifyFunc = (var) -> true;
        private Supplier<V> defaultVarSup = () -> null;

        public Builder(VarType<V> varType) {
            this.varType = varType;
        }

        public Builder<V> verify(Function<V, Boolean> verifyFunc) {
            this.verifyFunc = verifyFunc;
            return this;
        }

        public Builder<V> defaultVar(Supplier<V> defaultVarSup) {
            this.defaultVarSup = defaultVarSup;
            return this;
        }

        public Builder<V> defaultVar(V defaultVar) {
            this.defaultVarSup = () -> defaultVar;
            return this;
        }

        public Builder<V> next(String namespace) {
            this.NAMESPACE.add(namespace);
            return this;
        }

        /*
         * 请给予默认值后再调用 create()
         * 否则极易报错 Null
         * */
        public VarSet<V> create(String key) {
            return new VarSet<>(this, key);
        }
    }

    public abstract static class VarType<T> {
        public static final VarType<Boolean> BOOLEAN = new VarType<>() {
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

            @Override
            public Boolean getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsBoolean();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, Boolean var) {
                jsonObject.addProperty(key, var);
            }

        };

        public static final VarType<Integer> INTEGER = new VarType<>() {
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

            @Override
            public Integer getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsInt();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, Integer var) {
                jsonObject.addProperty(key, var);
            }
        };

        public static final VarType<Short> SHORT = new VarType<>() {
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

            @Override
            public Short getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsShort();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, Short var) {
                jsonObject.addProperty(key, var);
            }
        };

        public static final VarType<Long> LONG = new VarType<>() {
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

            @Override
            public Long getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsLong();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, Long var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<Double> DOUBLE = new VarType<>() {
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

            @Override
            public Double getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsDouble();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, Double var) {
                jsonObject.addProperty(key, var);
            }
        };

        public static final VarType<Float> FLOAT = new VarType<>() {
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

            @Override
            public Float getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsFloat();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, Float var) {
                jsonObject.addProperty(key, var);
            }
        };

        public static final VarType<String> STRING = new VarType<>() {
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

            @Override
            public String getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsString();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, String var) {
                jsonObject.addProperty(key, var);
            }
        };

        public static final VarType<List<String>> STRING_LIST = new VarType<>() {
            @Override
            public Tag writeToTag(List<String> strings) {
                ListTag tag = new ListTag();
                strings.forEach((string) -> {
                    tag.addTag(tag.size(), StringTag.valueOf(string));
                });
                return tag;
            }

            @Override
            public List<String> readFromTag(Tag tag) {
                if (tag instanceof ListTag listTag && listTag.getElementType() == Tag.TAG_STRING) {
                    List<String> list = new ArrayList<>();
                    listTag.forEach(tag1 -> {
                        list.add(tag1.getAsString());
                    });
                    return list;
                } else throw new IllegalArgumentException();
            }

            @Override
            public List<String> getFromJsonObject(JsonObject jsonObject, String key) {
                try {
                    JsonArray array = jsonObject.get(key).getAsJsonArray();
                    ArrayList<String> stringArrayList = new ArrayList<>();
                    array.forEach((jsonElement -> {
                        stringArrayList.add(jsonElement.getAsString());
                    }));
                    return stringArrayList;
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void addToJsonObject(JsonObject jsonObject, String key, List<String> var) {
                JsonArray array = new JsonArray();
                var.forEach(array::add);
                jsonObject.add(key, array);
            }
        };

        public abstract Tag writeToTag(T t);

        public abstract T readFromTag(Tag tag);

        public abstract T getFromJsonObject(JsonObject jsonObject, String key);

        public abstract void addToJsonObject(JsonObject jsonObject, String key, T t);
    }

}
