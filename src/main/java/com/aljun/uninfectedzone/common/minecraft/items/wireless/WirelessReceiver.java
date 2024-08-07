package com.aljun.uninfectedzone.common.minecraft.items.wireless;

import net.minecraft.world.item.Item;

public class WirelessReceiver extends Item {

    public WirelessReceiver() {
        super(new Properties());
    }

    public enum Mode {
        ;
        final int id;

        Mode(int id) {
            this.id = id;
        }

        public boolean is(Mode mode) {
            return this.id == mode.id;
        }
    }
}
