package com.aljun.uninfectedzone.core.client.gui.config;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FileList extends ObjectSelectionList<FileList.ConfigEntry> {
    public FileList(Minecraft p_94442_, int p_94443_, int p_94444_, int p_94445_, int p_94446_, int p_94447_) {
        super(p_94442_, p_94443_, p_94444_, p_94445_, p_94446_, p_94447_);
    }

    public static final class ConfigEntry extends ObjectSelectionList.Entry<ConfigEntry> {

        @Override
        public Component getNarration() {
            return null;
        }

        @Override
        public void render(PoseStack p_93523_, int p_93524_, int p_93525_, int p_93526_, int p_93527_, int p_93528_, int p_93529_, int p_93530_, boolean p_93531_, float p_93532_) {

        }
    }
}
