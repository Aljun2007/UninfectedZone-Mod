package com.aljun.uninfectedzone.core.utils;

import com.aljun.uninfectedzone.core.client.gui.modify.AbstractConfigModifyScreen;
import com.aljun.uninfectedzone.core.client.gui.modify.DefaultModifyScreen;
import com.aljun.uninfectedzone.core.client.gui.modify.EditBoxModifyScreen;
import com.aljun.uninfectedzone.core.client.gui.modify.EnumModifyScreen;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class VarSet<V> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    public final String MOD_ID;
    public final String ID;
    public final VarType<V> varType;
    final ArrayList<String> NAMESPACE;
    final String KEY;
    final Function<V, Boolean> verifyFunc;
    final Supplier<V> defaultVarSup;
    private final String nameSpaceString;

    private VarSet(Builder<V> builder, String key) {
        this.NAMESPACE = new ArrayList<>(builder.NAMESPACE);
        this.MOD_ID = this.NAMESPACE.get(0);
        this.KEY = key;
        this.verifyFunc = builder.verifyFunc;
        this.defaultVarSup = builder.defaultVarSup;
        this.varType = builder.varType;
        final String[] id = {""};
        NAMESPACE.forEach(a -> id[0] = id[0] + a + ".");
        id[0] = id[0] + key;
        this.ID = id[0];
        this.nameSpaceString = "var_set." + this.ID;
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

    public String getNameSpace() {
        return this.nameSpaceString;
    }

    @Override
    public int hashCode() {
        return this.nameSpaceString.hashCode();
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

        public static final VarType<Boolean> BOOLEAN = new VarType<>("boolean") {
            private final List<Boolean> allValues = List.of(true, false);

            @OnlyIn(Dist.CLIENT)
            @Override
            public AbstractConfigModifyScreen<Boolean> createModifyScreen(Boolean origin, VarSet<Boolean> varSet, Consumer<Boolean> consumer, MutableComponent displayName) {
                return EnumModifyScreen.create(origin, varSet, consumer, this.allValues, displayName);
            }

            @Override
            public String asString(Boolean aBoolean) {
                return aBoolean.toString();
            }

            @Override
            public Boolean fromString(String tString) {
                return Boolean.valueOf(tString);
            }

            @Override
            public Component asText(Boolean aBoolean) {
                return ComponentUtils.translate("var_set.default.boolean." + aBoolean);
            }

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
            public Boolean deserialize(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsBoolean();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, Boolean var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<Integer> INTEGER = new VarType<>("int") {
            @OnlyIn(Dist.CLIENT)
            @Override
            public AbstractConfigModifyScreen<Integer> createModifyScreen(Integer origin, VarSet<Integer> varSet, Consumer<Integer> consumer, MutableComponent displayName) {
                return EditBoxModifyScreen.create(origin, varSet, consumer, displayName);
            }

            @Override
            public String asString(Integer integer) {
                return integer.toString();
            }

            @Override
            public Integer fromString(String tString) {
                return Integer.getInteger(tString);
            }

            @Override
            public Component asText(Integer integer) {
                return ComponentUtils.literature(this.asString(integer));
            }

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
            public Integer deserialize(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsInt();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, Integer var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<Short> SHORT = new VarType<>("short") {
            @OnlyIn(Dist.CLIENT)
            @Override
            public AbstractConfigModifyScreen<Short> createModifyScreen(Short origin, VarSet<Short> varSet, Consumer<Short> consumer, MutableComponent displayName) {
                return EditBoxModifyScreen.create(origin, varSet, consumer, displayName);
            }

            @Override
            public String asString(Short aShort) {
                return aShort.toString();
            }

            @Override
            public Short fromString(String tString) {
                return Short.valueOf(tString);
            }

            @Override
            public Component asText(Short aShort) {
                return ComponentUtils.literature(this.asString(aShort));
            }

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
            public Short deserialize(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsShort();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, Short var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<Long> LONG = new VarType<>("long") {
            @OnlyIn(Dist.CLIENT)
            @Override
            public AbstractConfigModifyScreen<Long> createModifyScreen(Long origin, VarSet<Long> varSet, Consumer<Long> consumer, MutableComponent displayName) {
                return EditBoxModifyScreen.create(origin, varSet, consumer, displayName);
            }

            @Override
            public String asString(Long aLong) {
                return aLong.toString();
            }

            @Override
            public Long fromString(String tString) {
                return Long.getLong(tString);
            }

            @Override
            public Component asText(Long aLong) {
                return ComponentUtils.literature(this.asString(aLong));
            }

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
            public Long deserialize(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsLong();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, Long var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<Double> DOUBLE = new VarType<>("double") {
            @OnlyIn(Dist.CLIENT)
            @Override
            public AbstractConfigModifyScreen<Double> createModifyScreen(Double origin, VarSet<Double> varSet, Consumer<Double> consumer, MutableComponent displayName) {
                return EditBoxModifyScreen.create(origin, varSet, consumer, displayName);
            }

            @Override
            public String asString(Double aDouble) {
                return aDouble.toString();
            }

            @Override
            public Double fromString(String tString) {
                return Double.valueOf(tString);
            }

            @Override
            public Component asText(Double aDouble) {
                return ComponentUtils.literature(this.asString(aDouble));
            }

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
            public Double deserialize(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsDouble();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, Double var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<Float> FLOAT = new VarType<>("float") {
            @OnlyIn(Dist.CLIENT)
            @Override
            public AbstractConfigModifyScreen<Float> createModifyScreen(Float origin, VarSet<Float> varSet, Consumer<Float> consumer, MutableComponent displayName) {
                return EditBoxModifyScreen.create(origin, varSet, consumer, displayName);
            }

            @Override
            public String asString(Float aFloat) {
                return aFloat.toString();
            }

            @Override
            public Float fromString(String tString) {
                return Float.valueOf(tString);
            }

            @Override
            public Component asText(Float aFloat) {
                return ComponentUtils.literature(this.asString(aFloat));
            }

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
            public Float deserialize(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsFloat();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, Float var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<String> STRING = new VarType<>("string") {


            @OnlyIn(Dist.CLIENT)
            @Override
            public AbstractConfigModifyScreen<String> createModifyScreen(String origin, VarSet<String> varSet, Consumer<String> consumer, MutableComponent displayName) {
                return EditBoxModifyScreen.create(origin, varSet, consumer, displayName);
            }

            @Override
            public String asString(String string) {
                return string;
            }

            @Override
            public String fromString(String tString) {
                return tString;
            }

            @Override
            public Component asText(String string) {
                return ComponentUtils.literature(string);
            }

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
            public String deserialize(JsonObject jsonObject, String key) {
                try {
                    return jsonObject.get(key).getAsString();
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, String var) {
                jsonObject.addProperty(key, var);
            }
        };
        public static final VarType<List<String>> STRING_LIST = new VarType<>("string_list") {
            @Override
            public String asString(List<String> strings) {
                return "";
            }

            @Override
            public List<String> fromString(String tString) {
                return null;
            }

            @Override
            public Component asText(List<String> strings) {
                return ComponentUtils.literature("...");
            }

            @Override
            public Tag writeToTag(List<String> strings) {
                ListTag tag = new ListTag();
                strings.forEach(string -> tag.addTag(tag.size(), StringTag.valueOf(string)));
                return tag;
            }

            @Override
            public List<String> readFromTag(Tag tag) {
                if (tag instanceof ListTag listTag && listTag.getElementType() == Tag.TAG_STRING) {
                    List<String> list = new ArrayList<>();
                    listTag.forEach(tag1 -> list.add(tag1.getAsString()));
                    return list;
                } else throw new IllegalArgumentException();
            }

            @Override
            public List<String> deserialize(JsonObject jsonObject, String key) {
                try {
                    JsonArray array = jsonObject.get(key).getAsJsonArray();
                    ArrayList<String> stringArrayList = new ArrayList<>();
                    array.forEach(jsonElement -> stringArrayList.add(jsonElement.getAsString()));
                    return stringArrayList;
                } catch (UnsupportedOperationException | NullPointerException e) {
                    LOGGER.error(e.toString());
                    return null;
                }
            }

            @Override
            public void serialize(JsonObject jsonObject, String key, List<String> var) {
                JsonArray array = new JsonArray();
                var.forEach(array::add);
                jsonObject.add(key, array);
            }
        };
        public final String NAME;

        public VarType(String name) {
            this.NAME = name;
        }

        @OnlyIn(Dist.CLIENT)
        public AbstractConfigModifyScreen<T> createModifyScreen(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
            return DefaultModifyScreen.create(origin, varSet, consumer, displayName);
        }

        public abstract String asString(T t);

        public abstract T fromString(String tString);

        public abstract Component asText(T t);

        @Override
        public int hashCode() {
            return this.NAME.hashCode();
        }

        public abstract Tag writeToTag(T t);

        public abstract T readFromTag(Tag tag);

        public abstract T deserialize(JsonObject jsonObject, String key);

        public abstract void serialize(JsonObject jsonObject, String key, T t);
    }
}
