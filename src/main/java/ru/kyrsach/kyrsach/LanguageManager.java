package ru.kyrsach.kyrsach;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static ResourceBundle bundle;
    private static Locale currentLocale = new Locale("en", "EN");

    public static Locale getLocale() {
        return currentLocale;
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("main", currentLocale);
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("main", currentLocale);
    }


}
