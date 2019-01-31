package cz.ktweb.harmonicalgebraquiz;

public enum ScaleLabelType {
    tone_name(0, "Tones"),
    chord_name(1, "Chord names"),
    chord_degree(2, "Degrees"),
    tsd_degree(3, "TSD");

    private final int value;
    private final String label;
    private ScaleLabelType(int value, String lab) {
        this.value = value;
        this.label = lab;
    }
    public static ScaleLabelType ByValue(int value) {
        for(ScaleLabelType type : ScaleLabelType.values()) {
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
        String[] res = new String[ScaleLabelType.values().length];
        for(ScaleLabelType type : ScaleLabelType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }
};