package Game;

import BoardUI.BoardUIObserver;

public interface GameBoardSubject
{
    void addObserver(GameBoardObserver observer);
    void removeObserver(GameBoardObserver observer);
    void notifyObservers(Position position);
}
