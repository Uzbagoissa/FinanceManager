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

            String command = scanner.next();

            if (command.equals("1")) {
                monthlyReportt.readMonthlyReport(months);
                monthlyReportt.getMonthlyReportFromList();
                System.out.println("Все месячные отчеты считаны");

            } else if (command.equals("2")) {
                yearlyReportt.readYearlyReport(year);
                yearlyReportt.getYearlyReportFromList();
                System.out.println("Годовой отчет считан");

            } else if (command.equals("3")) {
                System.out.println(report.checkReports(monthlyReportt.getMonthlyReport(), yearlyReportt.getYearlyReport()));

            } else if (command.equals("4")) {
                monthlyReportt.getMonthlyReportInfo();

            } else if (command.equals("5")) {
                System.out.println("Рассматриваемый год: " + year.substring(2, 6));
                yearlyReportt.getYearlyReportInfo();

            } else if (command.equals("0")) {
                break;

            } else {
                System.out.println("Введена неверная команда, попробуйте еще раз");
            }
        }
    }
}
