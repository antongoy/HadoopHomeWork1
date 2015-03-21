import org.apache.hadoop.io.Text;

import java.util.*;

public class Aggregator {

    private ArrayList<Integer> occurrences;

    public Aggregator(Iterable<Text> values) {
        HashMap<Text, Integer> mapOfOccurrences = new HashMap<Text, Integer>();

        for (Text key : values) {
            Integer value;

            if ((value = mapOfOccurrences.get(key)) != null) {
                mapOfOccurrences.put(key, value + 1);
            } else {
                mapOfOccurrences.put(key, 1);
            }
        }

        occurrences = new ArrayList<Integer>(mapOfOccurrences.values());
        Collections.sort(occurrences);
    }

    public Integer getUniqueOccurrences() {
        return occurrences.size();
    }

    public Integer getMaxOccurrences() {
        return occurrences.get(occurrences.size() - 1);
    }

    public Integer getMinOccurrences() {
        return occurrences.get(0);
    }

    public Double getMeanOccurrences() {
        Double mean = 0.0;
        Integer length = occurrences.size();

        for(Integer value : occurrences) {
            mean += value;
        }

        return mean / length;
    }

    public Double getDeviationOccurrences() {
        Double mean = getMeanOccurrences();
        Double deviation = 0.0;
        Integer length = occurrences.size();

        for(Integer value : occurrences) {
            deviation += Math.pow(value - mean, 2);
        }

        return Math.sqrt(deviation / length);
    }

    public Double getMedianOccurrences() {
        Integer length = occurrences.size();

        if(length % 2 == 0) {
            Integer j = length / 2;
            Integer i = j - 1;

            return (occurrences.get(i) + occurrences.get(j)) / 2.0;
        } else {
            Integer i = length / 2;
            return  (double)occurrences.get(i);
        }
    }

    public String toString() {
        return occurrences.toString();
    }

}
