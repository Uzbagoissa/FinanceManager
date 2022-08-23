import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YearlyReport extends Report{
    private final ArrayList<ArrayList<String>> yearLinesList;
    private final HashMap<Integer, ArrayList<Integer>> yearlyReport;

    public YearlyReport() {
        this.yearLinesList = new ArrayList<>();
        this.yearlyReport = new HashMap<>();
    }

    public HashMap<Integer, ArrayList<Integer>> getYearlyReport() {
        return yearlyReport;
    }

    public ArrayList<ArrayList<String>> readYearlyReport(String year) {
        if (yearLinesList.isEmpty()){
            String fileContentsYearly = readFileContentsOrNull(year);
            String[] yearLines = fileContentsYearly.split(System.lineSeparator());
            for (int i = 1; i < yearLines.length; i++) {
                String[] yearLineContents = yearLines[i].split(",");
                ArrayList<String> words = new ArrayList<>();
                yearLinesList.add(words);
                for (int j = 0; j < yearLineContents.length; j++) {
                    words.add(yearLineContents[j]);
                }
            }
        }
        return yearLinesList;
    }

    public HashMap<Integer, ArrayList<Integer>> getYearlyReportFromList() {
        ArrayList<Integer> report1 = new ArrayList<>();
        ArrayList<Integer> report2 = new ArrayList<>();
        ArrayList<Integer> report3 = new ArrayList<>();
        ArrayList<Integer> report4 = new ArrayList<>();
        for (int i = 0; i < yearLinesList.size(); i++) {
            ArrayList<String> yearLineContents = yearLinesList.get(i);
            if (yearLineContents.get(0).equals("01")) {
                report1.add(Integer.parseInt(yearLineContents.get(1)));
                yearlyReport.put(Integer.parseInt(yearLineContents.get(0)), report1);
            }
            if (yearLineContents.get(0).equals("02")) {
                report2.add(Integer.parseInt(yearLineContents.get(1)));
                yearlyReport.put(Integer.parseInt(yearLineContents.get(0)), report2);
            }
            if (yearLineContents.get(0).equals("03")) {
                report3.add(Integer.parseInt(yearLineContents.get(1)));
                yearlyReport.put(Integer.parseInt(yearLineContents.get(0)), report3);
            }
            if (yearLineContents.get(0).equals("04")) {
                report4.add(Integer.parseInt(yearLineContents.get(1)));
                yearlyReport.put(Integer.parseInt(yearLineContents.get(0)), report4);
            }
        }
        return yearlyReport;
    }

    public ArrayList<Double> getAverageAmount() {
        ArrayList<Double> average = new ArrayList<>();
        double profit = 0;
        double averageProfit = 0;
        double waste = 0;
        double averageWaste = 0;
        for (int i = 0; i < yearLinesList.size(); i++) {
            ArrayList<String> yearLineContents = yearLinesList.get(i);
            if (yearLineContents.get(2).equals("false")) {
                profit = profit + Integer.parseInt(yearLineContents.get(1));
            }
            if (yearLineContents.get(2).equals("true")) {
                waste = waste + Integer.parseInt(yearLineContents.get(1));
            }
            averageProfit = profit / ((yearLinesList.size() - 1) / 2);
            averageWaste = waste / ((yearLinesList.size() - 1) / 2);
        }
        average.add(averageProfit);
        average.add(averageWaste);
        return average;
    }

    public void getYearlyReportInfo(){
        if (yearlyReport.isEmpty()){
            System.out.println("Информация отсутствует. Файлы не считаны");
        } else {
            for (Map.Entry<Integer, ArrayList<Integer>> moneyGo : yearlyReport.entrySet()) {
                System.out.println("Прибыль по месяцу №" + moneyGo.getKey() + " составила: " + (moneyGo.getValue().get(0) - moneyGo.getValue().get(1)));
            }
            System.out.println("Средний доход за все время составил: " + getAverageAmount().get(0) + ". Средний расход за все время составил: " + getAverageAmount().get(1));
        }
    }

}
