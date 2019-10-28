package cz.ktweb.harmonicalgebraquiz;

import android.util.Pair;

import java.util.Random;

enum KeyType {
    c(0, 0, true, "C"),
    trivial(0, 0, false, "random 0 #/b"),
    easy(1, 0, false, "random 1b-1#"),
    easy2(2, 0, false, "random 2b-2#"),
    easy_med(3, 0, false, "random 3b-3#"),
    medium(4, 0, false, "random 4b-4#"),
    medium2(5, 0, false, "random 5b-5#"),
    medium3(6, 0, false, "random 6b-6#"),
    hard(7, 0, false, "random 7b-7#"),
    ges(0, 6, true, "Gb/F#"),
    des(0, 1, true, "Db/C#"),
    as(0, 8, true, "Ab/G#"),
    ces(0, 3, true, "Eb/D#"),
    bes(0, 10, true, "Bb"),
    f(0, 5, true, "F"),
    c2(0, 0, true, "C"),
    g(0, 7, true, "G"),
    d(0, 2, true, "D"),
    a(0, 9, true, "A"),
    e(0, 4, true, "E"),
    b(0, 11, true, "B/Cb"),
    fis(0, 6, true, "F#/Gb"),
    ca(0, 0, true, "random C or A")
    ;

    private final int value;
    private final String label;
    private final int diff;
    private final int tonic;
    private final boolean absolute;
    private static Random rnd = new Random();
    private KeyType(int diff, int offset, boolean absolute, String lab) {
        this.value = this.ordinal();
        this.label = lab;
        this.diff = diff;
        this.absolute = absolute;
        this.tonic = offset;
    }
    public static KeyType ByValue(int value) {
        for(KeyType type : KeyType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return trivial;
    }
    public int Position() {
        return value;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[KeyType.values().length];
        for(KeyType type : KeyType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }

    public int Tonic() {
        return this.tonic;
    }


    public boolean Absolute() {
        return this.absolute;
    }

    public int Difficulty() {
        return this.diff;
    }

    private boolean isSharpRestricted() {
        return this == trivial || this == easy || this == easy2 || this == easy_med || this == medium || this == medium2 || this == medium3 || this == hard;
    }

    private boolean isTonicRestricted() {
        return !isSharpRestricted();
    }

    public Pair<Integer, ScaleType> SelectScale(ScaleType tpe) {
        if(this == ca) {
            ScaleType newType = tpe.ResolveRandomModes();
            return new Pair(rnd.nextBoolean() ? 0 : 9, newType);
        }
        else if(isTonicRestricted()) {
            ScaleType newType = tpe.ResolveRandomModes();
            return new Pair(Tonic(), newType);
        } else {
            int sharps = rnd.nextInt(Difficulty() * 2 + 1) - Difficulty();
            ScaleType newType = tpe.ResolveRandomModes();
            return new Pair(newType.GetTonicFromSharps(sharps), newType);
        }
    }
}
