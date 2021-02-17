import java.util.ArrayList;
import java.util.HashSet;
import java.lang.*;

public class State {

    public static final char Black = 'B';
    public static final char White = 'W';
    public static final int Empty = '_';

    private Move lastMove;

    private char lastLetterPlayed;

    private int board_size;

    public char[][] board;

    public char[][] getBoard() {
        return board;
    }

    public State() {
        this.board_size = 8;
        this.lastMove = new Move();
        this.lastLetterPlayed = 'W';
        this.board = new char[board_size][board_size];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 3 && j == 3 || i == 4 && j == 4) {
                    this.board[i][j] = White;
                } else if (i == 3 && j == 4 || i == 4 && j == 3) {
                    this.board[i][j] = Black;
                } else {
                    this.board[i][j] = Empty;
                }
            }
        }
    }

    public State(State st) {
        this.board_size = st.board_size;
        this.lastMove = st.lastMove;
        this.lastLetterPlayed = st.getLastLetterPlayed();
        this.board = new char[this.board_size][this.board_size];
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                this.board[i][j] = st.board[i][j];
            }
        }
    }

    public HashSet<Move> PossibleMoves(char player) {
        HashSet<Move> moves = new HashSet<>();
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                if (this.board[i][j] == '_') {
                    Move possibleMove = new Move(i, j);
                    boolean found;
                    int posX;
                    int posY;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (x == 0 && y == 0) {
                                continue;
                            }
                            posX = possibleMove.getRow() + x;
                            posY = possibleMove.getColumn() + y;
                            found = false;
                            if (posX == -1 || posX > 7 || posY == -1 || posY > 7) {
                                continue;
                            }
                            if (this.board[posX][posY] == player || this.board[posX][posY] == '_') {
                                continue;
                            }
                            while (!found) {
                                posX += x;
                                posY += y;
                                if (posX == -1 || posX > 7 || posY == -1 || posY > 7 || this.board[posX][posY] == '_') {
                                    found = true;
                                } else if (this.board[posX][posY] == player) {
                                    moves.add(possibleMove);
                                    found = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    public void movePiece(Move move,char player){
        if(!isValidMove(move)) return;
        this.setLastLetterPlayed(player);
        int row = move.getRow();
        int column = move.getColumn();
        this.lastMove = new Move(row,column);
        this.board[row][column] = player;
        int posX;
        int posY;
        boolean stop;
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <=1; y++){
                if(x==0 && y==0) continue;
                posX = row + x;
                posY = column + y;
                stop = false;
                if (posX == -1 || posX > 7 || posY == -1 || posY > 7) {
                    continue;
                }
                if (this.board[posX][posY] == player || this.board[posX][posY] == '_') {
                    continue;
                }
                while(!stop){
                    posX +=x;
                    posY +=y;
                    if(posX == -1 || posX > 7 || posY == -1 || posY > 7 || this.board[posX][posY] == '_'){
                        stop = true;
                    }else if(this.board[posX][posY] == player){
                        posX -=x;
                        posY -=y;
                        while(!(posX == row && posY ==column)) {
                            this.board[posX][posY] = player;
                            posX -= x;
                            posY -= y;
                        }
                        stop = true;
                    }
                }
            }//end of inner for loop
        }//end of for loop
    }//end of method movePiece



    public boolean isValidMove(Move move) {
        if (move.getRow() == -1 || move.getColumn() == -1 || move.getRow() > 7 || move.getColumn() > 7) {
            return false;
        }
        if (this.board[move.getRow()][move.getColumn()] != '_') {
            return false;
        }
        switch (getLastLetterPlayed()) {
            case State.Black:
                HashSet<Move> movesWhite = new HashSet<>(PossibleMoves('W'));
                if (movesWhite.contains(move)) {
                    return true;
                } else {
                    return false;
                }
            case State.White:
                HashSet<Move> movesBlack = new HashSet<>(PossibleMoves('B'));
                if (movesBlack.contains(move)) {
                    return true;
                } else {
                    return false;
                }
        }
        return true;
    }

    public ArrayList<State> getChildren(HashSet<Move> pieces, char player) {
        ArrayList<State> children = new ArrayList<>();
        for (Move p : pieces) {
            State child = new State(this);
            child.movePiece(p, player);
            children.add(child);
        }
        return children;
    }

    public int numberOfBlack() {
        int number = 0;
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                if (this.board[i][j] == 'B') number++;
            }
        }
        return number;
    }

    public int numberOfWhite() {
        int number = 0;
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                if (this.board[i][j] == 'W') number++;
            }
        }
        return number;
    }
    private int helpfunct(){
        int num=0;
        for(int i=0;i<getBoard_size();i++){
            for(int j=0;j<getBoard_size();j++){
                if(this.board[i][j] == '_'){
                    num += 1;
                }
            }
        }
        return num;
    }

    public void winner() {
        if (isTerminal()) {
            int num=helpfunct();
            int score = heuristic_number_of_pieces('B');
            if (score > 0) {
                System.out.println("the winner is black with score " + (numberOfBlack()+num));
            } else if (score < 0) {
                System.out.println("the winner is white with score " + (numberOfWhite()+num));
            } else {
                System.out.println("its a draw!!!!!");
            }
        }
    }

    public boolean isTerminal() {
        int counter = 0;
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                if (this.board[i][j] == 'W' || this.board[i][j] == 'B') counter++;
            }
        }

        if (counter == this.board_size * this.board_size) return true;

        HashSet<Move> p1 = PossibleMoves('W'); // Possible moves for the White
        HashSet<Move> p2 = PossibleMoves('B');// Possible moves for the Black

        if (p1.isEmpty() && p2.isEmpty()) {
            return true;
        }
        return false;
    }

    public void print() {
        System.out.println("------------------------------------");
        System.out.print("\t");
        for(int i =0; i<8; i++){
            System.out.print(i + "\t");
        }
        System.out.println();
        for (int i = 0; i < this.board_size; i++) {
            System.out.print(i + "\t");
            for (int j = 0; j < this.board_size; j++) {
                System.out.print(this.board[i][j]);
                if (j < this.board_size - 1) {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
        System.out.println("------------------------------------");
    }

    public int evaluate(char player) {
        int score = 0;

        if (isTerminal()) {
            int sscore = heuristic_number_of_pieces(player);
            if (sscore > 0) {
                score = 10000;
            } else if (sscore < 0) {
                score = -10000;
            }
        }
        int number_of_pieces = heuristic_number_of_pieces(player);
        int pieces_on_edges = heuristic_pieces_on_edges(player);
        int pieces_on_sides = heuristic_pieces_on_sides(player);
        int mobility = heuristic_mobility(player);
        int closeCorners = cornerCloseness(player);
        int frontierDisks = FrontierDisks(player);
        int interiorDisks = InteriorDisks(player);
        
        if(numberOfBlack()+numberOfWhite()>=40) {
            score += 1000*number_of_pieces + 50*pieces_on_sides + 100*pieces_on_edges + 120*mobility + 20*interiorDisks - 5*closeCorners - 10*frontierDisks;
        }else{
            score += 50*pieces_on_sides + 100*pieces_on_edges + 120*mobility + 20*interiorDisks - 5*closeCorners - 10*frontierDisks ;
        }
        return score;
    }

    private int heuristic_number_of_pieces(char player) {
        int Wpieces = numberOfWhite();
        int Bpieces = numberOfBlack();
        if(player == 'B')
            return Bpieces - Wpieces;
        else
            return Wpieces - Bpieces;
    }

    public int heuristic_pieces_on_edges(char player) {
        // For the edges the coordinates are: (0,0) , (0,7) , (7,0) , (7,7)
        int counterBlack = 0;
        int counterWhite = 0;
        for (int i = 0; i < this.board_size; i = i + 7) {
            for (int j = 0; j < this.board_size; j = j + 7) {
                if (board[i][j] == 'B') {
                    counterBlack += 4;
                } else if (board[i][j] == 'W') {
                    counterWhite += 4;
                }
            }
        }
        if(player == 'B')
            return counterBlack - counterWhite;
        else
            return counterWhite - counterBlack;
    }

    private int heuristic_pieces_on_sides(char player) {
        int counterBlack = 0;
        int counterWhite = 0;

        for (int i = 1; i < 7; i++) {
            if (this.board[i][0] == 'B' || this.board[i][7] == 'B') {
                counterBlack += 2;
            } else if (this.board[i][0] == 'W' || this.board[i][7] == 'W') {
                counterWhite += 2;
            }

            for (int j = 1; j < 7; j++) {
                if (this.board[0][j] == 'B' || this.board[7][j] == 'B') {
                    counterBlack += 2;
                } else if (this.board[0][j] == 'W' || this.board[7][j] == 'W') {
                    counterWhite += 2;
                }
            }
        }
        if(player == 'B')
            return counterBlack - counterWhite;
        else
            return counterWhite - counterBlack;
    }

    public int heuristic_mobility(char player){
        int counterBlack = 0;
        int counterWhite = 0;

        HashSet<Move> movesBlack = PossibleMoves('B');
        HashSet<Move> movesWhite = PossibleMoves('W');
        counterBlack = movesBlack.size();
        counterWhite = movesWhite.size();

        if(player == 'B'){
            return counterBlack - counterWhite;
        }else{
            return counterWhite - counterBlack;
        }
    }

    public int cornerCloseness(char player){
        int counterBlack = 0;
        int counterWhite = 0;

        if(this.board[0][0] == '_'){
            if(this.board[0][1] == 'B') counterBlack++;
            else if(this.board[0][1] == 'W') counterWhite++;
            if(this.board[1][1] == 'B') counterBlack++;
            else if(this.board[1][1] == 'W') counterWhite++;
            if(this.board[1][0] == 'B') counterBlack++;
            else if(this.board[1][0] == 'W') counterWhite++;
        }
        if(this.board[0][7] == '_'){
            if(this.board[0][6] == 'B') counterBlack++;
            else if(this.board[0][6] == 'W') counterWhite++;
            if(this.board[1][6] == 'B') counterBlack++;
            else if(this.board[1][6] == 'W') counterWhite++;
            if(this.board[1][7] == 'B') counterBlack++;
            else if(this.board[1][7] == 'W') counterWhite++;
        }
        if(this.board[7][0] == '_'){
            if(this.board[7][1] == 'B') counterBlack++;
            else if(this.board[7][1] == 'W') counterWhite++;
            if(this.board[6][1] == 'B') counterBlack++;
            else if(this.board[6][1] == 'W') counterWhite++;
            if(this.board[6][0] == 'B') counterBlack++;
            else if(this.board[6][0] == 'W') counterWhite++;
        }
        if(this.board[7][7] == '_'){
            if(this.board[6][7] == 'B') counterBlack++;
            else if(this.board[6][7] == 'W') counterWhite++;
            if(this.board[6][6] == 'B') counterBlack++;
            else if(this.board[6][6] == 'W') counterWhite++;
            if(this.board[7][6] == 'B') counterBlack++;
            else if(this.board[7][6] == 'W') counterWhite++;
        }
        if(player == 'B')
            return counterBlack - counterWhite;
        else
            return counterWhite - counterBlack;
    }
	
    public int FrontierDisks(char player){
        int counterBlack=0,counterWhite=0;
        for(int i = 0; i<this.board_size ;i++){
            for(int j=0; j<this.board_size ;j++){
                if(this.board[i][j] == '_') continue;
                int posX = i;
                int posY = j;
                outterLoop:
                for(int x = -1; x <= 1; x++){
                    for(int y = -1; y <=1; y++){
                        if (posX+x == -1 || posX+x > 7 || posY+y == -1 || posY+y > 7 || x == 0 || y == 0) {
                            continue;
                        }
                        if(this.board[posX+x][posY+y] == '_'){
                            if(this.board[posX][posY] == 'B'){
                                counterBlack++;
                                break outterLoop;
                            } else{
                                counterWhite++;
                                break outterLoop;
                            }
                        }
                    }
                }
            }
        }
        if(player == 'B')
            return counterBlack - counterWhite;
        else
            return counterWhite - counterBlack;
    }

    public int InteriorDisks(char player){
        int counterBlack=0,counterWhite=0;
        for(int i = 0; i<this.board_size ;i++){
            for(int j=0; j<this.board_size ;j++) {
                if (this.board[i][j] == '_') continue;
                int posX = i;
                int posY = j;
                int counter=0;
                outterLoop:
                for(int x = -1; x <= 1; x++){
                    for(int y = -1; y <=1; y++){
                        if (posX+x == -1 || posX+x > 7 || posY+y == -1 || posY+y > 7) {
                            counter++;
                            continue;
                        }
                        if(this.board[posX+x][posY+y] != '_'){
                            counter++;
                        }else{
                            break outterLoop;
                        }
                    }
                }
                if(counter == 8){
                    if(this.board[posX][posY] == 'B')
                        counterBlack++;
                    else
                        counterWhite++;
                }
            }
        }
        if(player == 'B')
            return counterBlack - counterWhite;
        else
            return counterWhite - counterBlack;
    }

    public void setLastLetterPlayed(char lastLetterPlayed) {
        this.lastLetterPlayed = lastLetterPlayed;
    }

    public char getLastLetterPlayed() {
        return lastLetterPlayed;
    }

    public int getBoard_size() {
        return board_size;
    }

    public void setBoard_size(int board_size) {
        this.board_size = board_size;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setColumn(lastMove.getColumn());
        this.lastMove.setValue(lastMove.getValue());
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void showMoves(HashSet<Move> moves,char player){
        State state=new State(this);
        for(Move move : moves){
            state.board[move.getRow()][move.getColumn()] = '*';
        }
        state.print();
    }
}