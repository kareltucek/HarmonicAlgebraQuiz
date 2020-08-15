package cz.ktweb.harmonicalgebraquiz;

import android.util.Pair;

import java.util.Random;

enum StimuliType {
    tones("Tone mode"),
    accompanied_tones("Accompanied tone mode"),
    accompanied_tones_inversion("Accompanied tone mode with inversions"),
    chord("Chord mode"),
    chord_inversion("Chord inversion mode"),
    guitar_chord("Guitar chord mode")
    ;

    private final int value;
    private final String label;
    private StimuliType(String lab) {
        this.value = this.ordinal();
        this.label = lab;
    }
    public static StimuliType ByValue(int value) {
        for(StimuliType type : StimuliType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return tones;
    }
    public int Position() {
        return value;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[StimuliType.values().length];
        for(StimuliType type : StimuliType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }
}
