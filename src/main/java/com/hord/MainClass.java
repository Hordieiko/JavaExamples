package com.hord;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {

    public static void main(String[] args) {
        Pattern nonWordPattern = Pattern.compile("\\W");
        Matcher matcher = nonWordPattern.matcher("apacApprovalTeam/");
        boolean b = !matcher.find();
        System.out.println(b);
    }
}

