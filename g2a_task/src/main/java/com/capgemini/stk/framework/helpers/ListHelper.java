package com.capgemini.stk.framework.helpers;

import java.util.List;

public class ListHelper {
    public static boolean containsIgnoreCase(List<String> list, String soughtFor) {
        for (String current : list) {
            if (current.equalsIgnoreCase(soughtFor)) {
                return true;
            }
        }
        return false;
    }
}