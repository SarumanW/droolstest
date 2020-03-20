package com.drools.model;

public enum Diet {
    FAT_LOSS("Fat loss diet"),
    PALEO("Paleo diet"),
    MEDITERRANEAN("Mediterranean diet"),
    KETO("Keto diet"),
    DIABETES("Diabetes diet"),
    VEGAN("Vegan diet"),
    VEGETERIAN("Vegeterian diet"),
    PESKATARIAN("Pesketarian diet"),
    GLUTEN_FREE("Gluten free diet"),
    DIARY_FREE("Diary free diet"),
    LOW_SODIUM("Low sodium diet");

    private String dietName;

    Diet(String dietName) {
        this.dietName = dietName;
    }
}
