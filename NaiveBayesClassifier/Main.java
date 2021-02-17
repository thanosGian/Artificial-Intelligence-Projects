
public class Main {
    public static void main(String[] args) {
          String path = "lingspam_public\\bare";
          for(int i = 1; i<10 ;i++) {
              NaiveBayes nv = new NaiveBayes();
              nv.training(path, i);
              nv.stats();
          }
    }
}
