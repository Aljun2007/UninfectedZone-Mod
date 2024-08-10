package com.aljun.uninfectedzone.core.client.gui.config.modify;

import com.aljun.uninfectedzone.core.client.gui.config.ConfigSetList;
import net.minecraft.client.gui.components.Button;


public class EnumModifyScreen<T> extends AbstractConfigModifyScreen<T> {

    protected Button valueButton;

    protected EnumModifyScreen(T origin, ConfigSetList.ConfigSetEntry<T> configSetEntry) {
        super(origin, configSetEntry);
    }


}
