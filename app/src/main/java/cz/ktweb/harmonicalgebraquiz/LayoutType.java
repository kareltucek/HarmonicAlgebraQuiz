package cz.ktweb.harmonicalgebraquiz;


enum LayoutType {
    relative_cmaj("Major layout"),
    relative_native("Native layout"),
    piano_cmaj("Piano layout - C"),
    piano_shifted("Piano layout - Shifted"),
    parallel_maj("Parallel major layout"),
    parallel_min("Parallel minor layout"),
    piano_parallel_maj("Piano layout - Parallel major"),
    piano_parallel_min("Piano layout - Parallel minor"),
    linear("Linear"),
    violin_1("Fretboard in fifths - G"),
    violin_3("Fretboard in fifths - C"),
    violin_4("Fretboard in fifths - D"),
    ;

    private final int value;
    private final String label;
    private LayoutType(String lab) {
        this.value = this.ordinal();
        this.label = lab;
    }
    public static LayoutType ByValue(int value) {
        for(LayoutType type : LayoutType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return piano_cmaj;
    }
    public int Position() {
        return value;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[LayoutType.values().length];
        for(LayoutType type : LayoutType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }
}
