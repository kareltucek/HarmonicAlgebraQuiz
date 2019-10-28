package cz.ktweb.harmonicalgebraquiz;

//should be refactored into three separate options - pattern, range size, range root
enum RestrictionType {
    basic1(0, 5, false, "Basic Learning 1 (lower half)"),
    basic2(6, 12, false, "Basic Learning 2 (upper half)"),
    basic3(0, 12, false, "Basic Learning 3 (single octave)"),
    basic4(-6, 5, false, "Basic Learning 4 (single octave shifted)"),
    basic5(-6, 17, false, "Basic Learning 5 (multi octave)"),
    l1(-6, 24, false, "Chromatics Learning 1 (lower/lower)"),
    l3(-6, 24, false, "Chromatics Learning 2 (lower/both)"),
    l3b(-6, 24, false, "Chromatics Learning 3 (lower+both)"),
    l4(-6, 24, false, "Chromatics Learning 4 (upper/upper)"),
    l6(-6, 24, false, "Chromatics Learning 5 (upper/both)"),
    l6b(-6, 24, false, "Chromatics Learning 6 (upper+both)"),
    l7(-6, 24, false, "Chromatics Learning 7 (both/both)"),
    l7b(-6, 24, false, "Chromatics Learning 8 (chromatics only)"),
    l8(-6, 24, false, "Chromatics Learning 9 (all)"),
    lower(0, 6, false, "Lower Half"),
    upper(6, 12, false, "Upper Half"),
    one_octave(0, 12, false, "One octave (tonic rooted)"),
    one_octave2(-6, 5, false, "One octave (fifth rooted)"),
    one_octave_f(-7, 5, true, "One octave (F rooted)"),
    one_octave_c(0, 12, true, "One octave (C rooted)"),
    one_octave_g(-5, 7, true, "One octave (G rooted)"),
    one_octave_d(2, 14, true, "One octave (D rooted)"),
    two_octave(-6, 17, false, "Two octave"),
    two_octave_f(-7, 17, true, "Two octave (F rooted)"),
    two_octave_c(0, 24, true, "Two octave (C rooted)"),
    two_octave_g(-5, 19, true, "Two octave (G rooted)"),
    two_octave_d(2, 26, true, "Two octave (D rooted)"),
    three_octave(-6, 17+12, false, "Three octave"),
    three_octave_f(-7, 17+12, true, "Three octave (F rooted)"),
    three_octave_c(0, 24+12, true, "Three octave (C rooted)"),
    three_octave_g(-5, 19+12, true, "Three octave (G rooted)"),
    three_octave_d(2, 26+12, true, "Three octave (D rooted)"),
    tsd(0, 12, false, "1/4/5/8"),
    off_tsd(0, 12, false, "2/3/6/7"),
    tsd2(-12, 12, false, "1/4/5/8 (Two octave)"),
    off_tsd2(-12, 12, false, "2/3/6/7 (Two octave)"),
    nodim(-12, 12, false, "all\\dim (Two octave)"),
    ;

    private final int value;
    private final String label;
    private final int from;
    private final int to;
    private final boolean absolute;
    private RestrictionType(int from, int to, boolean absolute, String lab) {
        this.value = this.ordinal();
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
        return two_octave;
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

    public boolean BansChromatic() {
        if(this == basic1 || this == basic2 || this == basic3 || this == basic4 || this == basic5) {
            return true;
        }
        return false;
    }

    public boolean RequiresChromatic() {
        if(this == l1 || this == l3 || this == l4 || this == l6 || this == l7 || this == l3b || this == l6b || this == l7b || this == l8) {
            return true;
        }
        return false;
    }

    public boolean Allows(ScaleType tpe, int q, int tonic, int tone ) {
        int deg = (tone - tonic + 7*12) % 12;
        if(this == nodim) {
            if((tone + tpe.OffsetWRTMajor() - tonic + 7*12) % 12 == 11) {
                return false;
            }
        }
        else if(this == tsd || this == tsd2 || this == off_tsd || this == off_tsd2) {
            switch(deg) {
                case 0:
                case 5:
                case 6:
                case 7:
                    if(this == off_tsd || this == off_tsd2) {
                        return false;
                    }
                    break;
                default:
                    if(this == tsd || this == tsd2) {
                        return false;
                    }
                    break;
            }
        }
        else if(this == l7b) {
            if(tpe.Contains(deg)) {
                return false;
            }
        } else if (this == l3b || this == l6b) {
            if( this == l3b && deg > 6 && !tpe.Contains(deg)) {
                return false;
            } else
            if( this == l6b && deg < 6 && !tpe.Contains(deg)) {
                return false;
            }
        } else if(this == l1 || this == l3 || this == l4 || this == l6 || this == l7) {
            if(q % 2 == 0) {
                //restricts non-chromatics
                if((this == l4) && deg < 6) {
                    return false;
                } else if((this == l1) && deg > 6) {
                    return false;
                } else if (!tpe.Contains(deg)){
                    return false;
                }
            } else {
                //restricts chromatics
                if((this == l4 || this == l6) && deg < 6) {
                    return false;
                } else if((this == l1 || this == l3) && deg > 6) {
                    return false;
                } else if (tpe.Contains(deg)) {
                    return false;
                }
            }
        }
        return true;
        //some ranges are relative to tonic, therefore thw following would actually trim the range
        //return from <= tone && to >= tone;
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
