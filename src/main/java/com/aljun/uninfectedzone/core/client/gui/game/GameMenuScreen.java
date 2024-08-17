package com.aljun.uninfectedzone.core.client.gui.game;

import com.aljun.uninfectedzone.core.client.gui.BaseScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GameMenuScreen extends BaseScreen {
    private Button gameButton;
    private Button configButtonButton;
}
