package com.aljun.uninfectedzone.deafult.zombie.abilities;

import com.aljun.uninfectedzone.core.utils.RandomHelper;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbility;
import com.aljun.uninfectedzone.core.zombie.abilities.ZombieAbilityInstance;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PathConstructing extends ZombieAbility {
    public static String KEY = "path_constructing";

    @Override
    protected ZombieAbilityInstance<? extends ZombieAbility> create(ZombieMainGoal mainGoal) {
        return new PathConstructingInstance(this, mainGoal);
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

    public enum Style {

        NORMAL(0), JUMP(1);

        public final int ID;

        Style(int id) {
            this.ID = id;
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

    public class PathConstructingInstance extends ZombieAbilityInstance<PathConstructing> {
        private final Mob mob;

        public PathConstructingInstance(PathConstructing ability, ZombieMainGoal main) {
            super(ability, main);
            this.mob = this.getZombie();
        }

        @Override
        public void tick() {

        }

        @Nullable
        private Direction getVerticalDirectionToTarget(BlockPos target) {
            int relativeY = target.getY() - this.getZombiePos().getY();
            if (relativeY > 0) return Direction.UP;
            else if (relativeY < 0) return Direction.DOWN;
            else return null;
        }

        private BlockPos getZombiePos() {
            return this.mob.getOnPos();
        }

        private Direction getHorizontalDirectionToTarget(BlockPos target) {

            BlockPos self = this.getZombiePos();

            int relativeX = target.getX() - self.getX();
            int relativeY = target.getY() - this.getZombiePos().getY();
            int relativeZ = target.getZ() - self.getZ();

            Direction direction;

            if (-1 <= relativeX && relativeX <= 1 && -1 <= relativeZ && relativeZ <= 1 && !(-1 <= relativeY && relativeY <= 1)) {
                if ((relativeX == 0 && relativeZ == -1) || (relativeX == -1 && relativeZ == -1)) {
                    direction = Direction.EAST;
                } else if (relativeX == -1) {
                    direction = Direction.NORTH;
                } else if ((relativeX == 1 && relativeZ == 1) || (relativeX == 0 && relativeZ == 1)) {
                    direction = Direction.WEST;
                } else if (relativeX == 1) {
                    direction = Direction.SOUTH;
                } else {
                    direction = RandomHelper.randomHorizontalDirection();
                }
            } else if (relativeX == 0 && -1 <= relativeY && relativeY <= 1 && relativeZ == 0) {
                direction = null;
            } else if (relativeZ <= relativeX && relativeZ < -relativeX) {
                direction = Direction.SOUTH;
            } else if (relativeZ > relativeX && relativeZ <= -relativeX) {
                direction = Direction.EAST;
            } else if (relativeZ >= relativeX && relativeZ > -relativeX) {
                direction = Direction.NORTH;
            } else if (relativeZ < relativeX && relativeZ >= -relativeX) {
                direction = Direction.WEST;
            } else {
                direction = RandomHelper.randomHorizontalDirection();
            }

            return direction;
        }
    }
}
