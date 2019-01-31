package cz.ktweb.harmonicalgebraquiz;

enum TempoType {
    gravec_100(0, 32, "32bpm - Grave"),
    largo(1, 44, "44bpm - Largo"),
    lento(2, 56, "56bpm - Lento"),
    adgagio(3, 66, "66bpm - Adagio"),
    andante(4, 88, "88bpm - Andante"),
    andante_moderato(5, 96, "96bpm - Andantino"),
    moderato(6, 112, "112bpm - Moderato"),
    allegro(7, 144, "144bpm - Allegro"),
    presto(8, 176, "176bpm - Presto")
    ;

    private final int value;
    private final int tempo;
    private final String label;
    private TempoType(int value, int tempo, String lab) {
        this.value = value;
        this.label = lab;
        this.tempo = tempo;
    }
    public int Position() {
        return value;
    }

    public static TempoType ByValue(int value) {
        for(TempoType type : TempoType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return andante;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[TempoType.values().length];
        for(TempoType type : TempoType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }

    public int Tempo() {
        return tempo;
    }

}
