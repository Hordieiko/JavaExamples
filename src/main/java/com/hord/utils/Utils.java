package com.hord.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Utils {
    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    /**
     * As instance:
     * String strings = "4653 Mercury Investors LLC\n" +
     *                 "888 Prospect LI, LLC\n";
     *
     * @param strings
     * @return
     */
    public List<String> splitStringsByLFSymbolToList(String strings) {
        List<String> values = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(strings, "\n");
        while (stringTokenizer.hasMoreElements())
            values.add(stringTokenizer.nextToken().trim());
        return values;
    }

    /**
     * Replace space with underscore
     *
     * @param name
     * @return
     */
    public String getTaskNameWithUnderscore(String name) {
        return name.trim().replace(" ", "_");
    }
}
