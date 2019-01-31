package cz.ktweb.harmonicalgebraquiz;


enum RestrictionType {
    lower(0, 0, 5, false, "Lower Half"),
    upper(1, 6, 12, false, "Upper Half"),
    octave1(2, 0, 12, false, "Single Octave 1-8"),
    octave2(3, -6, 5, false, "Single Octave 5-4"),
    multi_octave(4, -6, 17, false, "Two octave"),
    two_octave_f(5, -7, 17, true, "Two octave (F rooted)"),
    two_octave_c(6, 0, 24, true, "Two octave (C rooted)"),
    two_octave_g(7, -5, 19, true, "Two octave (G rooted)"),
    two_octave_d(8, 2, 26, true, "Two octave (D rooted)"),
    three_octave(9, -6, 17+12, false, "Three octave"),
    three_octave_f(10, -7, 17+12, true, "Three octave (F rooted)"),
    three_octave_c(11, 0, 24+12, true, "Three octave (C rooted)"),
    three_octave_g(12, -5, 19+12, true, "Three octave (G rooted)"),
    three_octave_d(13, 2, 26+12, true, "Three octave (D rooted)"),
    ;

    private final int value;
    private final String label;
    private final int from;
    private final int to;
    private final boolean absolute;
    private RestrictionType(int value, int from, int to, boolean absolute, String lab) {
        this.value = value;
        this.label = lab;
        this.from = from;
        this.to = to;
        this.absolute = absolute;
    }
    public static RestrictionType ByValue(int value) {
        for(RestrictionType type : RestrictionType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return multi_octave;
    }
    public int Position() {
        return value;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[RestrictionType.values().length];
        for(RestrictionType type : RestrictionType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }

    public static boolean RestrictionAllows(RestrictionType tpe, int deg) {
        return tpe.from <= deg && tpe.to >= deg;
    }

    public boolean Allows(int deg) {
        return RestrictionAllows(this, deg);
    }

    public int From() {
        return this.from;
    }

    public int To() {
        return this.to;
    }

    public boolean Absolute() {
        return this.absolute;
    }
}
