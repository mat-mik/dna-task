package com.example.dnatask.joboffer.job.model;

public enum Category {
    IT("IT"),
    FOOD_AND_DRINKS("Food & Drinks"),
    OFFICE("Office"),
    COURIER("Courier"),
    SHOP_ASSISTANT("Shop Assistant");

    private final String displayText;

    Category(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
