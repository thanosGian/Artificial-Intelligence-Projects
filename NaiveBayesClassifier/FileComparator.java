import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof File && o2 instanceof File) {
            File file1 = (File)o1;
            File file2 = (File)o2;
            String str1 = file1.getName();
            String str2 = file2.getName();
            String[] st1 = str1.split("(?<=\\D)(?=\\d)");
            String[] st2 = str2.split("(?<=\\D)(?=\\d)");
            int a = Integer.parseInt(st1[1]);
            int b = Integer.parseInt(st2[1]);
            int dif = a - b;
            if (dif == 0) {
                return 0;
            } else if (dif < 0) {
                //file1<file2
                return -1;
            } else {
                //file1>file2
                return 1;
            }
        }else{
            return 2;
        }
    }

}
