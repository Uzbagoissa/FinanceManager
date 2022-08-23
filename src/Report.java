import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Report {

    public String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл отчета. Возможно, файл не находится в нужной директории.");
            return null;
        }
    }

    public ArrayList<String> checkReports(HashMap monthlyReport, HashMap yearlyreport) {
        ArrayList<String> result = new ArrayList<>();
        if (monthlyReport.isEmpty() || yearlyreport.isEmpty()){
            System.out.println("Информация отсутствует. Файлы не считаны");
        } else {
            if (monthlyReport.equals(yearlyreport)){
                result.add("Сверка успешно проведена");
            }
            if (!monthlyReport.equals(yearlyreport)){
                for (Object o : yearlyreport.keySet()) {
                    for (Object o1 : monthlyReport.keySet()) {
                        if (o == o1){
                            if (!yearlyreport.get(o).equals(monthlyReport.get(o1))){
                                result.add(o1.toString());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
