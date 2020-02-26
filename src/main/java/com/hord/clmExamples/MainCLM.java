package com.hord.clmExamples;

import java.util.HashSet;
import java.util.Set;

public class MainCLM {

    public static void main(String[] args) {
        User user = new User();

        Set<String> roles = new HashSet<>();
        roles.add("BI Contract Admin");

        user.addRoles(roles);

        Contract contract = new Contract();
        contract.setStage("Manage");

        String stage = contract.getStage();
        Set<String> uRoles = user.getRoles();

        boolean result = !stage.equals("Request") && (!uRoles.contains("Administrator") || !uRoles.contains("BI Contract Admin"));

        System.out.println("Read Only?: " + result);
    }
}
