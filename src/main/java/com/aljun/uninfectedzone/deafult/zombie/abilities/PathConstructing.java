package com.aljun.uninfectedzone.deafult.zombie.abilities;

import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class PathConstructing extends ZombieAbility {
    public static String KEY = "path_constructing";

    @Override
    protected ZombieAbilityInstance<? extends ZombieAbility> create(ZombieMainGoal mainGoal) {
        return null;
    }

    @Override
    protected void init(ZombieAbilityInstance<?> instance) {

    }

    @Override
    public String getKey() {
        return KEY;
    }

    public enum PathStructure {
        SITU(0, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.AIR),
                Pack.of(0, 0, BlockType.AIR)),
                0, 0),
        FORWARD(1, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.AIR),
                Pack.of(0, 0, BlockType.AIR),
                Pack.of(1, 1, BlockType.AIR),
                Pack.of(1, 0, BlockType.AIR),
                Pack.of(1, -1, BlockType.SOLID)
        ), 1, 0),
        FORWARD_UP(2, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.AIR),
                Pack.of(0, 0, BlockType.AIR),
                Pack.of(0, 2, BlockType.AIR),
                Pack.of(1, 2, BlockType.AIR),
                Pack.of(1, 1, BlockType.AIR),
                Pack.of(1, -1, BlockType.SOLID),
                Pack.of(1, 0, BlockType.SOLID)
        ), 1, 1),
        FORWARD_DOWN(3, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.AIR),
                Pack.of(0, 0, BlockType.AIR),
                Pack.of(0, -2, BlockType.SOLID),
                Pack.of(1, 1, BlockType.AIR),
                Pack.of(1, 0, BlockType.AIR),
                Pack.of(1, -1, BlockType.AIR),
                Pack.of(1, -2, BlockType.SOLID)
        ), 1, -1),
        UP(4, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.AIR),
                Pack.of(0, 2, BlockType.AIR),
                Pack.of(0, 0, BlockType.SOLID)
        ), 0, 1),
        DOWN(5, Pack.ofList(
                Pack.of(0, 1, BlockType.AIR),
                Pack.of(0, 0, BlockType.AIR),
                Pack.of(0, -2, BlockType.SOLID),
                Pack.of(0, -1, BlockType.AIR),
                Pack.of(0, -3, BlockType.SOLID)
        ), 0, -1);

        public final int ID;
        public final int END_X;
        public final int END_Y;
        private final ArrayList<Pack> blocks;

        PathStructure(int id, ArrayList<Pack> blocks, int endX, int endY) {
            this.blocks = blocks;
            this.ID = id;
            this.END_X = endX;
            this.END_Y = endY;
        }

        public BlockPos getPos(int index, Direction direction, BlockPos startPos) {
            if (direction.equals(Direction.UP) || direction.equals(Direction.DOWN))
                throw new IllegalArgumentException("Illegal : " + direction.getName());
            Pack pack = this.blocks.get(index);
            return (new BlockPos(startPos)).relative(direction, pack.X).above(pack.Y);
        }

        public int maxIndex() {
            return this.blocks.size() - 1;
        }

        public boolean is(BlockType blockType) {
            return this.ID == blockType.ID;
        }

    }

    public enum BlockType {
        AIR(0), SOLID(1), OTHER(2);
        public final int ID;

        BlockType(int i) {
            this.ID = i;
        }

        public boolean is(BlockType blockType) {
            return this.ID == blockType.ID;
        }

    }

    private static class Pack {
        public final int X;
        public final int Y;
        public final BlockType BLOCK_TYPE;

        private Pack(int x, int y, BlockType type) {
            this.X = x;
            this.Y = y;
            this.BLOCK_TYPE = type;
        }

        public static Pack of(int x, int y, BlockType type) {
            return new Pack(x, y, type);
        }

        private static ArrayList<Pack> ofList(Pack... packs) {
            return (ArrayList<Pack>) List.of(packs);
        }
    }


}
