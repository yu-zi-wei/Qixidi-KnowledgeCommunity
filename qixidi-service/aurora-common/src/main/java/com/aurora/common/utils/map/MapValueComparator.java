package com.aurora.common.utils.map;

import java.util.Comparator;
import java.util.Map;

/**
 * 比较器（map按value比较）
 */
class MapValueComparator implements Comparator<Map.Entry<String, String>> {

    @Override
    public int compare(Map.Entry<String, String> me1, Map.Entry<String, String> me2) {

        return me1.getValue().compareTo(me2.getValue());
    }
}
