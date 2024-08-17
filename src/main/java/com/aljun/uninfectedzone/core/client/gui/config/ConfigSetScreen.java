package com.aljun.uninfectedzone.core.client.gui.config;

import com.aljun.uninfectedzone.core.client.ClientConfigUtils;
import com.aljun.uninfectedzone.core.client.gui.BaseScreen;
import com.aljun.uninfectedzone.core.config.ConfigSet;
import com.aljun.uninfectedzone.core.config.ConfigType;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.JsonManager;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ConfigSetScreen extends BaseScreen {
    public static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);
    final List<ConfigSet<?>> configList;
    final HashMap<ConfigSet<?>, Boolean> isDirtyList = new HashMap<>();
    private final int permission;
    public boolean isDirty = false;
    private ConfigSetList list = null;
    private EditBox searchBox = null;
    private Button modifyButton = null;
    private Button saveButton = null;

    public ConfigSetScreen() {
        super(ComponentUtils.translate("screen.uninfectedzone.config_set_screen.title"));
        if (Minecraft.getInstance().player != null) {
            this.permission = Minecraft.getInstance().player.getPermissionLevel();
        } else {
            this.permission = 0;
        }
        List<ConfigSet<?>> configSetList = new ArrayList<>(UninfectedZoneConfig.createAllSetsList().stream().filter(ConfigSet::isActive).toList());
        configSetList.sort(new ConfigSetComparator());
        this.configList = configSetList;
        ClientConfigUtils.enquireSever();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        this.list.render(poseStack, mouseX, mouseY, partialTick);
        this.searchBox.render(poseStack, mouseX, mouseY, partialTick);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();

        this.searchBox = new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, ComponentUtils.translate("screen.uninfectedzone.config.search_box.description"));
        this.list = new ConfigSetList(this, this.minecraft, this.width, this.height, 48, this.height - 64);
        this.searchBox.setResponder(this.list::filter);


        this.addWidget(this.searchBox);
        this.addWidget(this.list);

        this.modifyButton = this.addRenderableWidget(new Button(this.width - 60 - 5, this.height - 50, 60, 20, ComponentUtils.translate("screen.uninfectedzone.config.modify_button.name"), b -> {
            this.modify();
        }));

        this.saveButton = this.addRenderableWidget(new Button(this.width - 65, this.height - 25, 60, 20, ComponentUtils.translate("screen.uninfectedzone.base.save_button.name"), b -> {
            this.saveAndQuit();
        }));
    }

    private void modify() {
        if (this.list.getSelected() != null) {
            BaseScreen screen = this.list.getSelected().modifyScreen();
            screen.lastScreen = this;
            Minecraft.getInstance().setScreen(screen);
        }
    }

    private void saveAndQuit() {
        if (this.isDirty) {

            JsonObject common = createJson(ConfigType.COMMON);
            JsonObject game_data = createJson(ConfigType.GAME_DATA);
            JsonObject game_rule = createJson(ConfigType.GAME_RULE);
            JsonObject game_property = createJson(ConfigType.GAME_PROPERTY);

            this.isDirtyList.forEach((set, isDirty) -> {
                if (isDirty) {
                    JsonObject object;
                    if (set.CONFIG_TYPE.is(ConfigType.COMMON)) {
                        object = common;
                    } else if (set.CONFIG_TYPE.is(ConfigType.GAME_DATA)) {
                        object = game_data;
                    } else if (set.CONFIG_TYPE.is(ConfigType.GAME_RULE)) {
                        object = game_rule;
                    } else if (set.CONFIG_TYPE.is(ConfigType.GAME_PROPERTY)) {
                        object = game_property;
                    } else return;
                    this.writeToJson(object, set);
                }
            });

            ClientConfigUtils.sendToServer(common);
            ClientConfigUtils.sendToServer(game_data);
            ClientConfigUtils.sendToServer(game_rule);
            ClientConfigUtils.sendToServer(game_property);

            ClientConfigUtils.saveGlobalClient();

        }
        this.backOrClose();
    }

    private JsonObject createJson(ConfigType configType) {
        JsonObject object = new JsonObject();
        object.addProperty("type", configType.getName());
        return object;
    }

    private <T> void writeToJson(JsonObject object, ConfigSet<T> set) {
        JsonManager jsonManager = new JsonManager(object);
        jsonManager.write(set.VAR_SET, UninfectedZoneConfig.get(set));
    }

    public void setSelected(ConfigSetList.ConfigSetEntry<?> entry) {

    }

    @Override
    public void tick() {
        this.searchBox.tick();
        this.modifyButton.active = this.list.getSelected() != null && this.permission >= 2;
        this.saveButton.active = this.isDirty;
    }

    @Override
    public void removed() {

    }

    public Font font() {
        return this.font;
    }

    private static class ConfigSetComparator implements Comparator<ConfigSet<?>> {
        @Override
        public int compare(ConfigSet<?> o1, ConfigSet<?> o2) {
            return o1.VAR_SET.ID.compareTo(o2.VAR_SET.ID);
        }
    }
}
