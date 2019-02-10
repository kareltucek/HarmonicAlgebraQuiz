package cz.ktweb.harmonicalgebraquiz;


import android.util.Log;

enum ResolutionType {
    basic("Native"),
    inverted("Inverted Native - Other Tonic"),
    inverted2("Inverted Native - Same Tonic"),
    fifth("Inverted Native - Fifth"),
    major("Major"),
    minor("Minor"),
    c_major("C Major"),
    c_minor("C Minor"),
    chromatic("Chromatic"),
    hybrid_whole_tone("Hybrid Major/Whole Tone"),
    last_current("Last - Current"),
    current_lats("Current - Last"),
    ;

    private final int value;
    private final String label;
    private ResolutionType(String lab) {
        this.value = this.ordinal();
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

    public static boolean PresentInResolution(ScaleType scale, ResolutionType tpe, int tonic, int from_deg, int deg) {
        switch(tpe) {
            case major:
                return ScaleType.major.Contains(deg);
            case minor:
                return ScaleType.minor.Contains(deg);
            case c_major:
                return ScaleType.major.Contains(deg + tonic);
            case c_minor:
                return ScaleType.minor.Contains(deg + tonic);
            case hybrid_whole_tone:
                if(from_deg == deg) {
                    return true;
                }
                else if(from_deg % 2 == 0) {
                    return deg % 2 == 0;
                } else {
                    return scale.Contains(deg);
                }
            case chromatic:
                return true;
            default:
                return scale.Contains(deg);
        }
    }

    public boolean ShouldResolveDeg(ScaleType scale, int tonic, int from_deg, int to_deg, int deg) {
        from_deg = (from_deg + 7*12) % 12;
        to_deg = (to_deg + 7*12) % 12;
        deg = (deg + 7*12) % 12;
        return to_deg == deg || from_deg == deg || PresentInResolution(scale,this, tonic, from_deg, deg);
    }


    public boolean ShouldResolveTone(ScaleType scale, int tonic, int from, int to, int tone) {
        return ShouldResolveDeg(scale, tonic, from-tonic, to-tonic, tone-tonic);
    }
}