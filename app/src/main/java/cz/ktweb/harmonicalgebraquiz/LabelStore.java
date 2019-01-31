package cz.ktweb.harmonicalgebraquiz;


enum SimpleLabelType {
    minor_any(0),
    minor_sharps(1),
    minor_flats(2),
    major_any(3),
    major_sharps(4),
    major_flats(5),
    major_tsd(6),
    flatsharp(7),
    interval(8),
    minor_degrees(9),
    major_degrees(10);

    private final int value;
    private SimpleLabelType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getMinorValue() {
        return value > 2 && value < 6 ? value - 3 : value;
    }
    public int getMajorValue() {
        return value < 3 ? value + 3 : value;
    }

    public SimpleLabelType ByValue(int value) {
        for(SimpleLabelType type : SimpleLabelType.values()) {
            if(type.getValue() == value) {
                return type;
            }
        }
        return minor_any;
    }

    //well, this is nasty, sensible way must exist.
    public SimpleLabelType GetMinor() {
        return this.ByValue(getMinorValue());
    }

    public SimpleLabelType GetMajor() {
        return this.ByValue(getMajorValue());
    }
}

public class LabelStore {


    private static String[][] Labels = {
            {"c", " ", "d", " ", "e", "f", " ", "g", " ", "a", " ", "b"},
            {"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b"},
            {"c", "db", "d", "eb", "e", "f", "gb", "g", "ab", "a", "bb", "b"},
            {"C", " ", "D", " ", "E", "F", " ", "G", " ", "A", " ", "B"},
            {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"},
            {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"},
            {"T", " ", "s", " ", "d", "S", " ", "D", " ", "t", " ", "dim"},
            {" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "},
            {"1", "2b", "2", "3b", "3", "4", "5b", "5", "6b", "6", "7b", "7", "8"},
            {"i", " ", "ii°", "III", " ", "iv", " ", "V", "VI", " ", "VII", " "},
            {"I", " ", "ii", " ",  "iii", "IV", " ", "V", " ", "vi", " ", "vii°"}
    };

    public static String GetDegreeLabel(SimpleLabelType tpe, int degree) {
        return Labels[tpe.getValue()][degree%12];
    }

    public static String GetToneLabel(SimpleLabelType tpe, int tone) {
        return Labels[tpe.getValue()][tone%12];
    }
}
