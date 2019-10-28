package cz.ktweb.harmonicalgebraquiz;

import static cz.ktweb.harmonicalgebraquiz.QuestionPlaytypeConstants.*;

class QuestionPlaytypeConstants {
    public static final int LAST = 1;
    public static final int CURRENT = 2;
    public static final int NEXT = 4;
    public static final int CADENCE = 8;
    public static final int TONIC = 16;
    public static final int CONTINUE = 32;
    public static final int DELAY = 64;
    public static final int LAST2 = 128;
}

enum QuestionPlayType {
    play_current(CURRENT, "Current"),
    play_last(LAST | CURRENT, "Last + Current"),
    play_last2(LAST2 | CURRENT, "Last two + Current"),
    play_cadence(CADENCE | CURRENT, "Cadence + Current"),
    play_tonic(TONIC | CURRENT, "Tonic + Current"),
    play_cadence_last( CADENCE | LAST | CURRENT, "Cadence + Last + Current"),
    play_tonic_last( TONIC | LAST | CURRENT, "Tonic + Last + Current"),

    //Continue section
    play_listening( CURRENT | CONTINUE, "Current + Continue"),
    play_training( CURRENT | DELAY | CONTINUE, "Current + Delay + Continue"),
    play_listening3( LAST | CURRENT | CONTINUE, "Last + Current + Continue"),
    play_listening2( LAST | CURRENT | DELAY | CONTINUE, "Last + Current + Delay + Continue"),
    //play_next1(LAST | CURRENT | NEXT, "Last + Current + Next"),
    //play_next21(CURRENT | NEXT, "Current + Next"),
    ;

    private final int value;
    private final String label;
    private final int mask;
    //private QuestionPlayType(int value, boolean cadence, boolean last, boolean tonic, int delay, boolean continu, String lab) {
    private QuestionPlayType(int mask, String lab) {
        this.value = this.ordinal();
        this.label = lab;
        this.mask = mask;
    }
    public static QuestionPlayType ByValue(int value) {
        for(QuestionPlayType type : QuestionPlayType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return play_current;
    }
    public int Position() {
        return value;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[QuestionPlayType.values().length];
        for(QuestionPlayType type : QuestionPlayType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }


    public boolean Cadence() {
        return (mask & CADENCE) > 0;
    }

    public boolean Last() {
        return (mask & (LAST | LAST2)) > 0;
    }

    public boolean Last2() {
        return (mask & LAST2) > 0;
    }

    public boolean Continuu() {
        return (mask & CONTINUE) > 0;
    }

    /*return specifies relative of the delay*/
    public float Delay() {
        return (mask & DELAY) > 0 ? 1 : 0;
    }

    public boolean Tonic() {
        return (mask & TONIC) > 0;
    }

    public boolean Next() {
        return (mask & NEXT) > 0;
    }
}
