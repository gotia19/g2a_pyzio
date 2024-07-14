package com.capgemini.stk.framework.environment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PageSubURLsEnum {

    URL_DEMOQA_ELEMENTS("https://demoqa.com/elements"),
    URL_DEMOQA_ELEMENTS_TEXT_BOX("https://demoqa.com/text-box");
    //toDo

    private final String subURL;

    PageSubURLsEnum(String url) {
        this.subURL = url;
    }

    @Override
    public String toString() {
        return getValue();
    }

    public String getValue() {
        return subURL;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(PageSubURLsEnum::toString).collect(Collectors.toList());
    }
}