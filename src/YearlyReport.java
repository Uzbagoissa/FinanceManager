import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YearlyReport extends Report{

    public HashMap<Integer, ArrayList<Integer>> readYearlyReport(String year){
        HashMap<Integer, ArrayList<Integer>> yearlyReport = new HashMap<>();
        ArrayList<Integer> report1 = new ArrayList<>();
        ArrayList<Integer> report2 = new ArrayList<>();
        ArrayList<Integer> report3 = new ArrayList<>();
        ArrayList<Integer> report4 = new ArrayList<>();
        String fileContentsYearly = readFileContentsOrNull(year);
        String[] yearLines = fileContentsYearly.split(System.lineSeparator());
        for (int i = 1; i < yearLines.length; i++) {
            String[] yearLineContents = yearLines[i].split(",");
            if (yearLineContents[0].equals("01")){
                report1.add(Integer.parseInt(yearLineContents[1]));
                yearlyReport.put(Integer.parseInt(yearLineContents[0]), report1);
            }
            if (yearLineContents[0].equals("02")){
                report2.add(Integer.parseInt(yearLineContents[1]));
                yearlyReport.put(Integer.parseInt(yearLineContents[0]), report2);
            }
            if (yearLineContents[0].equals("03")){
                report3.add(Integer.parseInt(yearLineContents[1]));
                yearlyReport.put(Integer.parseInt(yearLineContents[0]), report3);
            }
            if (yearLineContents[0].equals("04")){
                report4.add(Integer.parseInt(yearLineContents[1]));
                yearlyReport.put(Integer.parseInt(yearLineContents[0]), report4);
            }
        }
        return yearlyReport;
    }

    public void yearlyReportInfo(String year){
        System.out.println("Рассматриваемый год: " + year.substring(2, 6));
        HashMap<Integer, ArrayList<Integer>> yearlyReport = readYearlyReport(year);
        for (Map.Entry<Integer, ArrayList<Integer>> moneyGo : yearlyReport.entrySet()) {
            System.out.println("Прибыль по месяцу №" + moneyGo.getKey() + " составила: " + (moneyGo.getValue().get(0) - moneyGo.getValue().get(1)));
        }
        String fileContentsYearly = readFileContentsOrNull(year);
        String[] yearLines = fileContentsYearly.split(System.lineSeparator());
        double profit = 0;
        double averageProfit = 0;
        double waste = 0;
        double averageWaste = 0;
        for (int i = 1; i < yearLines.length; i++){
            String[] yearLineContents = yearLines[i].split(",");
            if (yearLineContents[2].equals("false")){
                profit = profit + Integer.parseInt(yearLineContents[1]);
            }
            if (yearLineContents[2].equals("true")){
                waste = waste + Integer.parseInt(yearLineContents[1]);
            }
        }
        averageProfit = profit / ((yearLines.length - 1) / 2);
        averageWaste = waste / ((yearLines.length - 1) / 2);
        System.out.println("Средний доход за все время составил: " + averageProfit + ". Средний расход за все время составил: " + averageWaste);
    }
}
