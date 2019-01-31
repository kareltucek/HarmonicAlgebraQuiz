package cz.ktweb.harmonicalgebraquiz;

enum ChordType {
    major(new int[]{0,4,7}),
    major8(new int[]{0,4,7,12}),
    minor(new int[]{0,3,7}),
    minor8(new int[]{0,3,7,12}),
    major7(new int[]{0,4,7,10}),
    dim(new int[]{0,3,6});

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

    public Chord(Degree degree, ChordType tpe) {
        this.degree = degree;
        this.tpe = tpe;
    }

    public int[] Resolve(int tonic, boolean normalize) {
        int[] res = new int[tpe.Degrees().length];
        for(int i = 0; i < res.length; i++) {
            res[i] = tonic+degree.Value()+tpe.Degrees()[i];
            if(normalize) {
                res[i] = res[i] % 12 + (tpe.Degrees()[i] >= 12 ? 12 : 0);
            }
        }
        return res;
    }
}
