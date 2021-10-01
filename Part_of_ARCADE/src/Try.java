import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Try {
    public static void main(String[] args) {
        String line = "JUnit5.4.2-103-";
        File file = new File(line);
        Pattern pattern = Pattern.compile("-(\\d+)-");
        Matcher matcher = pattern.matcher(file.getName());
        while (matcher.find()) {
            String numOfTopics = matcher.group(1);
            System.out.println(numOfTopics+" "+line);
        }
        System.out.println();
    }
}
