package Game;
import Networking.ClientConnection;
import Networking.GameProcess;
import Networking.Position;

public class GameHistory {

        private String gameId;
        private int player1Id;
        private int player2Id;
        private int winnerToken;
        private long startTime;
        private long endTime;
        private int startingPlayerId;

        public GameHistory(){
                gameId = "-1";
                player1Id = -1;
                player2Id = -1;
                winnerToken = -2;
                startTime = 0L;
                endTime = 0L;
                startingPlayerId = -1;
        }


        public GameHistory(String gameId, long startTime, long endTime,int player1Id, int player2Id, int startingPlayerId, int winnerToken){
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

        public void saveGameHistory(GameProcess gamep){
                Game game = gamep.getGame();
                ArrayList<Position> moves = gamep.GetMoves();
                GameProcess startTime = gamep.getStartTime();
                GameProcess endTime = gamep.getEndTime();
                Pair<ClientConnection, ClientConnection> players = gamep.getPlayers();
                Game 

        }
}
