package com.example.crescendo;

import java.util.HashMap;

public class FrequencyTable<String, Integer> {
    private HashMap<String, java.lang.Integer> freqTable;
    private int totalKeys, valueSum;
    private String maxKey;
    private int maxCount;

    public FrequencyTable () {
        freqTable = new HashMap<String, java.lang.Integer>();
        totalKeys = 0;
        valueSum = 0;
        maxCount = 0;
        maxKey = null;
    }

    public void add (String key) {
        if (!freqTable.containsKey(key)) {
            freqTable.put(key, 1);
            if (maxCount == 0) {
                maxKey = key;
                maxCount = 1;
            }
        } else {
            int count = freqTable.get(key) + 1;
            if (count > maxCount) {
                maxKey = key;
                maxCount = count;
            }
            freqTable.put(key, count);
        }
    }

    public String getMostStreamed() {
        if (maxKey == null) {
            throw new NullPointerException("No streamed artists.");
        }
        return maxKey;
    }
}