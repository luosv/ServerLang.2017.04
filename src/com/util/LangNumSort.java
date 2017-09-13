package com.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * 比较两个序号值
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class LangNumSort implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        if (o1.getValue() > o2.getValue()) {
            return 1;
        }

        if (Objects.equals(o1.getValue(), o2.getValue())) {
            return 0;
        }
        return -1;
    }

}
