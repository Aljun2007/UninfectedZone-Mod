package com.aljun.uninfectedzone.core.client.gui.config.modify;

import com.aljun.uninfectedzone.core.client.gui.config.ConfigSetList;

public class DefaultModifyScreen<T> extends AbstractConfigModifyScreen<T> {

    protected DefaultModifyScreen(T origin, ConfigSetList.ConfigSetEntry<T> configSetEntry) {
        super(origin, configSetEntry);
    }


    public static <T> AbstractConfigModifyScreen<T> create(ConfigSetList.ConfigSetEntry<T> tConfigSetEntry, T value) {
        return new DefaultModifyScreen<>(value, tConfigSetEntry);
    }
}
