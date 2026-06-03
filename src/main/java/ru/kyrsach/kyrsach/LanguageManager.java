package ru.kyrsach.kyrsach;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
//    private static ResourceBundle bundle;
 static Locale currentLocale = new Locale("ru", "RU");

    public static Locale getLocale() {
        return currentLocale;
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
//        bundle = ResourceBundle.getBundle("main", currentLocale);
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("main", currentLocale);
    }


}
