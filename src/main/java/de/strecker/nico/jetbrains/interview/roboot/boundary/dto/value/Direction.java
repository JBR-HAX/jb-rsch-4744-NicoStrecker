package de.strecker.nico.jetbrains.interview.roboot.boundary.dto.value;

public enum Direction {
    NORTH(0,1), EAST(1,0), SOUTH(0,-1), WEST(-1,0);
    final int x;
    final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
