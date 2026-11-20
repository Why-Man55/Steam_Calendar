package org.example.view.models;

public class UtilsServlet {
    public static boolean isCorrectId(String id) {
        return id == null || !id.matches("17\\d{15}");
    }
}
