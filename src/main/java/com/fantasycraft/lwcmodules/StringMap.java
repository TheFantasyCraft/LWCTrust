package com.fantasycraft.lwcmodules;

import java.util.HashMap;

/**
 * Created by thomas on 7/22/2014.
 */
public class StringMap<k> extends HashMap<String,k> {
    @Override
    public k put(String key, k value) {
        return super.put(key.toLowerCase(), value);
    }

    // not @Override because that would require the key parameter to be of type Object

    @Override
    public k get(Object key) {
        return super.get(((String)key).toLowerCase());
    }
}
