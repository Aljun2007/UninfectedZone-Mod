package com.aljun.uninfectedzone.core.client.gui.config;

import com.aljun.uninfectedzone.core.client.gui.modify.AbstractConfigModifyScreen;
import com.aljun.uninfectedzone.core.config.ConfigSet;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.Collator;
import java.util.function.Function;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ConfigSetList extends ObjectSelectionList<ConfigSetList.ConfigSetEntry<?>> {
    private static Collator collator;

    private final ConfigSetScreen screen;


    public ConfigSetList(ConfigSetScreen screen, Minecraft minecraft, int x, int y, int width, int height) {
        super(minecraft, x, y, width, height, 20);
        this.screen = screen;
        this.refreshEntries(configSet -> true);
        this.setRenderBackground(false);

    }

    public void refreshEntries(Function<ConfigSet<?>, Boolean> filter) {
        this.clearEntries();
        this.screen.configList.forEach(configSet -> {
            if (filter.apply(configSet)) {
                this.addEntry(new ConfigSetEntry<>(configSet));
            }
        });
    }

    public void refreshEntries() {
        this.clearEntries();
        this.screen.configList.forEach(configSet -> {
            this.addEntry(new ConfigSetEntry<>(configSet));
        });
    }

    @Override
    public void setSelected(@Nullable ConfigSetList.ConfigSetEntry<?> entry) {
        super.setSelected(entry);
        this.screen.setSelected(entry);
    }

    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    public void filter(String string) {
        this.refreshEntries(configSet -> configSet.VAR_SET.ID.matches(string) || I18n.get("screen.uninfectedzone.config." + configSet.VAR_SET.getNameSpace() + ".name").matches(string));
    }


    public class ConfigSetEntry<T> extends Entry<ConfigSetEntry<?>> {

        public final ConfigSet<T> configSet;
        final Supplier<T> tSupplier;
        private final MutableComponent title;
        private final MutableComponent display;

        private ConfigSetEntry(ConfigSet<T> configSet) {
            this.configSet = configSet;
            this.tSupplier = UninfectedZoneConfig.getSupplier(configSet);
            this.title = ComponentUtils.translate("screen.uninfectedzone.config." + configSet.VAR_SET.getNameSpace() + ".name");

            this.display =
                    new TextComponent("")
                            .append(this.title.copy().withStyle(Style.EMPTY.withHoverEvent(
                                    new HoverEvent(
                                            HoverEvent.Action.SHOW_TEXT,
                                            ComponentUtils.translate("screen.uninfectedzone.config." + configSet.VAR_SET.getNameSpace() + ".description")
                                                    .append("\n")
                                                    .append(ComponentUtils.createModIDComponent(configSet.VAR_SET.MOD_ID))
                                    )
                            )))
                            .append(" " + I18n.get("screen.uninfectedzone.base.sign1") + " ")
                            .append(this.configSet.getValueDisplay(this.getValue())).append("   ")
                            .append(this.configSet.CONFIG_TYPE.getTranslatedName().withStyle(ChatFormatting.YELLOW))
                            .append(" ")
                            .append(this.configSet.CONFIG_TYPE.getTranslatedLocation().withStyle(ChatFormatting.AQUA));
        }

        public T getValue() {
            return tSupplier.get();
        }

        public void setValue(T t) {
            if (this.configSet.VAR_SET.verify(t)) {
                UninfectedZoneConfig.setOrOrigin(this.configSet, t);
                ConfigSetList.this.screen.isDirty = true;
                ConfigSetList.this.screen.isDirtyList.put(this.configSet, true);
            }
        }

        @Override
        public @NotNull Component getNarration() {
            return this.title;
        }

        @Override
        public void render(@NotNull PoseStack poseStack, int index, int rowTop, int rowLeft, int rowWidth, int itemHeight, int mouseX, int mouseY, boolean hover, float partialTick) {
            Component component = this.getDisplayComponent();
            drawCenteredString(poseStack, ConfigSetList.this.screen.font(), this.getDisplayComponent(), rowLeft + this.getDisplayWidth() / 2, rowTop + ConfigSetList.this.screen.font().lineHeight / 2, ConfigSetScreen.WHITE);
        }

        private Component getDisplayComponent() {
            return this.display;
        }

        private int getDisplayWidth() {
            return ConfigSetList.this.screen.font().width(this.getDisplayComponent());
        }

        @Override
        public boolean mouseClicked(double p_94737_, double p_94738_, int p_94739_) {
            ConfigSetList.this.setSelected(this);
            return super.mouseClicked(p_94737_, p_94738_, p_94739_);
        }

        public AbstractConfigModifyScreen<T> modifyScreen() {
            return this.configSet.VAR_SET.varType.createModifyScreen(this.getValue(), this.configSet.VAR_SET, this::setValue, this.getTitle());
        }

        public MutableComponent getTitle() {
            return this.title.copy();
        }

        public void defaultValue(T t) {
            UninfectedZoneConfig.setDefault(this.configSet);
        }

    }
}
