package com.hord.generateEnumDef;

import org.springframework.web.util.HtmlUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumParamDefGenerator {

    private List<String> values = new LinkedList<>();

    private EnumParamDef enumParamDef = new EnumParamDef();

    private static final String POSSIBLE_VALUE_PATTERN = "<possibleValue ref=\"%s\"/>";

    private static final String LOCALIZED_VALUE_PATTERN = "<LocalizedString attribute=\"%s\" value=\"%s\"/>";

    public EnumParamDefGenerator(List<String> values) {
        this.values.addAll(values);
        initEnumParamDef();
    }

    public EnumParamDef getEnumParamDef() {
        return enumParamDef;
    }

    private void initEnumParamDef() {
        enumParamDef.setPossibleValueList(values.stream().sequential().map(value -> String.format(POSSIBLE_VALUE_PATTERN, value)).collect(Collectors.toList()));
        enumParamDef.setLocalizedStringList(values.stream().sequential().map(htmlEscape()).map(value -> String.format(LOCALIZED_VALUE_PATTERN, value, value)).collect(Collectors.toList()));
    }

    private Function<String, String> htmlEscape() {
        return value -> HtmlUtils.htmlEscape(value);
    }
}
