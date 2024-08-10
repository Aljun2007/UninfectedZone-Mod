package com.aljun.uninfectedzone.core.client;

import com.aljun.uninfectedzone.core.client.gui.config.ConfigSetList;
import com.aljun.uninfectedzone.core.client.gui.config.modify.AbstractConfigModifyScreen;
import com.aljun.uninfectedzone.core.client.gui.config.modify.DefaultModifyScreen;
import com.aljun.uninfectedzone.core.config.ConfigType;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.file.config.ConfigFileUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.google.gson.JsonObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;

@OnlyIn(Dist.CLIENT)
public class ClientConfigUtils {

    private static final HashMap<VarSet.VarType<?>, BiFunction<ConfigSetList.ConfigSetEntry<?>, Object, AbstractConfigModifyScreen<?>>> modifyScreens = new HashMap<>();


    @SuppressWarnings("unchecked")
    public static <T> void registerModifyScreen(VarSet.VarType<T> varType, BiFunction<ConfigSetList.ConfigSetEntry<T>, T, AbstractConfigModifyScreen<T>> biFunction) {
        modifyScreens.put(varType, (a, b) -> biFunction.apply((ConfigSetList.ConfigSetEntry<T>) a, (T) b));
    }

    public static void receive(JsonObject jsonObject) {
        if (jsonObject.get("type").isJsonNull()) return;
        String name = jsonObject.get("type").getAsString();
        Optional<ConfigType> type = Arrays.stream(ConfigType.values()).filter((type1) -> type1.getName().equals(name)).findFirst();
        type.ifPresent(configType -> UninfectedZoneConfig.loadAndFixJson(jsonObject, configType));
    }

    public static void loadGlobalClient() {
        JsonObject client = ConfigFileUtils.readConfigOrCreateBlank(ConfigType.CLIENT);
        if (client != null) {
            UninfectedZoneConfig.loadAndFixJson(client, ConfigType.CLIENT);
            ConfigFileUtils.saveConfig(ConfigType.CLIENT, client);
        }
    }

    public static void saveGlobalClient() {
        JsonObject client = UninfectedZoneConfig.toJsonObject(ConfigType.CLIENT);
        UninfectedZoneConfig.writeToJson(client, ConfigType.CLIENT);
        ConfigFileUtils.saveConfig(ConfigType.CLIENT, client);
    }

    @SuppressWarnings("unchecked")
    public static <T> AbstractConfigModifyScreen<T> createModifyScreen(ConfigSetList.ConfigSetEntry<T> tConfigSetEntry, T value) {
        if (modifyScreens.containsKey(tConfigSetEntry.configSet.VAR_SET.varType)) {
            return (AbstractConfigModifyScreen<T>) modifyScreens.get(tConfigSetEntry.configSet.VAR_SET.varType).apply(tConfigSetEntry, value);
        } else return DefaultModifyScreen.create(tConfigSetEntry, value);
    }
}
