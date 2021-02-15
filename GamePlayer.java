import java.util.ArrayList;
import java.util.HashSet;

public class GamePlayer {

    private int maxDepth;

    private char playerLetter;

    public GamePlayer() {
        this.maxDepth = 0;
        this.playerLetter='0';
    }

    public GamePlayer(int maxDepth, char playerLetter){
      this.maxDepth = maxDepth;
      this.playerLetter = playerLetter;
    }

    public Move MiniMax(State st){
            return max(new State(st),0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private Move max(State state, int depth , int alpha, int beta) {

        if (state.isTerminal() || (depth == maxDepth)){
                Move lastMove = new Move (state.getLastMove().getRow() , state.getLastMove().getColumn(), state.evaluate(this.playerLetter));
                return lastMove;
        }
        char opponent;
        if(this.playerLetter == 'W'){
            opponent='B';
        }else{
            opponent='W';
        }
        HashSet<Move> moves = state.PossibleMoves(this.playerLetter);
        ArrayList<State> children = new ArrayList<State> (state.getChildren(moves, this.playerLetter));

        Move maxMove = new Move(Integer.MIN_VALUE);

        for  (State child : children) {
            
            Move move = min(child,depth + 1, alpha, beta);

            if (move.getValue() >= maxMove.getValue()){
                maxMove.setRow(child.getLastMove().getRow());
                maxMove.setColumn(child.getLastMove().getColumn());
                maxMove.setValue(move.getValue());
            }

            if (move.getValue() >= beta ){
                break;
            }
            alpha = Math.max(alpha, maxMove.getValue());
        }
        return maxMove;
     }

    private Move min(State state, int depth, int alpha, int beta) {

        if (state.isTerminal() || (depth == maxDepth)){
            Move lastMove = new Move (state.getLastMove().getRow() , state.getLastMove().getColumn(), state.evaluate(this.playerLetter));
            return lastMove;
        }
        char opponent;
        if(this.playerLetter == 'W'){
            opponent='B';
        }else{
            opponent='W';
        }
        HashSet<Move> moves = state.PossibleMoves(opponent);
        ArrayList<State> children = new ArrayList<State> (state.getChildren(moves, opponent));

        Move minMove = new Move(Integer.MAX_VALUE);

        for  (State child : children) {

            Move move = max(child,depth + 1, alpha , beta);

            if (move.getValue() <= minMove.getValue()){
                minMove.setRow(child.getLastMove().getRow());
                minMove.setColumn(child.getLastMove().getColumn());
                minMove.setValue(move.getValue());
            }
            //alpha prunning
            if(move.getValue() <= alpha){
                break;
            }
            beta= Math.min(beta, minMove.getValue());
        }
        return minMove;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public char getPlayerLetter() {
        return playerLetter;
    }

    public void setPlayerLetter(char playerLetter) {
        this.playerLetter = playerLetter;
    }
}
