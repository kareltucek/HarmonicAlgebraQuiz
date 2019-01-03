package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    major_flats(5);

    private final int value;
    private LabelType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
        layout.addView(button);
    }

    void addButtons() {
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

    String[][] Labels = {
            {"c", " ", "d", " ", "e", "f", " ", "g", " ", "a", " ", "b"},
            {"c", "db", "d", "eb", "e", "f", "gb", "g", "ab", "a", "bb", "b"},
            {"c", "db", "d", "eb", "e", "f", "gb", "g", "ab", "a", "bb", "b"},
            {"C", " ", "D", " ", "E", "F", " ", "G", " ", "A", " ", "B"},
            {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"},
            {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"}
    };

    String[][] FifthCircle = {
            {"db", "ab", "eb", "bb", "f", "c", "g", "d", "a", "e", "b", "f#", "c#", "g#", "d#", "a#", "b#"},
            {"Fb", "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#", "G#"},
    };
    int[] FifthCenters = {
            9,
            0
    };
    int[][] Positions = {
            {5, 7, 3, 8, 10},
            {5, 7, 9, 2, 4}
    };
    String[][] Questions = {
            {"s", "d", "T", "S", "D", "t"},
            {"S", "D", "t", "s", "d", "T"}
    };
    int[][] QuestionsTypes = {
            {0, 0, 1, 1, 1, 0},
            {1, 1, 0, 0, 0, 1}
    };
    int Center = 8;
    int QuestionCount = 0;
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
        if(keyPos > Center) {
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
    }

    void generateQuestion() {
        if(Config.QType == 0) {
            generateAlgebraQuestion();
        }
        else {
            generateSigQuestion();
        }
    }

    void generateSigQuestion() {
        CurrentSet++;
        if(CurrentSet > Config.Sets) {
            Finish();
        }
        int setSize = Config.Difficulty * 2 + 1;
        setQuestionCount(setSize*2);
        for(int type = 0; type < 2; type++) {
            int[] perm = generatePermutation(setSize);
            for(int i = 0; i < setSize; i++) {
                int q = type*setSize + i;
                int keyPos = Center-Config.Difficulty + perm[i];
                int sharps = keyPos - Center;
                LabelTypes[q] = type == 1 ? LabelType.major_any : LabelType.minor_any;
                TonicString[q] = type == 1 ? "Major" : "Minor";
                Answers[q] = decodeKey(type, keyPos);
                if(sharps == 0) {
                    QuestionStrings[q] = "0 #/b";
                } else if (sharps > 0) {
                    QuestionStrings[q] = "" + sharps + "#";
                } else {
                    QuestionStrings[q] = "" + (-sharps) + "b";
                }
            }
        }
    }

    void generateAlgebraQuestion(){
        int passes = 2;
        setQuestionCount(passes*5);
        int type = rnd.nextInt(2);
        int tonicPosition = rnd.nextInt(1+2*Config.Difficulty) + Center - Config.Difficulty;
        int tonic = decodeKey(type, tonicPosition);
        int[] perm = new int[QuestionCount];
        for(int i = 0; i < passes; i++) {
            int[] p = generatePermutation(5);
            for(int j = 0; j < 5; j++) {
                perm[i*5+j] = p[j];
            }
        }
        for(int i = 0; i < perm.length; i++) {
            TonicString[i] = Questions[type][5] + " = " + FifthCircle[type][tonicPosition];
            QuestionStrings[i] = Questions[type][perm[i]] + "";
            Answers[i] = (tonic + Positions[type][perm[i]]) % 12;
            LabelTypes[i] = decodeLabel(QuestionsTypes[type][perm[i]], tonicPosition);
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

    public void setupButtonProps(Button b, boolean red, boolean green) {
        int r = red ? 50 : 0;
        int g = green ? 50 : 0;
        int dark = !(boolean)b.getTag(R.id.colour) ? 50 : 0;
        b.getBackground().setColorFilter(Color.rgb(Config.BasicDarkness -g - dark,Config.BasicDarkness - r - dark,Config.BasicDarkness - r - g - dark), PorterDuff.Mode.MULTIPLY);
        b.setText(Labels[LabelTypes[CurrentQuestion].getValue()][(int)b.getTag(R.id.tone)]);
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
