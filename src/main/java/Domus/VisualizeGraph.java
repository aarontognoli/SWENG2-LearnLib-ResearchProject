package Domus;

import AstarBstar.JsonSupportClass;
import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DomusRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.visualization.dot.DOT;

import java.io.*;
import java.lang.reflect.Type;

public class VisualizeGraph {

    public static void main(String[] args) throws IOException {
        //from json dfa
        Gson gson = CustomGson.getCustomGson();
        JsonSupportClass<DomusRecord> myObj;
        try (Reader reader = new FileReader("./DomusDFA1u-1d.json")) {
            //Gson solution for generic types
            Type myType = new TypeToken<JsonSupportClass<DomusRecord>>() {
            }.getType();
            // Convert JSON File to Java Object
            myObj = gson.fromJson(reader, myType);
            CompactDFA<DomusRecord> dfa = myObj.getDFA();

            StringBuilder dotString = new StringBuilder();
            GraphDOT.write(dfa,dotString);
            DOT.renderDOTExternal(dotString.toString(),"svg");
        }

        //from dot file
        //DOT.renderDOTExternal(new File("./TestDot1u-1d.dot"),"svg");
    }
}

