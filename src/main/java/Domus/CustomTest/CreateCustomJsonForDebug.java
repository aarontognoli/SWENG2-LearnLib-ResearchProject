package Domus.CustomTest;

import Domus.DatasetUtils.DatasetClass.Dataset;
import Domus.DatasetUtils.DatasetClass.DatasetDay;
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

public class CreateCustomJsonForDebug {
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
                createFromString("a",myMap),//pre
                createFromString("aba",myMap),//during
                createFromString("aa",myMap)//post
        ));
        myTest.addDayToLastUser(new DatasetDay(
                createFromString("e",myMap),//pre
                createFromString("abc",myMap),//during
                createFromString("be",myMap)//post
        ));

        myTest.addUser();
        myTest.addDayToLastUser(new DatasetDay(
                createFromString("b",myMap),//pre
                createFromString("cdc",myMap),//during
                createFromString("e",myMap)//post
        ));
        myTest.addDayToLastUser(new DatasetDay(
                createFromString("d",myMap),//pre
                createFromString("cecd",myMap),//during
                createFromString("bc",myMap)//post
        ));
        Gson gson = getCustomGson();
        try (FileWriter writer = new FileWriter("./CustomJson2.json")) {
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
