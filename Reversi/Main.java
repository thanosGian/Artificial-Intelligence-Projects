import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void  main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.println("pc vs pc press 1\nhuman vs pc press 2 ");
        int number = s.nextInt();

        if (number == 1) {
        GamePlayer black = new GamePlayer(3,'B');
        GamePlayer white = new GamePlayer(5,'W');
//      PlayerPc black= new PlayerPc(5,'B');
//      PlayerPc white= new PlayerPc(5,'W');
        State state = new State();
        state.print();

        while(!state.isTerminal()){
            System.out.println();
            switch (state.getLastLetterPlayed()){
                case State.Black :
                    System.out.println(state.PossibleMoves('W'));
                    System.out.println(" White plays");
                    Move WhiteMove = white.MiniMax(state);
                    if(state.isValidMove(WhiteMove)){
                    state.movePiece(WhiteMove , 'W');
                    }else{
                    System.out.println("white cant play\tpassing the move to black!");
                    state.setLastLetterPlayed('W');
                    }
                    break;
                case State.White:
                    System.out.println(state.PossibleMoves('B'));
                    System.out.println("Black plays");
                    Move BlackMove = black.MiniMax(state);
                    if(state.isValidMove(BlackMove)){
                    state.movePiece(BlackMove, 'B');
                  }else{
                    System.out.println("black cant play\tpassing the move to white!");
                    state.setLastLetterPlayed('B');
                  }
                    break;
                default:
                    break;
            }
            state.print();
        }
        state.winner();
        } else if(number == 2) {

            Scanner sc = new Scanner(System.in);

            System.out.println("Maximum depth: ");
            int maxDepth = sc.nextInt();

            System.out.println("Press B for black or W for white: ");
            char playerLetter = Character.toUpperCase(sc.next().charAt(0));

            HumanPlayer human = new HumanPlayer(playerLetter, maxDepth);
            GamePlayer pc = new GamePlayer();
            pc.setMaxDepth(maxDepth);

            if (playerLetter == 'W') {
                pc.setPlayerLetter('B');
            } else if (playerLetter == 'B') {
                pc.setPlayerLetter('W');
            }
            State state = new State();
            state.print();

            while (!state.isTerminal()) {

                System.out.println();

                switch (state.getLastLetterPlayed()) {
                    case State.Black://paizei o White
                        if (human.getPlayerLetter() == 'W') {
                            System.out.println("White plays");
                            while (true) {
                                HashSet<Move> moves = state.PossibleMoves('W');
                                if (moves.isEmpty()) {
                                    System.out.println("White cant play\t,passing the move to Black!");
                                    state.setLastLetterPlayed('W');
                                    break;
                                }
                                System.out.println(moves);
                                state.showMoves(moves, 'W');
                                Move humanMove = human.input_Move();
                                if (state.isValidMove(humanMove)) {
                                    state.movePiece(humanMove, 'W');
                                    break;
                                } else {
                                    System.out.println("Invalid input.Try again!");
                                }
                            }
                        } else {
                            System.out.println("White plays");
                            Move pcMove = pc.MiniMax(state);
                            if (state.isValidMove(pcMove)) {
                                state.movePiece(pcMove, 'W');
                            } else {
                                System.out.println("White cant play\t,passing the move to Black!");
                                state.setLastLetterPlayed('W');
                            }
                        }
                        break;
                    case State.White://paizei o black
                        if (human.getPlayerLetter() == 'B') {
                            System.out.println("Black plays");
                            while (true) {
                                HashSet<Move> moves = state.PossibleMoves('B');
                                if (moves.isEmpty()) {
                                    System.out.println("Black cant play\t,passing the move to White!");
                                    state.setLastLetterPlayed('B');
                                    break;
                                }
                                System.out.println(moves);
                                state.showMoves(state.PossibleMoves('B'), 'B');
                                Move humanMove = human.input_Move();
                                System.out.println(humanMove);
                                if (state.isValidMove(humanMove)) {
                                    state.movePiece(humanMove, 'B');
                                    break;
                                } else {
                                    System.out.println("Invalid input.Try again!");
                                }
                            }
                        } else {
                            System.out.println("Black plays");
                            Move pcMove = pc.MiniMax(state);
                            if (state.isValidMove(pcMove)) {
                                state.movePiece(pcMove, 'B');
                            } else {
                                System.out.println("Black cant play\t,passing the move to White!");
                                state.setLastLetterPlayed('B');
                            }
                        }
                        break;
                    default:
                        break;

                }
                state.print();
            }
            state.winner();
        }
    }
}
