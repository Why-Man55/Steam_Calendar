package org.example.view.utils;

public class ViewUtilsForSteam {

    public static boolean isCorrectId(String id) {
        return id == null || !id.matches("17\\d{15}");
    }
}
