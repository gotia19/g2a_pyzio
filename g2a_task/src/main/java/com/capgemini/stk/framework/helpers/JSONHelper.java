package com.capgemini.stk.framework.helpers;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

public class JSONHelper {
    public static Object getIgnoreCase(JSONObject jsonObject, String key) {
        for (Object jsonKey : jsonObject.keySet()) {
            String jsonKeyString = jsonKey.toString();
            if (StringUtils.equalsIgnoreCase(jsonKeyString, key)) {
                return jsonObject.get(jsonKey);
            }
        }
        return null;
    }
}
