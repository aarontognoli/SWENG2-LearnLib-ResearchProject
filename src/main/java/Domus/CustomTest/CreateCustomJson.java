package Domus.CustomTest;

import Domus.DatasetUtils.DataserClass.Dataset;
import Domus.DatasetUtils.DataserClass.DatasetDay;
import Domus.DatasetUtils.DomusRecord;
import Domus.DatasetUtils.SensorState;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Domus.DatasetUtils.CustomGson.getCustomGson;

public class CreateCustomJson {
    public static void main(String[] args) throws Exception {
        Map<Character, DomusRecord> myMap = new HashMap<>();
        myMap.put('a',new DomusRecord(null,"a","a","", SensorState.DEBUG));
        myMap.put('b',new DomusRecord(null,"b","b","", SensorState.DEBUG));
        myMap.put('c',new DomusRecord(null,"c","c","", SensorState.DEBUG));
        myMap.put('d',new DomusRecord(null,"d","d","", SensorState.DEBUG));
        myMap.put('e',new DomusRecord(null,"e","e","", SensorState.DEBUG));
        Dataset myTest = new Dataset();
        myTest.addUser();

        myTest.addDayToLastUser(new DatasetDay(
                createFromString("aaa",myMap),//pre
                createFromString("ababababab",myMap),//during
                createFromString("aaa",myMap)//post
        ));
        myTest.addDayToLastUser(new DatasetDay(
                createFromString("abc",myMap),//pre
                createFromString("eabcba",myMap),//during
                createFromString("bbb",myMap)//post
        ));

        myTest.addUser();
        myTest.addDayToLastUser(new DatasetDay(
                createFromString("bdebd",myMap),//pre
                createFromString("abcdeab",myMap),//during
                createFromString("edc",myMap)//post
        ));
        myTest.addDayToLastUser(new DatasetDay(
                createFromString("dcdc",myMap),//pre
                createFromString("ceceabcecbea",myMap),//during
                createFromString("bcde",myMap)//post
        ));
        Gson gson = getCustomGson();
        try (FileWriter writer = new FileWriter("./CustomJson.json")) {
            gson.toJson(myTest, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<DomusRecord> createFromString(String string, Map<Character,DomusRecord> map)
    {
        List<DomusRecord> ret = new ArrayList<>();
        for(Character c : string.toCharArray())
        {
            ret.add(map.get(c));
        }
        return ret;
    }

}
