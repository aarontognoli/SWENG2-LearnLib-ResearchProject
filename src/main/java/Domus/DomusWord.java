package Domus;

import Domus.DatasetUtils.DomusRecord;
import net.automatalib.words.Word;

import java.util.List;

public class DomusWord extends Word<DomusRecord> {
    List<DomusRecord> word;

    public DomusWord(List<DomusRecord> word) {
        this.word = word;
    }

    @Override
    public int length() {
        return word.size();
    }

    @Override
    public DomusRecord getSymbol(int index) {
        return word.get(index);
    }
}
