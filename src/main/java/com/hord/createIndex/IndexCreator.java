package com.hord.createIndex;

import java.util.HashSet;
import java.util.Set;

public class IndexCreator {

    private static Set<String> infoComponentTables = new HashSet<String>() {
        {
            add("MCPINFO01");
            add("CONTRACT1");
            add("LEASE");
            add("REALESTATECONTRACT");
            add("REALESTATECONTRACT");
            add("C_MCPINFO01");
        }
    };

    private static Set<String> nonInfoComponentTables = new HashSet<String>() {
        {
            add("ASSETIDENTIFIER");
            add("ATTACHMENT");
            add("CONTACTRELATIONSHIP");
            add("CONTRACTEVENT");
            add("CONTRACTTERMS");
            add("DEPARTMENTALLOCATION");
            add("EQUIPMENT");
            add("MCPCOMP01");
            add("MCPCOMP02");
            add("MCPCOMP03");
            add("MCPCOMP04");
            add("PAYMENT");
            add("OPTIONS");
            add("PROPERTY");
            add("SUBTENANT");
            add("TANDC");
            add("THIRDPARTY");
        }
    };

    private static String formatStatusIndex = "CREATE INDEX %s_STATUS_IDX ON %s (STATUS);";

    public static void create() {
        System.out.println("-- InfoComponent STATUS index:");
        infoComponentTables
                .stream()
                .map(s -> String.format(formatStatusIndex, s, s) + "\n")
                .forEach(System.out::println);
        System.out.println("\n -- NonInfoComponent STATUS index:");
        nonInfoComponentTables
                .stream()
                .map(s -> String.format(formatStatusIndex, s, s) + "\n")
                .forEach(System.out::println);
    }
}
