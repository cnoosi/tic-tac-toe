package Game;
import Networking.GameProcess;

public class GameHistory {

        private String gameId;
        private int player1Id;
        private int player2Id;
        private int winnerToken;
        private long startTime;
        private long endTime;
        private int startingPlayerId;



        public GameHistory(String gameId, int player1Id, int player2Id, int winnerToken, long startTime, long endTime, int startingPlayerId){
                setGameId(gameId);
                setPlayer1Id(player1Id);
                setPlayer2Id(player2Id);
                setWinnerToken(winnerToken);
                setStartTime(startTime);
                setEndTime(endTime);
                setStartingPlayerId(startingPlayerId);

        }

        public String getGameId() {
                return gameId;
        }

        public void setGameId(String gameId) {
                this.gameId = gameId;
        }

        public int getPlayer1Id() {
                return player1Id;
        }

        public void setPlayer1Id(int player1Id) {
                this.player1Id = player1Id;
        }

        public int getPlayer2Id() {
                return player2Id;
        }

        public void setPlayer2Id(int player2Id) {
                this.player2Id = player2Id;
        }

        public int getWinnerToken() {
                return winnerToken;
        }

        public void setWinnerToken(int winnerToken) {
                this.winnerToken = winnerToken;
        }

        public long getStartTime() {
                return startTime;
        }

        public void setStartTime(long startTime) {
                this.startTime = startTime;
        }

        public long getEndTime() {
                return endTime;
        }

        public void setEndTime(long endTime) {
                this.endTime = endTime;
        }

        public int getStartingPlayerId() {
                return startingPlayerId;
        }

        public void setStartingPlayerId(int startingPlayerId) {
                this.startingPlayerId = startingPlayerId;
        }
}
