package cz.ktweb.harmonicalgebraquiz;


enum KeyType {
    c(0, 0, "C"),
    trivial(1, 0, "0 #/b"),
    easy(2, 2, "2#/b"),
    medium(3, 4, "4#/b"),
    hard(4, 7, "7#/b"),
    //easy_changing(5, 2, true, "changing 2#/b"),
    //medium_changing(6, 4, true, "changing 4#/b"),
    //hard_changing(7, 7, true, "changing 7#/b")
    ;

    private final int value;
    private final String label;
    private final int diff;
    private KeyType(int value, int diff,  String lab) {
        this.value = value;
        this.label = lab;
        this.diff = diff;
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


    public int Difficulty() {
        return this.diff;
    }
}
