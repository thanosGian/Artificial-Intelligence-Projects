import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer {

    private char playerLetter;
    private int maxDepth;

    public HumanPlayer(char playerLetter,int maxDepth){
        this.playerLetter = playerLetter;
        this.maxDepth = maxDepth;
    }

    public Move input_Move() throws IOException {
        System.out.println("Give me your move: ");
        int[] a = new int[2];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String lines = br.readLine();
        String[] strs = lines.trim().split("\\s+");
        for (int i = 0; i < strs.length; i++) {
            a[i] = Integer.parseInt(strs[i]);
        }
        return new Move(a[0],a[1]);
    }


    public char getPlayerLetter() {
        return playerLetter;
    }

    public void setPlayerLetter(char playerLetter) {
        this.playerLetter = playerLetter;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
