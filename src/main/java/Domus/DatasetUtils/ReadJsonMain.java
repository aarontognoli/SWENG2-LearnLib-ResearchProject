package Domus.DatasetUtils;

import Domus.DatasetUtils.DatasetClass.Dataset;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

//Snippet class to see if reading the Json works
public class ReadJsonMain {
    public static void main(String[] args) {
        Gson g = CustomGson.getCustomGson();
        Dataset d, d2;
        try (Reader reader = new FileReader("./DatasetSeries1.json")) {
            d = g.fromJson(reader,Dataset.class);
            System.out.println("Read d, you can do what you want");


        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Reader reader = new FileReader("./DatasetSeries2.json")) {
            d2 = g.fromJson(reader,Dataset.class);
            System.out.println("Read d2, you can do what you want");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
