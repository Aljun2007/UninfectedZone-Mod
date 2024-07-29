package com.aljun.uninfectedzone.core.config;

public enum ConfigType {
    /*
     * 全局设置
     * */
    COMMON("common", 0),

    /*
     * 客户端设置，模型显示之类的
     * */
    CLIENT("client", 1),

    /*
     * 游戏规则
     * */
    GAME_RULE("game_rule", 2),

    /*
     * 游戏内各类属性，
     * 如僵尸奔跑速度，挖掘速度
     * */
    GAME_PROPERTY("game_property", 3),

    /*
     * 如僵尸感染生物对应产生生物
     * 僵尸预设支持的生物
     * */
    GAME_DATA("game_data", 4);

    public final int ID;

    private final String NAME;

    ConfigType(String name, int id) {
        this.NAME = name;
        this.ID = id;
    }


    public String getName() {
        return NAME;
    }

    public boolean is(ConfigType configType) {
        return this.ID == configType.ID;
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
