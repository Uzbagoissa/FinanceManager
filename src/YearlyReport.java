import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YearlyReport{

    public void yearlyReportInfo(double[] average, HashMap<Integer, ArrayList<Integer>> yearlyReport){
        for (Map.Entry<Integer, ArrayList<Integer>> moneyGo : yearlyReport.entrySet()) {
            System.out.println("Прибыль по месяцу №" + moneyGo.getKey() + " составила: " + (moneyGo.getValue().get(0) - moneyGo.getValue().get(1)));
        }
        System.out.println("Средний доход за все время составил: " + average[0] + ". Средний расход за все время составил: " + average[1]);
    }
}
