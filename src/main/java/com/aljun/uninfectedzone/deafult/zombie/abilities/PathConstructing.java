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
                Pack.of(0, 1, BlockType.EMPTY),
                Pack.of(0, 0, BlockType.EMPTY)),
                0, 0),
        FORWARD(1, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.EMPTY),
                Pack.of(0, 0, BlockType.EMPTY),
                Pack.of(1, 1, BlockType.EMPTY),
                Pack.of(1, 0, BlockType.EMPTY),
                Pack.of(1, -1, BlockType.SOLID)
        ), 1, 0),
        FORWARD_UP(2, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.EMPTY),
                Pack.of(0, 0, BlockType.EMPTY),
                Pack.of(0, 2, BlockType.EMPTY),
                Pack.of(1, 2, BlockType.EMPTY),
                Pack.of(1, 1, BlockType.EMPTY),
                Pack.of(1, -1, BlockType.SOLID),
                Pack.of(1, 0, BlockType.SOLID)
        ), 1, 1),
        FORWARD_DOWN(3, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.EMPTY),
                Pack.of(0, 0, BlockType.EMPTY),
                Pack.of(0, -2, BlockType.SOLID),
                Pack.of(1, 1, BlockType.EMPTY),
                Pack.of(1, 0, BlockType.EMPTY),
                Pack.of(1, -1, BlockType.EMPTY),
                Pack.of(1, -2, BlockType.SOLID)
        ), 1, -1),
        UP(4, Pack.ofList(
                Pack.of(0, -1, BlockType.SOLID),
                Pack.of(0, 1, BlockType.EMPTY),
                Pack.of(0, 2, BlockType.EMPTY),
                Pack.of(0, 0, BlockType.SOLID)
        ), 0, 1),
        DOWN(5, Pack.ofList(
                Pack.of(0, 1, BlockType.EMPTY),
                Pack.of(0, 0, BlockType.EMPTY),
                Pack.of(0, -2, BlockType.SOLID),
                Pack.of(0, -1, BlockType.EMPTY),
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

        public BlockPos getPos(int index, Direction direction, BlockPos selfPos) {
            if (direction.equals(Direction.UP) || direction.equals(Direction.DOWN))
                throw new IllegalArgumentException("Illegal : " + direction.getName());
            Pack pack = this.blocks.get(index);
            return (new BlockPos(selfPos)).relative(direction, pack.X).above(pack.Y);
        }

        public int maxIndex() {
            return this.blocks.size() - 1;
        }

        public boolean is(PathStructure structure) {
            return this.ID == structure.ID;
        }

        public BlockPos getEndPos(Direction direction, BlockPos selfPos) {
            if (direction.equals(Direction.UP) || direction.equals(Direction.DOWN))
                throw new IllegalArgumentException("Illegal : " + direction.getName());
            return (new BlockPos(selfPos)).relative(direction, END_X).above(END_Y);
        }

        public BlockType getType(int index) {
            return this.blocks.get(index).BLOCK_TYPE;
        }
    }

    public enum BlockType {

        EMPTY(0), SOLID(1), OTHER(2);

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

        public boolean is(Style style) {
            return this.ID == style.ID;
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
            return new ArrayList<>(List.of(packs));
        }
    }

    public static class PathConstructingInstance extends ZombieAbilityInstance<PathConstructing> {
        private final Mob mob;

        public PathConstructingInstance(PathConstructing ability, ZombieMainGoal main) {
            super(ability, main);
            this.mob = this.getZombie();
        }

        @Override
        public void tick() {

        }

        public PathPack create(BlockPos selfPos, BlockPos buildTargetPos) {
            return this.create(selfPos, buildTargetPos, Style.NORMAL);
        }

        private PathPack create(BlockPos self, BlockPos target, Style style) {
            Direction horizontalDirection = this.getHorizontalDirectionToTarget(self, target);
            Direction verticalDirection = this.getVerticalDirectionToTarget(self, target);
            PathStructure pathStructure = PathStructure.SITU;
            if (style == Style.NORMAL) {
                if (horizontalDirection != null) {
                    if (verticalDirection == null) {
                        pathStructure = PathStructure.FORWARD;
                    } else if (verticalDirection.equals(Direction.UP)) {
                        pathStructure = PathStructure.FORWARD_UP;
                    } else if (verticalDirection.equals(Direction.DOWN)) {
                        pathStructure = PathStructure.FORWARD_DOWN;
                    }
                } else {
                    if (verticalDirection == null) {
                        pathStructure = PathStructure.FORWARD;
                    } else if (verticalDirection.equals(Direction.UP)) {
                        pathStructure = PathStructure.UP;
                    } else if (verticalDirection.equals(Direction.DOWN)) {
                        pathStructure = PathStructure.DOWN;
                    }
                }
            } else if (style.is(Style.JUMP)) {
                int x = self.getX() - target.getX();
                int z = self.getZ() - target.getZ();
                if (verticalDirection == null) {
                    pathStructure = PathStructure.FORWARD;
                } else if ((x ^ 2 + z ^ 2) <= 100) {
                    if (verticalDirection.equals(Direction.UP)) {
                        pathStructure = PathStructure.UP;
                    } else if (verticalDirection.equals(Direction.DOWN)) {
                        pathStructure = PathStructure.DOWN;
                    }
                } else {
                    if (horizontalDirection != null) {
                        if (verticalDirection.equals(Direction.UP)) {
                            pathStructure = PathStructure.FORWARD_UP;
                        } else if (verticalDirection.equals(Direction.DOWN)) {
                            pathStructure = PathStructure.FORWARD_DOWN;
                        }
                    } else {
                        if (verticalDirection.equals(Direction.UP)) {
                            pathStructure = PathStructure.UP;
                        } else if (verticalDirection.equals(Direction.DOWN)) {
                            pathStructure = PathStructure.DOWN;
                        }
                    }
                }
            }
            return new PathPack(horizontalDirection, pathStructure);
        }

        private Direction getHorizontalDirectionToTarget(BlockPos self, BlockPos target) {


            int relativeX = target.getX() - self.getX();
            int relativeY = target.getY() - self.getY();
            int relativeZ = target.getZ() - self.getZ();

            Direction direction;

            if (-1 <= relativeX && relativeX <= 1 && -1 <= relativeZ && relativeZ <= 1 && relativeY != 0) {
                if ((relativeX == 0 && relativeZ == -1) || (relativeX == -1 && relativeZ == -1)) {
                    direction = Direction.EAST;
                } else if (relativeX == -1) {
                    direction = Direction.NORTH;
                } else if ((relativeX == 1 && relativeZ == 1) || (relativeX == 0 && relativeZ == 1)) {
                    direction = Direction.WEST;
                } else if (relativeX == 1) {
                    direction = Direction.SOUTH;
                } else {
                    direction = null;
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

        @Nullable
        private Direction getVerticalDirectionToTarget(BlockPos self, BlockPos target) {
            int relativeY = target.getY() - self.getY();
            if (relativeY > 0) return Direction.UP;
            else if (relativeY < 0) return Direction.DOWN;
            else return null;
        }

        public static class PathPack {
            public final PathStructure pathStructure;
            public final Direction horizontalDirection;

            public PathPack(Direction horizontalDirection, PathStructure pathStructure) {
                this.pathStructure = pathStructure;
                this.horizontalDirection = horizontalDirection;
            }
        }
    }
}
