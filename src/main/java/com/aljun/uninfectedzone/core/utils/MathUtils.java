package com.aljun.uninfectedzone.core.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class MathUtils {

    public static Vec3 blockPosToVec3(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d);
    }


}
