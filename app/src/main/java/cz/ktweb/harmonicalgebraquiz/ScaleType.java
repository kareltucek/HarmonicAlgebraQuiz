package cz.ktweb.harmonicalgebraquiz;


enum ScaleType {
    major(0, 0, "Major"),
    minor(1, 9, "Minor"),
    whole_tone(2, 0, "Whole tone"),
    aiolian(3, 9, "Aiolian"),
    locrian(4, 11, "Locrian"),
    ionian(5, 0, "Ionian"),
    dorian(6, 2, "Dorian"),
    phrygian(7, 4, "Phrygian"),
    lydian(8, 5, "Lydian"),
    mixolydian(9, 7, "Mixolydian")
    ;

    private final int value;
    private final int offsetWRTMajor;
    private final String label;
    private ScaleType(int value, int offset, String lab) {
        this.value = value;
        this.label = lab;
        this.offsetWRTMajor = offset;
    }
    public int Position() {
        return value;
    }
    public int OffsetWRTMajor() {
        return offsetWRTMajor;
    }
    public static ScaleType ByValue(int value) {
        for(ScaleType type : ScaleType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return major;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[ScaleType.values().length];
        for(ScaleType type : ScaleType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }

    public static boolean majorResolvableTones[] = {true, false, true, false, true, true, false, true, false, true, false, true};


    public static boolean PresentInScale(ScaleType tpe, int deg) {
        if(tpe == whole_tone) {
            return deg % 2 == 0;
        } else {
            int degInMajor = (deg + 7*12 + tpe.offsetWRTMajor) % 12;
            return majorResolvableTones[degInMajor];
        }
    }

    public boolean Contains(int deg) {
        return PresentInScale(this, deg);
    }

    /**(this function works in tonic=C space)
     * @param from degree of the first tone, indexed by scale tones from one
     * @param count how many tones should be included in the chord
     * @return array of degrees indexed from zero by half tones
     */
    public int[] ResolveChord (int from, int count) {
        int[] res = new int[count];
        int i = 0;
        int atTone = 0;
        for(int j = this.offsetWRTMajor % 12; j < 36 && i < count; j++) {
            if(majorResolvableTones[j % 12]) {
                if(from > 0) {
                    from--;
                }
                if(from == 0) {
                    if(atTone % 2 == 0) {
                        res[i] = j - this.offsetWRTMajor;
                        i++;
                    }
                    atTone++;
                }
            }
        }
        return res;
    }

    public String GetDegreeLabel(ScaleLabelType type, int tonic, int degree) {
        return GetToneLabel(type, tonic, tonic+degree);
    }

    public String GetToneLabel(ScaleLabelType type, int tonic, int tone) {
        int degree = (tone - tonic) % 12;
        int sharps = GetSharps(tonic);
        SimpleLabelType sharpType = sharps >= 0 ? SimpleLabelType.major_sharps : SimpleLabelType.major_flats;
        if(!this.Contains(tone - tonic)) {
            return "";
        }
        switch(type) {
            case tone_name:
                return LabelStore.GetToneLabel(sharpType, tone);
            case chord_name:
                if(this.Contains(degree + 3) && this.Contains(degree + 6)) {
                    return LabelStore.GetToneLabel(sharpType.GetMinor(), tone) + "dim";
                } else if(this.Contains(degree + 4) && this.Contains(degree + 7)){
                    return LabelStore.GetToneLabel(sharpType.GetMajor(), tone);
                } else if(this.Contains(degree + 3) && this.Contains(degree + 7)){
                    return LabelStore.GetToneLabel(sharpType.GetMinor(), tone);
                } else {
                    return "?";
                }
            case tsd_degree:
                return LabelStore.GetToneLabel(SimpleLabelType.major_tsd, degree + OffsetWRTMajor());
            case chord_degree:
                if(this == major) {
                    return LabelStore.GetToneLabel(SimpleLabelType.major_degrees, degree);
                }
                else if(this == minor) {
                    return LabelStore.GetToneLabel(SimpleLabelType.minor_degrees, degree);
                } else {
                    return "?";
                }
        }
        return "?";
    }

    public int GetSharps(int tonic) {
        int c_tonic = (tonic-Config.TypeOfScale.OffsetWRTMajor()) % 12;
        int sharpcount = ((c_tonic * 7) % 12);
        if(sharpcount > 6) {
            sharpcount = - 12 + sharpcount;
        }
        return sharpcount;
    }

    public int GetTonicFromSharps(int sharpcount) {
        return (OffsetWRTMajor() + sharpcount*7 + 2*12*7) % 12;
    }

    public String GetScaleName(int tonic) {
        return GetToneLabel(ScaleLabelType.tone_name, tonic, tonic) + " " + label;

    }
}
