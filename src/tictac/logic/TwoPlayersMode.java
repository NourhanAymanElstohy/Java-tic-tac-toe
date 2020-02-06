package tictac.logic;
import tictac.database.*;
import tictac.ui.GameBodyB;

public class TwoPlayersMode extends Game {
    public TwoPlayersMode(boolean isRecorded, Player oppenent, User user, char myMark, GameBodyB ui) {
        super(isRecorded, Constants.DUAL, oppenent, user, myMark, ui);
    }
    /**
     * overriding the abstract function play to suit two players mode playing logic
     * @param x : integer (the row number in the board)
     * @param y : integer (the column number in the board)
     */
    @Override
    public void play(int x, int y) {
        System.out.println(board);
        int result = 4;
        if (!gameEnded) {
            Position position = null;
            if (myTurn) {
                position = makeMove(x, y);
                if (position != null) {
                    board = new Board(board, position, myMark);
                    ui.setText(buttons[x][y], myMark);
                    recordStep(x, y, Constants.MINE);
                    myTurn = !myTurn;
                    result = evaluateGame();
                }
            }
            if (!myTurn && !board.getFreePositions().isEmpty()) {
                position = makeMove(x, y);
                if (position != null) {
                    board = new Board(board, position, oppenentMark);
                    ui.setText(buttons[x][y], oppenentMark);
                    recordStep(x, y, Constants.OPPENENT);
                    myTurn = !myTurn;
                    result = evaluateGame();
                } 
            }
        }
        showResult(result);
    }
}
