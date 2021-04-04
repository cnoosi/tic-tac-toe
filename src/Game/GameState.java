package Game;

public class GameState {
    private boolean isActive = false;
    private int currentWinner = -1;
    private int currentToken = -1;

    public GameState() {}
    public GameState(boolean isActive, int currentWinner, int currentToken)
    {
        this.isActive = isActive;
        this.currentWinner = currentWinner;
        this.currentToken = currentToken;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public void setCurrentWinner(int currentWinner)
    {
        this.currentWinner = currentWinner;
    }

    public void setCurrentToken(int currentToken)
    {
        this.currentToken = currentToken;
    }

    public boolean getIsActive() {return this.isActive;}
    public int getCurrentWinner() {return this.currentWinner;}
    public int getCurrentToken() {return this.currentToken;}
}
