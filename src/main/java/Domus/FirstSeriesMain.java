package Domus;

import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DataserClass.DatasetDay;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FirstSeriesMain {
    public static void main(String[] args) {
        Gson gson = CustomGson.getCustomGson();
        Dataset dataset = new Dataset();
        for (int u = 0; u < 6; u++)
        {
            dataset.addUser();
            for (int d = 0; d < 10; d++) {
                try {
                    dataset.addDayToLastUser(new DatasetDay("src/main/resources/Domus Series 1/User " + (u + 1) + "/Day " + (d + 1) + ".vna"));
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
        try (FileWriter writer = new FileWriter("./DatasetSeries1.json")) {
            gson.toJson(dataset, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}