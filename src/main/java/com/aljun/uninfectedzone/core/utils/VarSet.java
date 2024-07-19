package com.aljun.uninfectedzone.core.utils;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class VarSet<V> {
    final ArrayList<String> NAMESPACE;
    final String KEY;
    final Function<V, Boolean> verifyFunc;
    final Supplier<V> defaultVarSup;
    final TagUtils.TagType<V> tagType;
    private VarSet(Builder<V> builder, String key) {
        this.NAMESPACE = new ArrayList<>(builder.NAMESPACE);
        this.KEY = key;
        this.verifyFunc = builder.verifyFunc;
        this.defaultVarSup = builder.defaultVarSup;
        this.tagType = builder.tagType;
    }

    public static <T> VarSet.Builder<T> builder(String root, TagUtils.TagType<T> tagType) {
        return new VarSet.Builder<T>(tagType).next(root);
    }

    public boolean verify(V var) {
        return this.verifyFunc.apply(var);
    }

    public V defaultVar() {
        return this.defaultVarSup.get();
    }

    public static class Builder<V> {
        private final ArrayList<String> NAMESPACE = new ArrayList<>();
        private final TagUtils.TagType<V> tagType;
        private Function<V, Boolean> verifyFunc = (var) -> true;
        private Supplier<V> defaultVarSup = () -> null;

        public Builder(TagUtils.TagType<V> tagType) {
            this.tagType = tagType;
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


        public VarSet<V> create(String key) {
            return new VarSet<>(this, key);
        }
    }


}
