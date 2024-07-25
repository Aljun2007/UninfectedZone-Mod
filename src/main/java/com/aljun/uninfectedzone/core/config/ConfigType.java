package com.aljun.uninfectedzone.core.config;

public enum ConfigType {
    /*
     * 全局设置
     * */
    GLOBAL("global"),

    /*
     * 客户端设置，模型显示之类的
     * */
    CLIENT("client"),

    /*
     * 游戏规则
     * */
    GAME_RULE("game_rule"),

    /*
     * 游戏内各类属性，
     * 如僵尸奔跑速度，挖掘速度
     * */
    GAME_PROPERTY("game_property"),

    /*
     * 如僵尸感染生物对应产生生物
     * 僵尸预设支持的生物
     * */
    GAME_DATA("game_data");

    private final String ID;

    ConfigType(String id) {
        this.ID = id;
    }

    public String getID() {
        return ID;
    }
}
