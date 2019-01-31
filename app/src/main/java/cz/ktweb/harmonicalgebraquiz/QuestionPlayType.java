package cz.ktweb.harmonicalgebraquiz;


enum QuestionPlayType {
    play_current(0, false, false, false, 0, false, "Current"),
    play_last(1, false, true, false, 0, false, "Last + Current"),
    play_cadence(2, true, false, false, 0, false, "Cadence + Current"),
    play_tonic(3, false, false, true, 0, false, "Tonic + Current"),
    play_cadence_last(4, true, true, false, 0, false, "Cadence + Last + Current"),
    play_tonic_last(5, false, true, true, 0, false, "Tonic + Last + Current"),
    play_listening(6, false, false, false, 0, true, "Current + Continue"),
    play_training(7, false, false, false, 1, true, "Current + Delay + Continue"),
    play_listening3(8, false, true, false, 0, true, "Last + Current + Continue"),
    play_listening2(9, false, true, false, 1, true, "Last + Current + Delay + Continue"),
    ;

    private final int value;
    private final String label;
    private final boolean cadence;
    private final boolean last;
    private final boolean tonic;
    private final int delay;
    private final boolean continu;
    private QuestionPlayType(int value, boolean cadence, boolean last, boolean tonic, int delay, boolean continu, String lab) {
        this.value = value;
        this.label = lab;
        this.last = last;
        this.cadence = cadence;
        this.delay = delay;
        this.continu = continu;
        this.tonic = tonic;
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
        return this.cadence;
    }

    public boolean Last() {
        return this.last;
    }

    public boolean Continuu() {
        return this.continu;
    }

    public int Delay() {
        return this.delay;
    }

    public boolean Tonic() {
        return this.tonic;
    }
}
