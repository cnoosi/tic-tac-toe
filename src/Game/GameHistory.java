package Game;
import Networking.GameProcess;

public class GameHistory {

        private String gameId;
        private int player1Id;
        private int player2Id;
        private int winnerToken;
        private long startTime;
        private long endTime;
        private int[][] gameMoves;


        public GameHistory(String gameId, int player1Id, int player2Id, int winnerToken, long startTime, long endTime, int[][] gameMoves){
                setGameId(gameId);
                setplayer1Id(player1Id);
                setPlayer2Id(player2Id);
                setWinnerToken(winnerToken);
                setStartTime(startTime);
                setGameMoves();
        }

        public void saveGameHistory(GameProcess game){

        }

}
