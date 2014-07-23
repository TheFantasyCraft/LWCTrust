package com.fantasycraft.lwcmodules;

import java.util.ArrayList;

/**
 * Created by thomas on 7/22/2014.
 */
public class StringList extends ArrayList<String> {
    @Override
    public boolean contains(Object o) {
        String paramStr = (String)o;
        for (String s : this) {
            if (paramStr.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
}