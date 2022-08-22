import java.util.HashMap;
import java.util.Map;

public class MonthlyReport{

    public void monthlyReportInfo(HashMap<Integer, HashMap<Integer, String>> monthAllProfit, HashMap<Integer, HashMap<Integer, String>> monthAllWaste){
        for (Map.Entry<Integer, HashMap<Integer, String>> entry : monthAllProfit.entrySet()) {
            System.out.println("В " + entry.getKey() + "м месяце самая большая прибыль составила: ");
            for (Map.Entry<Integer, String> value : entry.getValue().entrySet()) {
                System.out.println(value.getKey() + " при продаже " + value.getValue());
            }
        }
        for (Map.Entry<Integer, HashMap<Integer, String>> entry : monthAllWaste.entrySet()) {
            System.out.println("В " + entry.getKey() + "м месяце самая большая трата составила: ");
            for (Map.Entry<Integer, String> value : entry.getValue().entrySet()) {
                System.out.println(value.getKey() + " на приобретение " + value.getValue());
            }
        }
    }
}