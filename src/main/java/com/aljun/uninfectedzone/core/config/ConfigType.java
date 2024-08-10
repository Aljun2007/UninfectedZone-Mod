package com.aljun.uninfectedzone.core.config;

import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public enum ConfigType {
    /*
     * 全局设置
     * */
    COMMON("common", 0, "server"),

    /*
     * 客户端设置，模型显示之类的
     * */
    CLIENT("client", 1, "client"),

    /*
     * 游戏规则
     * */
    GAME_RULE("game_rule", 2, "server"),

    /*
     * 游戏内各类属性，
     * 如僵尸奔跑速度，挖掘速度
     * */
    GAME_PROPERTY("game_property", 3, "server"),

    /*
     * 如僵尸感染生物对应产生生物
     * 僵尸预设支持的生物
     * */
    GAME_DATA("game_data", 4, "server");

    public final int ID;

    private final String NAME;
    private final String location;

    ConfigType(String name, int id, String location) {
        this.NAME = name;
        this.ID = id;
        this.location = location;
    }

    public boolean is(ConfigType configType) {
        return this.ID == configType.ID;
    }

    public MutableComponent getTranslatedLocation() {
        return ComponentUtils.translate("screen.uninfectedzone.config.config_location." + this.getLocation() + ".name")
                .withStyle(
                        Style.EMPTY.withHoverEvent(
                                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        ComponentUtils.translate("screen.uninfectedzone.config.config_location." + this.getLocation() + ".description"))));
    }

    public String getLocation() {
        return this.location;
    }

    public MutableComponent getTranslatedName() {
        return ComponentUtils.translate("screen.uninfectedzone.config.config_type." + this.getName() + ".name")
                .withStyle(
                        Style.EMPTY.withHoverEvent(
                                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        ComponentUtils.translate("screen.uninfectedzone.config.config_type." + this.getName() + ".description"))));

    }

    public String getName() {
        return NAME;
    }

    public enum SavedLocation {
        CONSTANT(0), SAVE(1);
        public final int ID;

        SavedLocation(int id) {
            ID = id;
        }

        public boolean is(SavedLocation location) {
            return this.ID == location.ID;
        }
    }
}
