import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonthlyReport extends Report{
    private final ArrayList<ArrayList<ArrayList<String>>> monthList;
    private final HashMap<Integer, ArrayList<Integer>> monthlyReport;

    public MonthlyReport() {
        this.monthList = new ArrayList<>();
        this.monthlyReport = new HashMap<>();
    }

    public HashMap<Integer, ArrayList<Integer>> getMonthlyReport() {
        return monthlyReport;
    }

    public ArrayList<ArrayList<ArrayList<String>>> readMonthlyReport(String[] months) {
        if (monthList.isEmpty()){
            for (int i = 0; i < months.length; i++) {
                String fileContentsMonthly = readFileContentsOrNull(months[i]);
                String[] monthLines = fileContentsMonthly.split(System.lineSeparator());
                ArrayList<ArrayList<String>> lines = new ArrayList<>();
                monthList.add(lines);
                for (int j = 1; j < monthLines.length; j++) {
                    String[] monthLineContents = monthLines[j].split(",");
                    ArrayList<String> words = new ArrayList<>();
                    lines.add(words);
                    for (int k = 0; k < monthLineContents.length; k++) {
                        words.add(monthLineContents[k]);
                    }
                }
            }
        }
        return monthList;
    }

    public HashMap<Integer, ArrayList<Integer>> getMonthlyReportFromList() {
        for (int i = 0; i < monthList.size(); i++) {
            ArrayList<Integer> reportt = new ArrayList<>();
            int sumFalseMonthTotal = 0;
            int sumTrueMonthTotal = 0;
            for (int j = 0; j < monthList.get(i).size(); j++) {
                ArrayList<String> monthLineContents = monthList.get(i).get(j);
                if (monthLineContents.get(1).equals("FALSE")){
                    sumFalseMonthTotal = Integer.parseInt(monthLineContents.get(2)) * Integer.parseInt(monthLineContents.get(3)) + sumFalseMonthTotal;
                }
                if (monthLineContents.get(1).equals("TRUE")){
                    sumTrueMonthTotal = Integer.parseInt(monthLineContents.get(2)) * Integer.parseInt(monthLineContents.get(3)) + sumTrueMonthTotal;
                }
            }
            reportt.add(sumFalseMonthTotal);
            reportt.add(sumTrueMonthTotal);
            monthlyReport.put(i+1, reportt);
        }
        return monthlyReport;
    }

    public HashMap<Integer, HashMap<Integer, String>> getMonthProfit() {
        HashMap<Integer, HashMap<Integer, String>> monthAllProfit = new HashMap<>();
        for (int i = 0; i < monthList.size(); i++) {
            HashMap<Integer, String> profit = new HashMap<>();
            int maxProfit = 0;
            String goodP = "";
            for (int j = 0; j < monthList.get(i).size(); j++) {
                ArrayList<String> monthLineContents = monthList.get(i).get(j);
                if (monthLineContents.get(1).equals("FALSE")){
                    int count = Integer.parseInt(monthLineContents.get(2)) * Integer.parseInt(monthLineContents.get(3));
                    if (count > maxProfit){
                        maxProfit = count;
                        goodP = monthLineContents.get(0);
                    }
                }
            }
            profit.put(maxProfit, goodP);
            monthAllProfit.put(i + 1, profit);
        }
        return monthAllProfit;
    }

    public HashMap<Integer, HashMap<Integer, String>> getMonthWaste() {
        HashMap<Integer, HashMap<Integer, String>> monthAllWaste = new HashMap<>();
        for (int i = 0; i < monthList.size(); i++) {
            HashMap<Integer, String> waste = new HashMap<>();
            int maxWaste = 0;
            String goodW = "";
            for (int j = 0; j < monthList.get(i).size(); j++) {
                ArrayList<String> monthLineContents = monthList.get(i).get(j);
                if (monthLineContents.get(1).equals("TRUE")){
                    int count = Integer.parseInt(monthLineContents.get(2)) * Integer.parseInt(monthLineContents.get(3));
                    if (count > maxWaste){
                        maxWaste = count;
                        goodW = monthLineContents.get(0);
                    }
                }
            }
            waste.put(maxWaste, goodW);
            monthAllWaste.put(i + 1, waste);
        }
        return monthAllWaste;
    }

    public void getMonthlyReportInfo() {
        if (monthlyReport.isEmpty()){
            System.out.println("Информация отсутствует. Файлы не считаны");
        } else {
            for (Map.Entry<Integer, HashMap<Integer, String>> entry : getMonthProfit().entrySet()) {
                System.out.println("В " + entry.getKey() + "м месяце самая большая прибыль составила: ");
                for (Map.Entry<Integer, String> value : entry.getValue().entrySet()) {
                    System.out.println(value.getKey() + " при продаже " + value.getValue());
                }
            }
            for (Map.Entry<Integer, HashMap<Integer, String>> entry : getMonthWaste().entrySet()) {
                System.out.println("В " + entry.getKey() + "м месяце самая большая трата составила: ");
                for (Map.Entry<Integer, String> value : entry.getValue().entrySet()) {
                    System.out.println(value.getKey() + " на приобретение " + value.getValue());
                }
            }
        }
    }

}