package net.minecraft.server;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

public class BlockPosition extends BaseBlockPosition {

    public static final BlockPosition ZERO = new BlockPosition(0, 0, 0);
    private static final int c = 1 + MathHelper.c(MathHelper.b(30000000));
    private static final int d = BlockPosition.c;
    private static final int e = 64 - BlockPosition.c - BlockPosition.d;
    private static final int f = BlockPosition.d;
    private static final int g = BlockPosition.f + BlockPosition.e;
    private static final long h = (1L << BlockPosition.c) - 1L;
    private static final long i = (1L << BlockPosition.e) - 1L;
    private static final long j = (1L << BlockPosition.d) - 1L;

    public BlockPosition(int i, int j, int k) {
        super(i, j, k);
    }

    public BlockPosition(double d0, double d1, double d2) {
        super(d0, d1, d2);
    }

    public BlockPosition(Entity entity) {
        this(entity.locX, entity.locY, entity.locZ);
    }

    public BlockPosition(Vec3D vec3d) {
        this(vec3d.a, vec3d.b, vec3d.c);
    }

    public BlockPosition(BaseBlockPosition baseblockposition) {
        this(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
    }

    public BlockPosition a(double d0, double d1, double d2) {
        return d0 == 0.0D && d1 == 0.0D && d2 == 0.0D ? this : new BlockPosition((double) this.getX() + d0, (double) this.getY() + d1, (double) this.getZ() + d2);
    }

    public BlockPosition a(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new BlockPosition(this.getX() + i, this.getY() + j, this.getZ() + k);
    }

    public BlockPosition a(BaseBlockPosition baseblockposition) {
        return baseblockposition.getX() == 0 && baseblockposition.getY() == 0 && baseblockposition.getZ() == 0 ? this : new BlockPosition(this.getX() + baseblockposition.getX(), this.getY() + baseblockposition.getY(), this.getZ() + baseblockposition.getZ());
    }

    public BlockPosition b(BaseBlockPosition baseblockposition) {
        return baseblockposition.getX() == 0 && baseblockposition.getY() == 0 && baseblockposition.getZ() == 0 ? this : new BlockPosition(this.getX() - baseblockposition.getX(), this.getY() - baseblockposition.getY(), this.getZ() - baseblockposition.getZ());
    }

    public BlockPosition up() {
        return this.up(1);
    }

    public BlockPosition up(int i) {
        return this.shift(EnumDirection.UP, i);
    }

    public BlockPosition down() {
        return this.down(1);
    }

    public BlockPosition down(int i) {
        return this.shift(EnumDirection.DOWN, i);
    }

    public BlockPosition north() {
        return this.north(1);
    }

    public BlockPosition north(int i) {
        return this.shift(EnumDirection.NORTH, i);
    }

    public BlockPosition south() {
        return this.south(1);
    }

    public BlockPosition south(int i) {
        return this.shift(EnumDirection.SOUTH, i);
    }

    public BlockPosition west() {
        return this.west(1);
    }

    public BlockPosition west(int i) {
        return this.shift(EnumDirection.WEST, i);
    }

    public BlockPosition east() {
        return this.east(1);
    }

    public BlockPosition east(int i) {
        return this.shift(EnumDirection.EAST, i);
    }

    public BlockPosition shift(EnumDirection enumdirection) {
        return this.shift(enumdirection, 1);
    }

    public BlockPosition shift(EnumDirection enumdirection, int i) {
        return i == 0 ? this : new BlockPosition(this.getX() + enumdirection.getAdjacentX() * i, this.getY() + enumdirection.getAdjacentY() * i, this.getZ() + enumdirection.getAdjacentZ() * i);
    }

    public BlockPosition c(BaseBlockPosition baseblockposition) {
        return new BlockPosition(this.getY() * baseblockposition.getZ() - this.getZ() * baseblockposition.getY(), this.getZ() * baseblockposition.getX() - this.getX() * baseblockposition.getZ(), this.getX() * baseblockposition.getY() - this.getY() * baseblockposition.getX());
    }

    public long asLong() {
        return ((long) this.getX() & BlockPosition.h) << BlockPosition.g | ((long) this.getY() & BlockPosition.i) << BlockPosition.f | ((long) this.getZ() & BlockPosition.j);
    }

    public static BlockPosition fromLong(long i) {
        int j = (int) (i << 64 - BlockPosition.g - BlockPosition.c >> 64 - BlockPosition.c);
        int k = (int) (i << 64 - BlockPosition.f - BlockPosition.e >> 64 - BlockPosition.e);
        int l = (int) (i << 64 - BlockPosition.d >> 64 - BlockPosition.d);

        return new BlockPosition(j, k, l);
    }

    public static Iterable<BlockPosition> a(BlockPosition firstPosition, BlockPosition secondPosition) {
        return new Iterable() {
            public Iterator<BlockPosition> iterator() {
                return new AbstractIterator() {
                    private BlockPosition b = null;

                    protected BlockPosition a() {
                        if (this.b == null) {
                            this.b = firstPosition;
                            return this.b;
                        } else if (this.b.equals(secondPosition)) {
                            return (BlockPosition) this.endOfData();
                        } else {
                            int i = this.b.getX();
                            int j = this.b.getY();
                            int k = this.b.getZ();

                            if (i < secondPosition.getX()) {
                                ++i;
                            } else if (j < secondPosition.getY()) {
                                i = firstPosition.getX();
                                ++j;
                            } else if (k < secondPosition.getZ()) {
                                i = firstPosition.getX();
                                j = firstPosition.getY();
                                ++k;
                            }

                            this.b = new BlockPosition(i, j, k);
                            return this.b;
                        }
                    }

                    protected Object computeNext() {
                        return this.a();
                    }
                };
            }
        };
    }

    public static Iterable<BlockPosition.MutableBlockPosition> b(BlockPosition blockposition, BlockPosition blockposition1) {
        return new Iterable() {
            public Iterator<BlockPosition.MutableBlockPosition> iterator() {
                return new AbstractIterator() {
                    private BlockPosition.MutableBlockPosition b = null;

                    protected BlockPosition.MutableBlockPosition a() {
                        if (this.b == null) {
                            this.b = new BlockPosition.MutableBlockPosition(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                            return this.b;
                        } else if (this.b.equals(blockposition1)) {
                            return (BlockPosition.MutableBlockPosition) this.endOfData();
                        } else {
                            int i = this.b.getX();
                            int j = this.b.getY();
                            int k = this.b.getZ();

                            if (i < blockposition1.getX()) {
                                ++i;
                            } else if (j < blockposition1.getY()) {
                                i = blockposition.getX();
                                ++j;
                            } else if (k < blockposition1.getZ()) {
                                i = blockposition.getX();
                                j = blockposition.getY();
                                ++k;
                            }

                            this.b.setX(i);
                            this.b.setY(j);
                            this.b.setZ(k);

                            return this.b;
                        }
                    }

                    protected Object computeNext() {
                        return this.a();
                    }
                };
            }
        };
    }

    public BaseBlockPosition d(BaseBlockPosition baseblockposition) {
        return this.c(baseblockposition);
    }

    public static final class MutableBlockPosition extends BlockPosition {

        public void setX(int x) {
            (this).a = x;
        }

        public void setY(int y) {
            ((BaseBlockPosition) this).c = y;
        }

        public void setZ(int z) {
            ((BaseBlockPosition) this).d = z;
        }

        public MutableBlockPosition() {
            this(0, 0, 0);
        }

        public MutableBlockPosition(int i, int j, int k) {
            super(0, 0, 0);

            this.setX(i);
            this.setY(j);
            this.setZ(k);
        }

        public BlockPosition.MutableBlockPosition c(int i, int j, int k) {
            setX(i);
            setY(j);
            setZ(k);

            return this;
        }

        public BaseBlockPosition d(BaseBlockPosition baseblockposition) {
            return super.c(baseblockposition);
        }
    }
}
