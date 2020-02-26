package com.hord.generateEnumDef;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EnumParamDef {

    private List<String> possibleValueList = new LinkedList<>();

    private List<String> localizedStringList = new LinkedList<>();

    public EnumParamDef() {
    }

    public EnumParamDef(List<String> possibleValueList, List<String> localizedStringList) {
        this.possibleValueList.addAll(possibleValueList);
        this.localizedStringList.addAll(localizedStringList);
    }

    public List<String> getPossibleValueList() {
        return this.possibleValueList;
    }

    public void setPossibleValueList(List<String> possibleValueList) {
        this.possibleValueList.clear();
        this.possibleValueList.addAll(possibleValueList);
    }

    public void addPossibleValueList(List<String> possibleValueList) {
        this.possibleValueList.addAll(possibleValueList);
    }

    public List<String> getLocalizedStringList() {
        return this.localizedStringList;
    }

    public void setLocalizedStringList(List<String> localizedStringList) {
        this.localizedStringList.clear();
        this.localizedStringList.addAll(localizedStringList);
    }

    public void addLocalizedStringList(List<String> localizedStringList) {
        this.localizedStringList.addAll(localizedStringList);
    }

    @Override
    public String toString() {
        return "PossibleValueList:\n" +
                this.possibleValueList.stream().collect(Collectors.joining("\n")) + "\n" +
                "LocalizedStringList:\n" +
                this.localizedStringList.stream().collect(Collectors.joining("\n"));
    }
}
