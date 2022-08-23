import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String[] months = {"m.202101.csv", "m.202102.csv", "m.202103.csv", "m.202104.csv"};
        String year = "y.2021.csv";

        YearlyReport yearlyReportt = new YearlyReport();
        MonthlyReport monthlyReportt = new MonthlyReport();
        Report report = new Report();

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
                monthlyReportt.readMonthlyReport(months);
                monthlyReportt.getMonthlyReportFromList();

            } else if (command == 2) {
                yearlyReportt.readYearlyReport(year);
                yearlyReportt.getYearlyReportFromList();

            } else if (command == 3) {
                System.out.println(report.checkReports(monthlyReportt.getMonthlyReport(), yearlyReportt.getYearlyReport()));

            } else if (command == 4) {
                monthlyReportt.getMonthlyReportInfo();

            } else if (command == 5) {
                System.out.println("Рассматриваемый год: " + year.substring(2, 6));
                yearlyReportt.getYearlyReportInfo();

            } else if (command == 0) {
                break;
            }
        }
    }
}
