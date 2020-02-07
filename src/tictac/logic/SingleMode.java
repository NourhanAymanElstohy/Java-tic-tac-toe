package tictac.logic;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import tictac.database.*;
import tictac.ui.GameBodyB;
import java.util.*;

public class SingleMode extends Game {
    private int level;
    public SingleMode(boolean isRecorded, Player oppenent, User user, char myMark,int level, GameBodyB ui) {
        super(isRecorded, Constants.SOLO, oppenent, user, myMark, ui);
        this.level = level;
    }

    /**
     * this function return a new board with computer movement depending on the
     * min and max algorithm
     *
     * @param board : Board
     * @return bestChild : Board
     */
    private Board findBestMove(Board board) {
        ArrayList<Position> positions = board.getFreePositions();
        Board bestChild = null;
        int previous = Integer.MIN_VALUE;
        for (Position p : positions) {
            Board child = new Board(board, p, oppenentMark);
            int current = min(child);
            if (current > previous) {
                bestChild = child;
                previous = current;
            }
        }
        return bestChild;
    }

    public int max(Board board) {
        GameState gameState = board.getGameState(myMark, oppenentMark);
        if (null != gameState) {
            switch (gameState) {
                case OppWin:
                    return 1;
                case YouWin:
                    return -1;
                case Draw:
                    return 0;
                default:
                    break;
            }
        }
        ArrayList<Position> positions = board.getFreePositions();
        int best = Integer.MIN_VALUE;
        for (Position p : positions) {
            Board b = new Board(board, p, oppenentMark);
            int move = min(b);
            if (move > best) {
                best = move;
            }
        }
        return best;
    }

    public int min(Board board) {
        GameState gameState = board.getGameState(myMark, oppenentMark);
        if (null != gameState) {
            switch (gameState) {
                case OppWin:
                    return 1;
                case YouWin:
                    return -1;
                case Draw:
                    return 0;
                default:
                    break;
            }
        }
        ArrayList<Position> positions = board.getFreePositions();
        int best = Integer.MAX_VALUE;
        for (Position p : positions) {
            Board b = new Board(board, p, myMark);
            int move = max(b);
            if (move < best) {
                best = move;
            }
        }
        return best;
    }

    public Position getRandomMove() {
        Random rand = new Random();
        Position pos;
        int x;
        int y;
        // Generate random move
        do {
            x = rand.nextInt(3);
            y = rand.nextInt(3);
        } while (board.getBoard()[x][y] == Constants.Cross || board.getBoard()[x][y] == Constants.Circle);
        pos = new Position(x, y);
        return pos;
    }
    public Position getWinningMove() {
        Position pos = null;
        int[][] preferredMoves = {{1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2}, {0, 1}, {1, 0}, {1, 2}, {2, 1}};
        for(int[] move : preferredMoves){
            if(board.getBoard()[move[0]][move[1]] != Constants.Cross &&board.getBoard()[move[0]][move[1]]!= Constants.Circle ){
                pos = new Position(move[0], move[1]);
            }
         }
        return pos;
    }
    /**
     * overriding the abstract function play to suit single mode playing logic
     *
     * @param x : integer (the row number in the board)
     * @param y : integer (the column number in the board)
     */
    @Override
    public void play(int x, int y) {
        int result = 4;
        if (!gameEnded) {
            Position position;
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
                if(level == Constants.EASY){
                     position = getRandomMove();
                     board = new Board(board, position, oppenentMark);
                      ui.setText(buttons[position.getRow()][position.getColumn()], oppenentMark);
                }
                else{
                    board = findBestMove(board);
                    drawBoardOnButtons(board);
                }  
                myTurn = !myTurn;   
                result = evaluateGame();
            }
        }
        showResult(result);
    }

}
