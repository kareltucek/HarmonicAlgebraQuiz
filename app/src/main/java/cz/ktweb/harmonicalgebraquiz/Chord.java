package cz.ktweb.harmonicalgebraquiz;

import java.util.Arrays;

enum ChordType {
    major(new int[]{0,4,7}),
    major8(new int[]{0,4,7,12}),
    minor(new int[]{0,3,7}),
    minor8(new int[]{0,3,7,12}),
    major7(new int[]{0,4,7,10}),
    dim(new int[]{0,3,6}),
    dim7(new int[]{0,3,6,9}),
    dummy(new int[]{});

    private final int[] degrees;

    private ChordType(int[] degrees) {
        this.degrees = degrees;
    }

    public int[] Degrees() {
        return degrees;
    }
}

enum Degree{
    I(0),
    ii(1),
    II(2),
    iii(3),
    III(4),
    IV(5),
    V(7),
    vi(8),
    VI(9),
    vii(10),
    VII(11),
    VIII(12);

    private final int value;

    private Degree(int value) {
        this.value = value;
    }

    public int Value() {
        return value;
    }
}

public class Chord {
    public Degree degree;
    public ChordType tpe;
    public String label;

    public Chord(Degree degree, ChordType tpe, String label) {
        this.degree = degree;
        this.tpe = tpe;
        this.label = label;
    }

    //inversion == number of inverted tones
    //inversionDir = -1,1; 0 = off
    public int[] Resolve(int tonic, boolean normalize, int inversion, int inversionDir) {
        int[] res = new int[tpe.Degrees().length];
        for(int i = 0; i < res.length; i++) {
            int inversionShift = 0;
            if(inversionDir != 0) {
                if(inversionDir > 0 && i < inversion) {
                    inversionShift = 12;
                }
                if(inversionDir < 0 && i >= res.length - inversion ) {
                    inversionShift -= 12;
                }
            }
            res[i] = tonic+degree.Value()+tpe.Degrees()[i]+inversionShift;
            if(normalize) {
                res[i] = res[i] % 12 + (tpe.Degrees()[i] >= 12 ? 12 : 0);
            }
        }
        Arrays.sort(res);
        return res;
    }
}
