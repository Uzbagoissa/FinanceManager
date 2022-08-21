import java.util.ArrayList;
import java.util.HashMap;

public class MonthlyReport extends Report{

    public HashMap<Integer, ArrayList<Integer>> readMonthlyReport(String[] months){
        HashMap<Integer, ArrayList<Integer>> monthlyReport = new HashMap<>();
        for (int i = 0; i < months.length; i++) {
            String fileContentsMonthly = readFileContentsOrNull(months[i]);
            String[] monthLines = fileContentsMonthly.split(System.lineSeparator());
            ArrayList<Integer> report = new ArrayList<>();
            int sumFalseMonthTotal = 0;
            int sumTrueMonthTotal = 0;
            for (int j = 1; j < monthLines.length; j++) {
                String[] monthLineContents = monthLines[j].split(",");
                if (monthLineContents[1].equals("FALSE")){
                    sumFalseMonthTotal = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]) + sumFalseMonthTotal;
                }
                if (monthLineContents[1].equals("TRUE")){
                    sumTrueMonthTotal = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]) + sumTrueMonthTotal;
                }
            }
            report.add(sumFalseMonthTotal);
            report.add(sumTrueMonthTotal);
            monthlyReport.put(i+1, report);
        }
        return monthlyReport;
    }

    public void monthlyReportInfo(String[] months){
        for (int i = 0; i < months.length; i++) {
            String fileContentsMonthly = readFileContentsOrNull(months[i]);
            String[] monthLines = fileContentsMonthly.split(System.lineSeparator());
            int maxProfit = 0;
            int maxWaste = 0;
            String goodP = "";
            String goodW = "";
            for (int j = 1; j < monthLines.length; j++){
                String[] monthLineContents = monthLines[j].split(",");
                if (monthLineContents[1].equals("FALSE")){
                    int count = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]);
                    if (count > maxProfit){
                        maxProfit = count;
                        goodP = monthLineContents[0];
                    }
                }
                if (monthLineContents[1].equals("TRUE")){
                    int count = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]);
                    if (count > maxWaste){
                        maxWaste = count;
                        goodW = monthLineContents[0];
                    }
                }
            }
            System.out.println("В " + (i + 1) + "м месяце самая большая прибыль составила: " + maxProfit + " при продаже " + goodP);
            System.out.println("В " + (i + 1) + "м месяце самая большая трата составила: " + maxWaste + " на приобретение " + goodW);
        }
    }
}
