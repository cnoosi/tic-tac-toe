package BoardUI;

import Game.Position;

public interface BoardUISubject
{
    void addObserver(BoardUIObserver observer);
    void removeObserver(BoardUIObserver observer);
    void notifyObservers(Position position);
}
