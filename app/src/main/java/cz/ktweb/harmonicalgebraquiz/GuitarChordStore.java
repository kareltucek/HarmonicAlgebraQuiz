package cz.ktweb.harmonicalgebraquiz;

public class GuitarChordStore {
    public static GuitarChord[] basicChords = {
            //A (transposition base)
            new GuitarChord(Tonic.a, ChordType.major,  new int[]{0, 0, 2, 2, 2, 0}, 0),
            new GuitarChord(Tonic.a, ChordType.major7,  new int[]{0, 0, 2, 0, 2, 0}, 0),
            new GuitarChord(Tonic.a, ChordType.minor,  new int[]{0, 0, 2, 2, 1, 0}, 0),
            new GuitarChord(Tonic.a, ChordType.minor7,  new int[]{0, 0, 2, 0, 1, 0}, 0),
            new GuitarChord(Tonic.a, ChordType.dim7,  new int[]{1, 2, 1, 2}, 2),

            //A#
            new GuitarChord(Tonic.ais, ChordType.dim7,  new int[]{2, 3, 2, 3}, 2),

            //B is transposable
            new GuitarChord(Tonic.b, ChordType.major7,  new int[]{2, 1, 2, 0, 2}, 1),
            new GuitarChord(Tonic.b, ChordType.dim7,  new int[]{0, 1, 0 ,1}, 2),

            //C
            new GuitarChord(Tonic.c, ChordType.major,  new int[]{3, 3, 2, 0, 1, 0}, 0),
            new GuitarChord(Tonic.c, ChordType.major7,  new int[]{3, 3, 2, 3, 1, 0}, 0),
            new GuitarChord(Tonic.c, ChordType.dim7,  new int[]{1, 2, 1, 2}, 2),

            //C#
            new GuitarChord(Tonic.cis, ChordType.dim7,  new int[]{2, 3, 2, 3}, 2),

            //D
            new GuitarChord(Tonic.d, ChordType.major,  new int[]{0, 2, 3, 2}, 2),
            new GuitarChord(Tonic.d, ChordType.major7,  new int[]{0, 2, 1, 2}, 2),
            new GuitarChord(Tonic.d, ChordType.minor,  new int[]{0, 2, 3, 1}, 2),
            new GuitarChord(Tonic.d, ChordType.minor7,  new int[]{0, 2, 1, 1}, 2),
            new GuitarChord(Tonic.d, ChordType.dim7,  new int[]{0, 1, 0 ,1}, 2),

            //D#
            new GuitarChord(Tonic.dis, ChordType.dim7,  new int[]{1, 2, 1, 2}, 2),

            //E (transposition base)
            new GuitarChord(Tonic.e, ChordType.major,  new int[]{0, 2, 2, 1, 0, 0}, 0),
            new GuitarChord(Tonic.e, ChordType.major7,  new int[]{0, 2, 2, 1, 3, 0}, 0),
            new GuitarChord(Tonic.e, ChordType.minor,  new int[]{0, 2, 2, 0, 0, 0}, 0),
            new GuitarChord(Tonic.e, ChordType.minor7,  new int[]{0, 2, 2, 0, 3, 0}, 0),
            new GuitarChord(Tonic.e, ChordType.dim7,  new int[]{2, 3, 2, 3}, 2),

            //F is transposable
            new GuitarChord(Tonic.f, ChordType.dim7,  new int[]{0, 1, 0 ,1}, 2),

            //F#
            new GuitarChord(Tonic.fis, ChordType.dim7,  new int[]{1, 2, 1, 2}, 2),

            //G
            new GuitarChord(Tonic.g, ChordType.major,  new int[]{3, 2, 0, 0, 0, 3}, 0),
            new GuitarChord(Tonic.g, ChordType.major7,  new int[]{3, 2, 0, 0, 0, 1}, 0),
            new GuitarChord(Tonic.g, ChordType.dim7,  new int[]{2, 3, 2, 3}, 2),

            //G#
            new GuitarChord(Tonic.gis, ChordType.dim7,  new int[]{0, 1, 0 ,1}, 2),
    };

    public static GuitarChord[] transposables = {
            new GuitarChord(Tonic.e, ChordType.major,  new int[]{0, 2, 2, 1, 0, 0}, 0),
            new GuitarChord(Tonic.e, ChordType.major7,  new int[]{0, 2, 2, 1, 3, 0}, 0),
            new GuitarChord(Tonic.e, ChordType.minor,  new int[]{0, 2, 2, 0, 0, 0}, 0),
            new GuitarChord(Tonic.e, ChordType.minor7,  new int[]{0, 2, 2, 0, 3, 0}, 0),

            new GuitarChord(Tonic.a, ChordType.major,  new int[]{0, 0, 2, 2, 2, 0}, 0),
            new GuitarChord(Tonic.a, ChordType.major7,  new int[]{0, 0, 2, 0, 2, 0}, 0),
            new GuitarChord(Tonic.a, ChordType.minor,  new int[]{0, 0, 2, 2, 1, 0}, 0),
            new GuitarChord(Tonic.a, ChordType.minor7,  new int[]{0, 0, 2, 0, 1, 0}, 0),
    };


}
