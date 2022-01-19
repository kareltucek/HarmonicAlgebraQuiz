package cz.ktweb.harmonicalgebraquiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum Tonic{
    c(0),
    cis(1),
    d(2),
    dis(3),
    e(4),
    f(5),
    fis(6),
    g(7),
    gis(8),
    a(9),
    ais(10),
    b(11),
;

    private final int value;

    private Tonic(int value) {
        this.value = value;
    }

    public int Value() {
        return value;
    }
}


public class GuitarChord {
    public Tonic tonic;
    public ChordType tpe;
    public int[] chordTab;
    public int firstToneOffset;


    private static int[] offsets = {
            Tonic.e.Value(),
            Tonic.e.Value() + 5,
            Tonic.e.Value() + 2*5,
            Tonic.e.Value() + 3*5,
            Tonic.e.Value() + 3*5+4,
            Tonic.e.Value() + 4*5+4,
    };

    public GuitarChord(Tonic tonic, ChordType tpe, int[] chordTab, int firstToneOffset) {
        this.tonic = tonic;
        this.tpe = tpe;
        this.chordTab = chordTab;
        this.firstToneOffset = firstToneOffset;
    }

    public int[] ResolveTones(int root) {
        int[] res = new int[chordTab.length];

        for (int i = 0; i < chordTab.length; i++) {
            res[i] = chordTab[i] + offsets[i+firstToneOffset] + root;
        }

        return res;
    }
}

