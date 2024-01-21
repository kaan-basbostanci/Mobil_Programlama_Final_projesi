package com.example.mobil_programlama_final_uygulama;


import java.util.Map;

public class LabelModel {
    private String label;
    private String description;

    public LabelModel() {
        // Boş parametreli kurucu metod gereklidir (Firestore'dan verileri çekerken kullanılır).
    }

    public LabelModel(String label, String description) {
        this.label = label;
        this.description = description;
    }
    public static LabelModel toObject(Map<String, Object> data) {
        String label = (String) data.get("Label");
        String description = (String) data.get("Description");
        return new LabelModel(label, description);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
