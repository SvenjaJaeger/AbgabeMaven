package org.example;


import java.util.Optional;

public class Player extends Players {
    public Player(Board.Disk disk, String name) {
        super(disk, name);
    }

    @Override
    Optional<Integer> computeMove(Board board) {
        return Optional.empty();
    }

    @Override
    boolean isComputer() {
        return false;
    }


}


