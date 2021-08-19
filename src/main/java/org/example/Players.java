package org.example;

import java.util.Optional;

public abstract class Players {
    final String name;
    final Board.Disk disk;

    public Players(Board.Disk disk, String name) {
        this.disk = disk;
        this.name = name;
    }

    Board.Disk getDisk() {
        return disk;
    }

    abstract boolean isComputer();

    abstract Optional<Integer> computeMove(Board board);

}