package Linkers;

import Game.Position;

public interface BoardObserver
{
    void update(Position pos, int token);
}
