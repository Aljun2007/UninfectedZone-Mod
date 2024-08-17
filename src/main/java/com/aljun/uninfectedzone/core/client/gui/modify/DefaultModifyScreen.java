package com.aljun.uninfectedzone.core.client.gui.modify;

import com.aljun.uninfectedzone.core.client.gui.config.ConfigSetScreen;
import com.aljun.uninfectedzone.core.file.FileUtils;
import com.aljun.uninfectedzone.core.file.config.ConfigFileUtils;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class DefaultModifyScreen<T> extends AbstractConfigModifyScreen<T> {

    private static final Logger LOGGER = LogUtils.getLogger();
    protected final String path;
    private final MutableComponent failed = ComponentUtils.translate("screen.uninfectedzone.default_modify_screen.failed").withStyle(ChatFormatting.RED);
    private final MutableComponent success = ComponentUtils.translate("screen.uninfectedzone.default_modify_screen.success").withStyle(ChatFormatting.GREEN);
    protected Button recreateButton;
    protected Button openButton;
    protected Button refreshButton;
    private boolean isValid = false;

    protected DefaultModifyScreen(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
        super(origin, varSet, consumer, displayName);
        this.path = getCachePath();
        try {
            FileUtils.saveJsonFile(
                    this.path, this.create()
            );
        } catch (IOException e) {
            LOGGER.error("Creating File failed:{}", String.valueOf(e));
        }
    }

    protected String getCachePath() {
        return ConfigFileUtils.gamePath() + "\\cache\\" + this.hashCode() + ".json";
    }

    protected JsonObject create() {
        return this.serialize(this.origin);
    }

    protected JsonObject serialize(T t) {
        JsonObject jsonObject = new JsonObject();
        this.varSet.varType.serialize(jsonObject, "value", t);
        return jsonObject;
    }

    public static <T> AbstractConfigModifyScreen<T> create(T origin, VarSet<T> varSet, Consumer<T> consumer, MutableComponent displayName) {
        return new DefaultModifyScreen<>(origin, varSet, consumer, displayName);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        drawCenteredString(poseStack, this.font, this.isValid ? this.success : this.failed, this.width / 2, this.height / 2, ConfigSetScreen.WHITE);
    }

    @Override
    protected void init() {
        super.init();

        this.refreshButton = this.addRenderableWidget(new Button(this.width / 2 - 185, this.height / 2 - 15, 120, 20, ComponentUtils.translate("screen.uninfectedzone.default_modify_screen.refresh_button.name"), b -> {
            this.refresh();
        }));
        this.recreateButton = this.addRenderableWidget(new Button(this.width / 2 - 60, this.height / 2 - 15, 120, 20, ComponentUtils.translate("screen.uninfectedzone.default_modify_screen.recreate_button.name"), b -> {
            this.recreate();
        }));
        this.openButton = this.addRenderableWidget(new Button(this.width / 2 + 65, this.height / 2 - 15, 120, 20, ComponentUtils.translate("screen.uninfectedzone.default_modify_screen.open_file_folder_button.name"), b -> {
            this.openFileFolder();
        }));

        this.backOrCloseButton.setMessage(ComponentUtils.translate("screen.uninfectedzone.base.cancel"));

    }

    protected void refresh() {
        JsonObject object = this.get();
        if (object != null) {
            if (object.has("value")) {
                T t = this.deserialize(object);
                if (t != null) {
                    if (varSet.verify(t)) {
                        this.setValue(t);
                        this.isValid = true;
                    } else this.isValid = false;
                }
            }
        }
    }

    protected void recreate() {
        try {
            FileUtils.saveJsonFile(
                    this.path, this.serialize(this.getValue())
            );
        } catch (IOException e) {
            LOGGER.error("Recreating File failed:{}", String.valueOf(e));
        }
    }

    protected void openFileFolder() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("explorer.exe /select," + path);
        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }
    }

    @Nullable
    protected JsonObject get() {
        try {
            FileUtils.loadJsonFileOrCreate(
                    this.path, this::create
            );
        } catch (IOException e) {
            LOGGER.error("Reading File failed:{}", String.valueOf(e));
        }
        return null;
    }

    protected T deserialize(JsonObject jsonObject) {
        return this.varSet.varType.deserialize(jsonObject, "value");
    }

    @Override
    protected void defaultValue() {
        super.defaultValue();
        this.recreate();
    }

    @Override
    protected void originValue() {
        super.originValue();
        this.recreate();
    }
}
