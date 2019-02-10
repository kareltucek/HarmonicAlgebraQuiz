package cz.ktweb.harmonicalgebraquiz;

enum KeyType {
    c(0, 0, true, "C"),
    trivial(0, 0, false, "random 0 #/b"),
    easy(1, 0, false, "random 1b-1#"),
    easy2(2, 0, false, "random 2b-2#"),
    easy_med(3, 0, false, "random 3b-3#"),
    medium(5, 0, false, "random 5b-5#"),
    hard(7, 0, false, "random 7b-7#"),
    ges(0, 6, true, "Gb/F#"),
    des(0, 1, true, "Db/C#"),
    as(0, 8, true, "Ab"),
    ces(0, 3, true, "Eb/D#"),
    bes(0, 10, true, "Bb"),
    f(0, 5, true, "F"),
    c2(0, 0, true, "C"),
    g(0, 7, true, "G"),
    d(0, 2, true, "D"),
    a(0, 9, true, "A"),
    e(0, 4, true, "E"),
    b(0, 11, true, "B/Cb"),
    fis(0, 6, true, "F#/Gb")
    //easy_changing(5, 2, true, "changing 2#/b"),
    //medium_changing(6, 4, true, "changing 4#/b"),
    //hard_changing(7, 7, true, "changing 7#/b")
    ;

    private final int value;
    private final String label;
    private final int diff;
    private final int tonic;
    private final boolean absolute;
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
}
