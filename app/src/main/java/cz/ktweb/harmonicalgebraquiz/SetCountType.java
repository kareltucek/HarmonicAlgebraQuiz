package cz.ktweb.harmonicalgebraquiz;


enum SetCountType {
    c_100(0, 100, 100, "100 questions"),
    c_100_20(1, 100, 20, "100 questions, 20/set"),
    c_100_5(2, 100, 5, "100 questions, 5/set"),
    c_20(3, 20, 20, "20 questions"),
    c_20_5(4, 20, 5, "20 questions, 5/set"),
    c_20_1(5, 20, 1, "20 questions, 1/set"),
    ;

    private final int value;
    private final int inQuiz;
    private final int inSet;
    private final String label;
    private SetCountType(int value, int inQuiz, int inSet, String lab) {
        this.value = value;
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
