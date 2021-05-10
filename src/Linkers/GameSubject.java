package Linkers;

import Game.Position;

public interface GameSubject
{
    void addClientObserver(Object o);
    void removeClientObserver(Object o);
    void notifyClientObservers(Position pos);
    void notifyClientObserver(Position pos);
}
