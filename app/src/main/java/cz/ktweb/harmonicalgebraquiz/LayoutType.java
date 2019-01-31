package cz.ktweb.harmonicalgebraquiz;


enum LayoutType {
    relative_cmaj(0, "Relative to C Major layout"),
    relative_native(1, "Native layout"),
    piano_cmaj(2, "Piano C Major layout"),
    piano_shifted(3, "Shifted Piano layout"),
    violin_1(4, "Violin 1st pos layout"),
    violin_3(5, "Violin 3rd pos layout"),
    violin_4(6, "Violin 4th pos layout"),
    ;

    private final int value;
    private final String label;
    private LayoutType(int value, String lab) {
        this.value = value;
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
