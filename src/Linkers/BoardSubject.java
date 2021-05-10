package Linkers;

import Game.Position;

public interface BoardSubject
{
    void addObserver(Object o);
    void removeObserver(Object o);
    void notifyObservers(Position pos, int token);
}
