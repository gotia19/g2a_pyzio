package com.capgemini.stk.framework.actions;

public final class PerformSetupAction {
    private PerformSetupAction() {
    }

    //    public static Language getLanguageProperty() {
    //        String   propertyValue = System.getProperty("language", "English");
    //        Language langEnum      = Language.fromString(propertyValue);
    //        assertNotNull("Unknown language parameter: " + propertyValue, langEnum);
    //        return langEnum;
    //    }

    public static String getEnvironmentProperty() {
        return System.getProperty("env", "TEST");
    }

    public static boolean isEnvironmentPropertyQA() {
        return getEnvironmentProperty().equals("QA");
    }

    public static boolean isDebugPropertyPresent() {
        return System.getProperty("debug") != null;
    }
}
