package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.Random;

enum LabelType {
    minor_any(0),
    minor_sharps(1),
    minor_flats(2),
    major_any(3),
    major_sharps(4),
    major_flats(5),
    major_tsd(6),
    flatsharp(7);

    private final int value;
    private LabelType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getMinorValue() {
        return value > 2 ? value - 3 : value;
    }
    public int getMajorValue() {
        return value < 3 ? value + 3 : value;
    }
}

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Random rnd = new Random();

    void addSpace(int weight, int layoutId) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100);
        lp.weight = weight;
        LinearLayout layout = (LinearLayout) findViewById(layoutId);
        Space space = new Space(getApplicationContext());
        space.setLayoutParams(lp);
        layout.addView(space);
    }

    void addButton(int tone, int layoutId) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100);
        lp.weight = 2;
        LinearLayout layout = (LinearLayout) findViewById(layoutId);
        Button button = new Button(getApplicationContext());
        button.setTag(R.id.tone, tone);
        button.setTag(R.id.colour, layoutId == R.id.whites);
        button.setLayoutParams(lp);
        button.setOnClickListener(this);
        button.setTextAppearance(R.style.AppTheme);
        button.setAllCaps(false);
        button.setSingleLine(true);
        button.setEllipsize(TextUtils.TruncateAt.END);
        layout.addView(button);
    }

    void addButtonsPiano() {
        int i = 0;
        addSpace(1, R.id.blacks);
        addButton(i++, R.id.whites);
        addButton(i++, R.id.blacks);
        addButton(i++, R.id.whites);
        addButton(i++, R.id.blacks);
        addButton(i++, R.id.whites);
        addSpace(2, R.id.blacks);
        addButton(i++, R.id.whites);
        addButton(i++, R.id.blacks);
        addButton(i++, R.id.whites);
        addButton(i++, R.id.blacks);
        addButton(i++, R.id.whites);
        addButton(i++, R.id.blacks);
        addButton(i++, R.id.whites);
        addSpace(2, R.id.blacks);
        addButton(0, R.id.whites);
        addSpace(1, R.id.blacks);

    }

    void addButtonsFlatSharp() {
        addButton(0, R.id.whites);
        addButton(0, R.id.blacks);
        for(int i = 0; i < 7; i++) {
            addButton(i+1, R.id.whites);
        }
        for(int i = 0; i < 7; i++) {
            addButton(i+11, R.id.blacks);
        }
    }

    void addButtons() {
        if(Config.QType == 1 && Config.InvertedMode) {
            addButtonsFlatSharp();
        } else {
            addButtonsPiano();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        CurrentSet = 0;
        addButtons();
        newSet();

        ((TextView)findViewById(R.id.correct)).setTextColor(Config.Green);
        ((TextView)findViewById(R.id.wrooong)).setTextColor(Config.Red);
    }

    enum majorFunction {
        major,
        minor,
        diminished,
        none
    }

    majorFunction[] Functions = {
            majorFunction.major,
            majorFunction.none,
            majorFunction.minor,
            majorFunction.none,
            majorFunction.minor,
            majorFunction.major,
            majorFunction.none,
            majorFunction.major,
            majorFunction.none,
            majorFunction.minor,
            majorFunction.none,
            majorFunction.diminished
    };

    String[][] Labels = {
            {"c", " ", "d", " ", "e", "f", " ", "g", " ", "a", " ", "b"},
            {"c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "b"},
            {"c", "db", "d", "eb", "e", "f", "gb", "g", "ab", "a", "bb", "b"},
            {"C", " ", "D", " ", "E", "F", " ", "G", " ", "A", " ", "B"},
            {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"},
            {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"},
            {"T", " ", "s", " ", "d", "S", " ", "D", " ", "t", " ", "dim"},

    };

    String[][] FifthCircle = {
            {"gb", "db", "ab", "eb", "bb", "f", "c", "g", "d", "a", "e", "b", "f#", "c#", "g#", "d#", "a#", "e#", "b#"},
            {"Bbb", "Fb", "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#", "G#", "D#"},
    };
    int[] FifthCenters = {
            9,
            0
    };
    int[][] Positions = {
            {5, 7, 3, 8, 10, 0, 2},
            {5, 7, 9, 2, 4, 0, 11}
    };
    int[][] FifthPositions = {
            {-1, 1, 0, -1, 1, 0, 2},
            {-1, 1, 0, -1, 1, 0, 2}
    };
    String[][] Questions = {
            {"s", "d", "T", "S", "D", "t", "dim"},
            {"S", "D", "t", "s", "d", "T", "dim"}
    };
    int[][] QuestionsTypes = {
            {0, 0, 1, 1, 1, 0, 0},
            {1, 1, 0, 0, 0, 1, 0}
    };
    int Center = 9;
    int QuestionCount = 0;
    int[] TonicTypes = new int[QuestionCount];
    int[] TonicIds = new int[QuestionCount];
    String[] TonicString = new String[QuestionCount];
    String[] QuestionStrings = new String[QuestionCount];
    LabelType[] LabelTypes = new LabelType[QuestionCount];
    int[] Answers = new int[QuestionCount];
    int CurrentQuestion = 0;
    int CurrentSet = 0;

    int Answered = 0;
    int Correct = 0;
    int Wrong = 0;
    boolean WasCorrect = true;
    boolean WasWrong = true;

    int[] generatePermutation(int size) {
        int[] arr = new int[size];
        for(int i = 0; i < size; i++) {
            boolean ok = false;
            while(ok == false) {
                ok = true;
                arr[i] = rnd.nextInt(size);
                for (int j = 0; j < i; j++) {
                    ok &= arr[i] != arr[j];
                }
            }
        }
        return arr;
    }

    int decodeKey(int type, int position) {
        return (FifthCenters[type] + (position-Center)*7 + 12*7) % 12;
    }

    LabelType decodeLabel(int type, int keyPos) {
        if (keyPos > Center) {
            return type == 1 ? LabelType.major_sharps : LabelType.minor_sharps;
        } else if (keyPos < Center) {
            return type == 1 ? LabelType.major_flats : LabelType.minor_flats;
        } else {
            return type == 1 ? LabelType.major_any : LabelType.minor_any;
        }
    }

    void setQuestionCount(int c) {
        QuestionCount = c;
        QuestionStrings = new String[QuestionCount];
        Answers = new int[QuestionCount];
        TonicString = new String[QuestionCount];
        LabelTypes = new LabelType[QuestionCount];
        TonicIds = new int[QuestionCount];
        TonicTypes = new int[QuestionCount];
    }

    void generateQuestion() {
        CurrentSet++;
        if(CurrentSet > Config.Sets) {
            Finish();
        }
        if(Config.QType == 0) {
            generateAlgebraQuestion();
        }
        else {
            generateSigQuestion();
        }
    }

    void generateSigQuestion() {
        int setSize = Config.Difficulty * 2 + 1;
        setQuestionCount(setSize*2);
        int[] flipped = new int[setSize];
        for(int j = 0; j < 2; j++) {
            int[] perm = generatePermutation(setSize);
            for(int i = 0; i < setSize; i++) {
                int type = j;
                if (Config.InvertedMode && Config.ShuffleInvertedSigMode) {
                    if(type == 0) {
                        flipped[i] = rnd.nextInt(2);
                        type = flipped[i];
                    } else {
                        type = 1 - flipped[i];
                    }
                }
                Log.d("dbg", "type = " + type);
                int q = j*setSize + i;
                int keyPos = Center-Config.Difficulty + perm[i];
                int sharps = keyPos - Center;
                TonicIds[q] = -1;
                TonicTypes[q] = 0;
                if(Config.InvertedMode) {
                    LabelTypes[q] = LabelType.flatsharp;
                    TonicString[q] = "";
                    if(sharps >= 0) {
                        Answers[q] = sharps;
                    } else {
                        Answers[q] = 10 - sharps;
                    }
                    QuestionStrings[q] = FifthCircle[type][keyPos];
                    Log.d("dbg", "Qstr = " + QuestionStrings[q] );
                    Log.d("dbg", "Qstr tpe = " + type);
                }
                else {
                    LabelTypes[q] = type == 1 ? LabelType.major_any : LabelType.minor_any;
                    TonicString[q] = type == 1 ? "Major" : "Minor";
                    Answers[q] = decodeKey(type, keyPos);
                    if (sharps == 0) {
                        QuestionStrings[q] = "0 #/b";
                    } else if (sharps > 0) {
                        QuestionStrings[q] = "" + sharps + "#";
                    } else {
                        QuestionStrings[q] = "" + (-sharps) + "b";
                    }
                }
            }
        }
    }

    void generateAlgebraQuestion(){
        int setSize = Questions[0].length;
        setQuestionCount(Config.Passes*setSize);
        int type = rnd.nextInt(2);
        int tonicPosition = rnd.nextInt(1+2*Config.Difficulty) + Center - Config.Difficulty;
        int tonic = decodeKey(type, tonicPosition);
        int[] perm = new int[QuestionCount];
        for(int i = 0; i < Config.Passes; i++) {
            int[] p = generatePermutation(setSize);
            for(int j = 0; j < setSize; j++) {
                perm[i*setSize+j] = p[j];
            }
        }
        for(int i = 0; i < perm.length; i++) {
            if(Config.InvertedMode) {
                TonicIds[i] = tonic;
                TonicTypes[i] = type;
                TonicString[i] = Questions[type][5] + " = " + FifthCircle[type][tonicPosition];
                QuestionStrings[i] = FifthCircle[QuestionsTypes[type][perm[i]]][tonicPosition + FifthPositions[type][perm[i]]] + "";
                Answers[i] = (tonic + Positions[type][perm[i]]) % 12;
                LabelTypes[i] = decodeLabel(QuestionsTypes[type][perm[i]], tonicPosition);
            }
            else {
                TonicIds[i] = tonic;
                TonicTypes[i] = type;
                TonicString[i] = Questions[type][5] + " = " + FifthCircle[type][tonicPosition];
                QuestionStrings[i] = Questions[type][perm[i]] + "";
                Answers[i] = (tonic + Positions[type][perm[i]]) % 12;
                LabelTypes[i] = decodeLabel(QuestionsTypes[type][perm[i]], tonicPosition);
            }
        }
    }

    void clearButtons(int layoutId) {
        LinearLayout l = (LinearLayout)findViewById(layoutId);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                setupButtonProps((Button) v, false, false);
            }
        }
    }

    void UpdateQuestion() {
        TextView tonic = (TextView)findViewById(R.id.Tonic);
        TextView question = (TextView)findViewById(R.id.Question);
        tonic.setText(TonicString[CurrentQuestion]);
        question.setText(QuestionStrings[CurrentQuestion]);
        clearButtons(R.id.whites);
        clearButtons(R.id.blacks);
        ((TextView)findViewById(R.id.correct)).setText("" + Correct);
        ((TextView)findViewById(R.id.wrooong)).setText("" + Wrong);
        WasCorrect = true;
        WasWrong = false;
    }

    void newSet() {
        generateQuestion();
        CurrentQuestion = 0;
        UpdateQuestion();
    }

    String determineLabel(int tone) {
        if(Config.InvertedMode) {
            if(Config.QType == 0 ) {
                int majorTonic = TonicTypes[CurrentQuestion] == 1 ? TonicIds[CurrentQuestion] : (TonicIds[CurrentQuestion] + 3) % 12;
                int degree = (tone - majorTonic + 7 * 12) % 12;
                return Labels[LabelType.major_tsd.getValue()][degree];
            } else {
                if(tone < 10) {
                    return "" + tone + "#";
                } else {
                    return "" + (tone - 10) + "b";
                }
            }
        }
        else {
            if (Config.VisualMode && Config.QType == 0) {
                int majorTonic = TonicTypes[CurrentQuestion] == 1 ? TonicIds[CurrentQuestion] : (TonicIds[CurrentQuestion] + 3) % 12;
                int degree = (tone - majorTonic + 7 * 12) % 12;
                if (Functions[degree] == majorFunction.major) {
                    return Labels[LabelTypes[CurrentQuestion].getMajorValue()][tone];
                } else if (Functions[degree] == majorFunction.minor) {
                    return Labels[LabelTypes[CurrentQuestion].getMinorValue()][tone];
                } else if (Functions[degree] == majorFunction.diminished) {
                    return Labels[LabelTypes[CurrentQuestion].getMinorValue()][tone] + "dim";
                } else {
                    return "";
                }
            } else {
                return Labels[LabelTypes[CurrentQuestion].getValue()][tone];
            }
        }
    }

    public void setupButtonProps(Button b, boolean red, boolean green) {
        int tone = (int)b.getTag(R.id.tone);
        int r = red ? 50 : 0;
        int g = green ? 50 : 0;
        int black = !(boolean)b.getTag(R.id.colour) ? 50 : 0;
        int tonic = tone == TonicIds[CurrentQuestion] && Config.HighlightTonic ? 20 : 0;
        int dark = black + tonic;
        b.getBackground().setColorFilter(Color.rgb(Config.BasicDarkness -g - dark,Config.BasicDarkness - r - dark,Config.BasicDarkness - r - g - dark), PorterDuff.Mode.MULTIPLY);
        b.setText(determineLabel(tone));
    }

    public void onClick(View v)
    {
        clearButtons(R.id.whites);
        clearButtons(R.id.blacks);
        int answer = (int)v.getTag(R.id.tone);
        if(answer == Answers[CurrentQuestion]) {
            CurrentQuestion++;
            Answered++;
            Correct += WasCorrect ? 1 : 0;
            Wrong += WasWrong ? 1 : 0;
            if(CurrentQuestion == QuestionCount) {
                newSet();
                UpdateQuestion();
            }
            else {
                UpdateQuestion();
            }
            setupButtonProps((Button)v, false, false);
        } else {
            WasCorrect = false;
            WasWrong = true;
            setupButtonProps((Button)v, true, false);
        }
    }

    void giveUpButtons(int layoutId) {
        LinearLayout l = (LinearLayout)findViewById(layoutId);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                setupButtonProps((Button) v, false, (int)((Button)v).getTag(R.id.tone) == Answers[CurrentQuestion]);
            }
        }
    }


    public void onClickGiveUp(View v) {
        WasCorrect = false;
        giveUpButtons(R.id.whites);
        giveUpButtons(R.id.blacks);
    }

    public void Finish() {
        Intent i = new Intent(getApplicationContext(), ScoreActivity.class);
        Config.LastScore = (100 * (1+Correct)) / (Correct+Wrong+1);
        startActivity(i);
    }
}
