package cz.ktweb.harmonicalgebraquiz;


import android.util.Log;

enum ResolutionType {
    basic(0, "Native"),
    major(1, "Major"),
    minor(2, "Minor"),
    chromatic(3, "Chromatic"),
    hybrid_whole_tone(4, "Hybrid whole tone"),
    last_current(5, "Last - current - tonic")
    ;

    private final int value;
    private final String label;
    private ResolutionType(int value, String lab) {
        this.value = value;
        this.label = lab;
    }
    public int Position() {
        return value;
    }
    public static ResolutionType ByValue(int value) {
        for(ResolutionType type : ResolutionType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return basic;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[ResolutionType.values().length];
        for(ResolutionType type : ResolutionType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }

    public static boolean PresentInResolution(ScaleType scale, ResolutionType tpe, int from, int deg) {
        switch(tpe) {
            case major:
                return ScaleType.major.Contains(deg);
            case minor:
                return ScaleType.minor.Contains(deg);
            case hybrid_whole_tone:
                if(from == deg) {
                    return true;
                }
                else if(from % 2 == 0) {
                    return deg % 2 == 0;
                } else {
                    return scale.Contains(deg);
                }
            case chromatic:
                return true;
            case basic:
                return scale.Contains(deg);
            case last_current:
                return scale.Contains(deg);
            default:
                return false;
        }
    }

    public boolean ShouldResolve(ScaleType scale, int from, int to, int deg) {
        from = (from + 7*12) % 12;
        to = (to + 7*12) % 12;
        deg = (deg + 7*12) % 12;
        return to == deg || from == deg || PresentInResolution(scale,this, from, deg);
    }
}