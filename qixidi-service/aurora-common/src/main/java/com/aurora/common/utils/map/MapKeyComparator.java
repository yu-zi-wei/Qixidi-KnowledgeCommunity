package com.aurora.common.utils.map;


import java.util.Comparator;

/**
 * 比较器（map按key进行比较）
 */
public class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
