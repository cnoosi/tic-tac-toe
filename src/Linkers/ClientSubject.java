package Linkers;

import Game.Position;

public interface ClientSubject
{
    void addClientObserver(Object o);
    void removeClientObserver(Object o);
    void notifyClientObservers(Position pos);
}
