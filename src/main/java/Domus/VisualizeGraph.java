package Domus;

import AstarBstar.JsonSupportClass;
import Domus.DatasetUtils.CustomGson;
import Domus.DatasetUtils.DomusRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.dot.DOT;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

public class VisualizeGraph {

    public static void main(String[] args) throws IOException {
        //from json dfa
        visualizeJSON("DomusDFA1u-1d.json", false);
        //from dot file
        //visualizeDot("./TestDot1u-1d.dot");
    }

    public static void visualizeGraph(CompactDFA<?> automata) throws IOException
    {
        StringBuilder dotString = new StringBuilder();
        //CompactDFA<DomusRecord> a = filterGraph((CompactDFA<DomusRecord>) automata);
        GraphDOT.write(automata,dotString);

        DOT.renderDOTExternal(dotString.toString(),"svg");

    }

    public static void visualizeFile(File image) throws IOException {
        Desktop.getDesktop().open(image);
    }

    public static void visualizeDot(String path) throws IOException
    {
        DOT.renderDOTExternal(new File(path),"svg");
    }
    public static void visualizeJSON(String path, boolean toFilter) throws IOException {
        Gson gson = CustomGson.getCustomGson();
        JsonSupportClass<DomusRecord> myObj;
        try (Reader reader = new FileReader(path)) {
            //Gson solution for generic types
            Type myType = new TypeToken<JsonSupportClass<DomusRecord>>() {
            }.getType();
            // Convert JSON File to Java Object
            myObj = gson.fromJson(reader, myType);
            CompactDFA<DomusRecord> dfa = myObj.getDFA();

            if (toFilter) {
                visualizeGraph(filterGraph(dfa));
            } else {
                visualizeGraph(dfa);
            }
        }
    }

    public static CompactDFA<DomusRecord> filterGraph(CompactDFA<DomusRecord> automata)
    {
        CompactDFA<DomusRecord> ret = new CompactDFA<>(automata);
        for(Integer i : ret.getStates())
        {
            for(DomusRecord dr : ret.getInputAlphabet())
                ret.removeTransition(i,dr,i);
        }
        return ret;
    }
}

