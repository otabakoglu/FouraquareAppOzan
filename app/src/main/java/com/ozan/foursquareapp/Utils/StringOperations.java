package com.ozan.foursquareapp.Utils;

import java.util.List;

public class StringOperations {

    public static String stringListToString(List<String> list){

        StringBuilder builder = new StringBuilder();
        for (String line : list) {
            builder.append(line + "\n");
        }
        return builder.toString();
    }

}
