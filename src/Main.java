import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Report report = new Report();
        MonthlyReport monthlyReport = new MonthlyReport();
        YearlyReport yearlyReport = new YearlyReport();

        String[] months = {"m.202101.csv", "m.202102.csv", "m.202103.csv", "m.202104.csv"};
        String year = "y.2021.csv";

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:");

            System.out.println("1 - Считать все месячные отчёты");
            System.out.println("2 - Считать годовой отчёт");
            System.out.println("3 - Сверить отчёты");
            System.out.println("4 - Вывести информацию о всех месячных отчётах");
            System.out.println("5 - Вывести информацию о годовом отчёте");
            System.out.println("0 - Выход");

            int command = scanner.nextInt();

            if (command == 1) {
                System.out.println(monthlyReport.readMonthlyReport(months));

            } else if (command == 2) {
                System.out.println(yearlyReport.readYearlyReport(year));

            } else if (command == 3) {
                System.out.println(report.checkReports(monthlyReport.readMonthlyReport(months), yearlyReport.readYearlyReport(year)));

            } else if (command == 4) {
                monthlyReport.monthlyReportInfo(months);

            } else if (command == 5) {
                yearlyReport.yearlyReportInfo(year);

            } else if (command == 0) {
                break;
            }
        }
    }
}
