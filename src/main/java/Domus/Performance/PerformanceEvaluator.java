package Domus.Performance;

import Domus.DatasetUtils.DomusRecord;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.words.Word;

import java.util.ArrayList;
import java.util.List;

public class PerformanceEvaluator {
    private final List<Word<DomusRecord>> positiveSequences;
    private final List<Word<DomusRecord>> negativeSequences;
    private final DFA<?, DomusRecord> dfa;
    private double accuracy; // fraction of the samples correctly classified in the dataset
    private double precision; // fraction of samples correctly classified in the positive class
                                // among the ones classified in the positive class
    private double recall; // fraction of samples correctly classified in the positive class
                            // among the ones belonging to the positive class
    private double F1score; // harmonic mean of the precision and recall

    public PerformanceEvaluator(DFA<?, DomusRecord> dfa) {
        this.dfa = dfa;
        this.positiveSequences = new ArrayList<>();
        this.negativeSequences = new ArrayList<>();
    }

    public PerformanceEvaluator(DFA<?, DomusRecord> dfa, List<Word<DomusRecord>> positiveSequences, List<Word<DomusRecord>> negativeSequences) {
        this.dfa = dfa;
        this.positiveSequences = positiveSequences;
        this.negativeSequences = negativeSequences;
    }

    public void run() {
        int samples = positiveSequences.size() + negativeSequences.size();
        int truePos = 0;
        int trueNeg = 0;
        int falsePos = 0;
        int falseNeg = 0;
        for (Word<DomusRecord> seq : positiveSequences) {
            if (dfa.accepts(seq)) {
                truePos++;
            } else {
                falseNeg++;
            }
        }

        for (Word<DomusRecord> seq : negativeSequences) {
            if (!dfa.accepts(seq)) {
                trueNeg++;
            } else {
                falsePos++;
            }
        }

        accuracy = samples > 0 ? 100.0*(truePos + trueNeg)/samples : 0;
        precision = (truePos + falsePos) > 0 ? 100.0*truePos/(truePos + falsePos) : 0;
        recall = (truePos + falseNeg) > 0 ? 100.0*truePos/(truePos + falseNeg): 0;
        F1score = (precision + recall) > 0 ? 2 * precision * recall / (precision + recall) : 0;
    }

    public void addToPositive(Word<DomusRecord> sequence) {
        positiveSequences.add(sequence);
    }

    public void addToPositive(List<Word<DomusRecord>> sequences) {
        positiveSequences.addAll(sequences);
    }

    public void addToNegative(Word<DomusRecord> sequence) {
        positiveSequences.add(sequence);
    }

    public void addToNegative(List<Word<DomusRecord>> sequences) {
        positiveSequences.addAll(sequences);
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getF1score() {
        return F1score;
    }
}
