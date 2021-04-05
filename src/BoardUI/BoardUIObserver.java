package BoardUI;

import Game.Position;

public interface BoardUIObserver
{
    void onMoveExecuted(Position move);
}
