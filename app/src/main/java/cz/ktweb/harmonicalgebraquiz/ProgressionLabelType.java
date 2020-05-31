package cz.ktweb.harmonicalgebraquiz;

public enum ProgressionLabelType {
    chord_degree(0, "Degrees"),
    chord_name(1, "Tones");

    private final int value;
    private final String label;
    private ProgressionLabelType(int value, String lab) {
        this.value = value;
        this.label = lab;
    }
    public static ProgressionLabelType ByValue(int value) {
        for(ProgressionLabelType type : ProgressionLabelType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return chord_degree;
    }
    public int Position() {
        return value;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[ProgressionLabelType.values().length];
        for(ProgressionLabelType type : ProgressionLabelType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }

};