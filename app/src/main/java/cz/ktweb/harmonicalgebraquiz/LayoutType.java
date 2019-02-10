package cz.ktweb.harmonicalgebraquiz;


enum LayoutType {
    relative_cmaj("Major layout"),
    relative_native("Native layout"),
    piano_cmaj("Piano layout - C"),
    piano_shifted("Piano layout - Shifted"),
    linear("Linear"),
    violin_1("Violin 1st pos layout"),
    violin_3("Violin 3rd pos layout"),
    violin_4("Violin 4th pos layout"),
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
