package cz.ktweb.harmonicalgebraquiz;


enum SetCountType {
    c_e100(1000000, 100, "endless, 100/set"),
    c_e20(1000000, 20, "endless, 20/set"),
    c_e5(1000000, 5, "endless, 5/set"),
    c_e1(1000000, 1, "endless, 1/set"),
    c_100(100, 100, "100 questions"),
    c_100_20(100, 20, "100 questions, 20/set"),
    c_100_5(100, 5, "100 questions, 5/set"),
    c_100_1(100, 1, "100 questions, 1/set"),
    c_20(20, 20, "20 questions"),
    c_20_5(20, 5, "20 questions, 5/set"),
    c_20_1(20, 1, "20 questions, 1/set"),
    ;

    private final int value;
    private final int inQuiz;
    private final int inSet;
    private final String label;
    private SetCountType(int inQuiz, int inSet, String lab) {
        this.value = this.ordinal();
        this.label = lab;
        this.inQuiz = inQuiz;
        this.inSet = inSet;
    }
    public int Position() {
        return value;
    }

    public static SetCountType ByValue(int value) {
        for(SetCountType type : SetCountType.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return c_100;
    }
    public static String[] GetLabelArray() {
        String[] res = new String[SetCountType.values().length];
        for(SetCountType type : SetCountType.values()) {
            res[type.value] = type.label;
        }
        return res;
    }

    public int QuestionsInQuiz() {
        return inQuiz;
    }

    public int QuestionsInSet() {
        return inSet;
    }

}
