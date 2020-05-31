package cz.ktweb.harmonicalgebraquiz;


import android.util.Log;

import java.util.Random;

enum ScaleType {
    major(0, "Major"),
    minor(9, "Minor"),
    random_min_maj(0, "Random Major/Minor"),
    whole_tone(0, "Whole Tone"),
    aiolian(9, "Aiolian"),
    locrian(11, "Locrian"),
    ionian(0, "Ionian"),
    dorian(2, "Dorian"),
    phrygian(4, "Phrygian"),
    lydian(5, "Lydian"),
    mixolydian(7, "Mixolydian"),
    random(0, "Random Mod")
    ;

    private final int value;
    private final int offsetWRTMajor;
    private final String label;
    private static Random rnd = new Random();
    private ScaleType(int offset, String lab) {
        this.value = this.ordinal();
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

    public ScaleType ResolveRandomModes() {
        switch(this) {
            case random:
                switch(rnd.nextInt(7)) {
                    case 0:
                        return aiolian;
                    case 1:
                        return locrian;
                    case 2:
                        return ionian;
                    case 3:
                        return dorian;
                    case 4:
                        return phrygian;
                    case 5:
                        return lydian;
                    case 7:
                        return mixolydian;
                    default:
                        return aiolian;
                }
            case random_min_maj:
                switch(rnd.nextInt(2)) {
                    case 0:
                        return major;
                    case 1:
                        return minor;
                    default:
                        return major;
                }
            default:
                return this;
        }
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
            return (deg + 7*12) % 2 == 0;
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

    /**
     * This works in standard space of the scale, given by the tonic and tone.
     * @param tonic
     * @param fromTone
     * @param count
     * @param skip
     * @return chord in degrees
     */
    public int[] ResolveChordInKey (int tonic, int fromTone, int count, int skip) {
        int[] res = new int[count];
        int filled = 0;
        int atTone = 0;
        int octave = fromTone - ((fromTone - tonic + 7*12) % 12);
        for(int j = 0; filled < count; j++) {
            int currentDegree = fromTone + j - tonic;
            if(this.Contains(currentDegree)) {
                if(atTone % 2 == 0) {
                    res[(filled-skip+count)%count] = currentDegree + (filled-skip < 0 ? 12 : 0);
                    filled++;
                }
                atTone++;
            }
        }

        return res;
    }

    /**
     * E.g., n=4 in C magor, means fifth tone, result is 7
     * @param n tone index, from zero
     * @return
     */
    public int GetNthTone(int n) {
        while(n < 0) {
            n += 7;
        }
        for(int i = 0; i < 120; i++) {
            if(n == 0 && this.Contains(i)) {
                return i;
            } else if (this.Contains(i)) {
                n--;
            }
        }
        return 0;
    }

    public int GetNthToneInKey(int tonic, int tone, int n) {
        int offset = 0;
        int step = n > 0 ? 1 : -1;
        int relative = ((tone - tonic + 7*12) % 12);
        for(int i = 0; i < 120; i++) {
            if(n == 0 && this.Contains(relative + offset)) {
                return tone + offset;
            } else if (this.Contains(relative + offset)) {
                n -= step;
            }
            offset += step;
        }
        return 0;
    }


    public String GetCustomChordToneLabel(int tonic, Chord chord) {
        int tone = tonic + chord.degree.Value();
        int degree = (chord.degree.Value() - tonic) % 12;
        int sharps = GetSharps(tonic);
        SimpleLabelType sharpType = sharps >= 0 ? SimpleLabelType.major_sharps : SimpleLabelType.major_flats;
        if(!this.Contains(degree)) {
            return "";
        }
        switch ( chord.tpe) {

            case major:
            case major8:
                return LabelStore.GetToneLabel(sharpType.GetMajor(), tone);
            case minor:
            case minor8:
                return LabelStore.GetToneLabel(sharpType.GetMinor(), tone);
            case major7:
                return LabelStore.GetToneLabel(sharpType.GetMajor(), tone) + "7";
            case dim:
                return LabelStore.GetToneLabel(sharpType.GetMajor(), tone) + "*";
            case dim7:
                return LabelStore.GetToneLabel(sharpType.GetMinor(), tone) + "*7";
            case dummy:
                return "";
        }
        return "?";
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
        int c_tonic = (tonic-this.OffsetWRTMajor()+7*12) % 12;
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


    public int GetMajorTonic(int tonicAt) {
        return (tonicAt - offsetWRTMajor + 7*12) % 12;
    }


    public int AugFourthDirection() {
        switch ( this ) {
            case locrian:
                return 1;
            case lydian:
                return -1;
            default:
                return 0;
        }
    }

}
