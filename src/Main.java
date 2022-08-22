import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String[] months = {"m.202101.csv", "m.202102.csv", "m.202103.csv", "m.202104.csv"};
        String year = "y.2021.csv";

        YearlyReport yearlyReportt = new YearlyReport();
        MonthlyReport monthlyReportt = new MonthlyReport();
        Report report = new Report();

        HashMap<Integer, ArrayList<Integer>> monthlyReport = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> yearlyReport = new HashMap<>();
        HashMap<Integer, HashMap<Integer, String>> monthAllProfit = new HashMap<>();
        HashMap<Integer, HashMap<Integer, String>> monthAllWaste = new HashMap<>();
        double[] average = new double[2];

        Scanner scanner = new Scanner(System.in);

        System.out.println("Считать все месячные отчёты?");
        String command = scanner.next();
        if (command.equals("да")) {
            for (int i = 0; i < months.length; i++) {
                String fileContentsMonthly = report.readFileContentsOrNull(months[i]);
                String[] monthLines = fileContentsMonthly.split(System.lineSeparator());
                ArrayList<Integer> reportt = new ArrayList<>();
                HashMap<Integer, String> profit = new HashMap<>();
                HashMap<Integer, String> waste = new HashMap<>();
                int sumFalseMonthTotal = 0;
                int sumTrueMonthTotal = 0;
                int maxProfit = 0;
                int maxWaste = 0;
                String goodP = "";
                String goodW = "";
                for (int j = 1; j < monthLines.length; j++) {
                    String[] monthLineContents = monthLines[j].split(",");
                    if (monthLineContents[1].equals("FALSE")){
                        sumFalseMonthTotal = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]) + sumFalseMonthTotal;
                        int count = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]);
                        if (count > maxProfit){
                            maxProfit = count;
                            goodP = monthLineContents[0];
                        }
                    }
                    if (monthLineContents[1].equals("TRUE")){
                        sumTrueMonthTotal = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]) + sumTrueMonthTotal;
                        int count = Integer.parseInt(monthLineContents[2]) * Integer.parseInt(monthLineContents[3]);
                        if (count > maxWaste){
                            maxWaste = count;
                            goodW = monthLineContents[0];
                        }
                    }
                }
                reportt.add(sumFalseMonthTotal);
                reportt.add(sumTrueMonthTotal);
                profit.put(maxProfit, goodP);
                waste.put(maxWaste, goodW);
                monthlyReport.put(i+1, reportt);
                monthAllProfit.put(i + 1, profit);
                monthAllWaste.put(i + 1, waste);
            }
        }

        System.out.println("Считать ежегодный отчёт?");
        String command1 = scanner.next();
        if (command1.equals("да")) {
            ArrayList<Integer> report1 = new ArrayList<>();
            ArrayList<Integer> report2 = new ArrayList<>();
            ArrayList<Integer> report3 = new ArrayList<>();
            ArrayList<Integer> report4 = new ArrayList<>();
            double profit = 0;
            double averageProfit = 0;
            double waste = 0;
            double averageWaste = 0;
            String fileContentsYearly = report.readFileContentsOrNull(year);
            String[] yearLines = fileContentsYearly.split(System.lineSeparator());
            for (int i = 1; i < yearLines.length; i++) {
                String[] yearLineContents = yearLines[i].split(",");
                if (yearLineContents[0].equals("01")) {
                    report1.add(Integer.parseInt(yearLineContents[1]));
                    yearlyReport.put(Integer.parseInt(yearLineContents[0]), report1);
                }
                if (yearLineContents[0].equals("02")) {
                    report2.add(Integer.parseInt(yearLineContents[1]));
                    yearlyReport.put(Integer.parseInt(yearLineContents[0]), report2);
                }
                if (yearLineContents[0].equals("03")) {
                    report3.add(Integer.parseInt(yearLineContents[1]));
                    yearlyReport.put(Integer.parseInt(yearLineContents[0]), report3);
                }
                if (yearLineContents[0].equals("04")) {
                    report4.add(Integer.parseInt(yearLineContents[1]));
                    yearlyReport.put(Integer.parseInt(yearLineContents[0]), report4);
                }
                if (yearLineContents[2].equals("false")) {
                    profit = profit + Integer.parseInt(yearLineContents[1]);
                }
                if (yearLineContents[2].equals("true")) {
                    waste = waste + Integer.parseInt(yearLineContents[1]);
                }
                averageProfit = profit / ((yearLines.length - 1) / 2);
                averageWaste = waste / ((yearLines.length - 1) / 2);
            }
            average = new double[]{averageProfit, averageWaste};
        }

        System.out.println("Сверить отчёты?");
        String command2 = scanner.next();
        if (command2.equals("да")) {
            System.out.println(report.checkReports(monthlyReport, yearlyReport));
        }

        System.out.println("Вывести информацию о всех месячных отчётах?");
        String command3 = scanner.next();
        if (command3.equals("да")) {
            monthlyReportt.monthlyReportInfo(monthAllProfit, monthAllWaste);
        }

        System.out.println("Вывести информацию о годовом отчёте?");
        String command4 = scanner.next();
        if (command4.equals("да")) {
            System.out.println("Рассматриваемый год: " + year.substring(2, 6));
            yearlyReportt.yearlyReportInfo(average, yearlyReport);
        }

        System.out.println("Завершить работу с программой?");
        String command5 = scanner.next();
        if (command5.equals("да")) {
            System.exit(1);
        }
    }
}
