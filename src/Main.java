import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MonthlyReport monthlyReport = new MonthlyReport();
        String fileContents = monthlyReport.readFileContentsOrNull("m.202101.csv");
        String[] lines = fileContents.split(System.lineSeparator());
        System.out.println(lines[1]);
    }
}
