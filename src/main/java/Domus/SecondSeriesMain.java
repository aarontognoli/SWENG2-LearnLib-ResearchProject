package Domus;

import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DataserClass.DatasetDay;
import Domus.DatasetUtils.DomusActivityRecord;
import Domus.DatasetUtils.DomusParser;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SecondSeriesMain {
    public static void main(String[] args) {
        Gson gson = CustomGson.getCustomGson();
        Dataset dataset = new Dataset();
        for (int u = 0; u < 6; u++)
        {
            dataset.addUser();
            List<DomusActivityRecord> activityRecords;
            List<DomusActivityRecord> filteredActivityRecords = new ArrayList<>();
            try {
                activityRecords = DomusParser.parseActivities("src/main/resources/Domus Series 2/User "+(u+1)+"/User"+(u+1)+"-activity-annotation-series2.txt");
                filteredActivityRecords = SecondSeriesMain.filterTeaActivity(activityRecords);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            for (int d = 0; d < 5; d++) {
                LocalTime start =filteredActivityRecords.get(d).start().toLocalTime();
                LocalTime end=filteredActivityRecords.get(d).end().toLocalTime();
                try {
                    dataset.addDayToLastUser(
                            new DatasetDay("src/main/resources/Domus Series 2/User " + (u + 1) + "/Day " + (d + 1) + ".vna",
                            start,
                            end)
                    );
                }
                catch (IOException ex)
                {
                    System.out.println(ex.getMessage());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        try (FileWriter writer = new FileWriter("./DatasetSeries2.json")) {
            gson.toJson(dataset, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<DomusActivityRecord> filterTeaActivity(List<DomusActivityRecord> records)
    {
        return new ArrayList<>(records).stream().filter(x-> x.activityId().equals("7")).toList();
    }
}
