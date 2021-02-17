import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NaiveBayes {

    HashSet<String> words;
    HashMap<String, Integer> hamWords;
    HashMap<String, Integer> spamWords;
    double numberOfHam;
    double numberOfSpam;
    double vocabulary;
    double N_hams;
    double N_spams;
    ArrayList<File> files;

    public NaiveBayes () {
        this.words = new HashSet<>();
        this.hamWords = new HashMap<>();
        this.spamWords = new HashMap<>();
        this.numberOfHam = 0;
        this.numberOfSpam = 0;
        this.vocabulary = 0;
        this.N_hams = 0;
        this.N_spams = 0;
        this.files = new ArrayList<>();
    }

    public void ReadTrainingSet(String path,int training) {
        int filesReadedSoFar = 0;
        File dir = new File(path);
        Scanner sc = null;
        FileComparator cmp = new FileComparator();
        ArrayList<File> filesToBeRemoved = new ArrayList<>();

        for (File fileEntry1 : dir.listFiles()) {
            files.add(fileEntry1);
        }

        Collections.sort(files, cmp);
        System.out.println("Training file/s:");
        for (File fileEntry : files) {
            filesReadedSoFar++;
            filesToBeRemoved.add(fileEntry);
            String insidePath = path + "\\" + fileEntry.getName();
            System.out.println(fileEntry.getName());
            File file = new File(insidePath);
            for (File message : file.listFiles()) {
                String insideMess = insidePath + "\\" + message.getName();
                File file1 = new File(insideMess);
                try {
                    sc = new Scanner(file1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String str = insideMess.substring(insideMess.lastIndexOf("\\") + 1);
                if (str.startsWith("spm")) {
                    this.numberOfSpam++;
                    while (sc.hasNext()) {
                        String st = new String(sc.next());
                        if (spamWords.containsKey(st)) {
                            spamWords.put(st, spamWords.get(st) + 1);
                        } else {
                            spamWords.put(st, 1);
                            words.add(st);
                        }
                    }
                } else {
                    this.numberOfHam++;
                    while (sc.hasNext()) {
                        String st = new String(sc.next());
                        if (hamWords.containsKey(st)) {
                            hamWords.put(st, hamWords.get(st) + 1);
                        } else {
                            hamWords.put(st, 1);
                            words.add(st);
                        }
                    }
                }
            }
            if (training == filesReadedSoFar) {
                this.vocabulary = words.size();
                break;
            }
        }
        files.removeAll(filesToBeRemoved);
    }
//---------------------------------PROBABILITES-------------------------------------------------------------------------
    public double PriorProbability(String category){
        if(category.equalsIgnoreCase("ham")){
            return Math.log(numberOfHam/(numberOfSpam+numberOfHam));
        }else if (category.equalsIgnoreCase("spam")){
            return Math.log(numberOfSpam/(numberOfSpam+numberOfHam));
        }else{
            return 0;
        }
    }

    //multinomial NB with Boolean attributes: P(ti|spam) = 1+N_ti,spam/m+N_spam
    public double ConditionalProbability(String word, String category){
        double p = 0;
        if(category.equalsIgnoreCase("ham")){
            if(!hamWords.containsKey(word)){
                p = 1/(vocabulary+N_hams);
                p = Math.log(p);
            }else {
                p = (hamWords.get(word) + 1) / (N_hams + vocabulary);
                p = Math.log(p);
            }
        }else if(category.equalsIgnoreCase("spam")){
            if(!spamWords.containsKey(word)){
                p = 1/(vocabulary+N_spams);
                p = Math.log(p);
            }else {
                p = (spamWords.get(word) + 1) / (N_spams + vocabulary);
                p = Math.log(p);
            }
        }
        return p;
    }

    public String Classification(String path){
        HashSet<String> message= new HashSet<>();
        double p_ham = 0;
        double p_spam = 0;
        File file = new File(path);
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(input.hasNext()){
            message.add(input.next());
        }
        for(String word : message){
            p_ham +=ConditionalProbability(word,"ham");
            p_spam +=ConditionalProbability(word,"spam");
        }
        p_ham += PriorProbability("ham");
        p_spam +=PriorProbability("spam");
        if(p_ham > p_spam){
            return "ham";
        }else {
            return "spam";
        }
    }
//----------------------------------TRAINING----------------------------------------------------------------------------
public void training(String basePath,int training){
        ReadTrainingSet(basePath,training);
        NumberOfAllTokens("ham");
        NumberOfAllTokens("spam");
}

//The amount of correct classifications/the total amount of classifications
public void stats(){
       double acc = 0;
       double test =0;
       double precision_spam = 0;
       double fake_spam = 0;
       double fake_ham = 0;
       System.out.println("Testing file/s :");
       for(File fl : files) {
           System.out.println(fl.getName());
           for (File fileEntry : fl.listFiles()) {
               test++;
               String newMessage = fileEntry.getPath();
               String category = Classification(newMessage);
               if ((newMessage.substring(newMessage.lastIndexOf("\\") + 1).startsWith("sp")) && category.startsWith("sp")) {
                   acc++;
                   precision_spam++;
               } else if (!(newMessage.substring(newMessage.lastIndexOf("\\") + 1).startsWith("sp")) && !category.startsWith("sp")) {
                   acc++;
               } else if (category.startsWith("sp") && !(newMessage.substring(newMessage.lastIndexOf("\\") + 1).startsWith("sp"))) {
                   fake_spam++;
               } else if ((newMessage.substring(newMessage.lastIndexOf("\\") + 1).startsWith("sp")) && !category.startsWith("sp")) {
                   fake_ham++;
               }
           }
       }
       double precision = precision_spam/(fake_spam+precision_spam);
       double recall = precision_spam/(precision_spam+fake_ham);
       double F1 = 2*(precision*recall)/(precision+recall);
       System.out.println("---------------------------------------------------------------------------------------------");
       System.out.println("acc:"+acc/test);
       System.out.println("Precision:"+precision);
       System.out.println("Recall:"+recall);
       System.out.println("F1:"+F1);
       System.out.println("---------------------------------------------------------------------------------------------");
}
//-----------------------------------HELPER_FUNCTIONS-------------------------------------------------------------------
    private double NumberOfAllTokens(String category){
        if(category.equalsIgnoreCase("ham")){
            N_hams = hamWords.values().stream().mapToInt(Integer::intValue).sum();
            return N_hams;
        }
        if(category.equalsIgnoreCase("spam")){
            N_spams = spamWords.values().stream().mapToInt(Integer::intValue).sum();
            return N_spams;
        }
        return 0;
    }
}
