package com.example.mageapp.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by foo on 11/25/17.
 */

public class RequestParamList extends HashMap<String, List<String>> {
    public List get(String key) {
        List<String> list = super.get(key);
        if (list == null) {
            list = new ArrayList();
            this.put(key, list);
        }
        return list;
    }
}
