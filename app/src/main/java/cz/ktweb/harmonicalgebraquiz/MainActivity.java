package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.Random;

import static cz.ktweb.harmonicalgebraquiz.QuizType.EarQuiz;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Random rnd = new Random();
    boolean newSet = false;
    public static Player p = new Player();
    public static HandlerThread playThread;
    public static Handler playHandler;
    public static HandlerThread listeningThread;
    public static Handler listeningHandler;
    public static int playingActive = 0;
    public static boolean stopped = false;
    public static boolean wantTuning = false;
    public static GestureDetector gestureDetector = new GestureDetector();


    void addSpace(int weight, int layoutId, boolean seriously) {
        if(!seriously) {
            return;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100);
        lp.weight = weight;
        LinearLayout layout = (LinearLayout) findViewById(layoutId);
        Space space = new Space(getApplicationContext());
        space.setLayoutParams(lp);
        layout.addView(space);
    }

    void setButtonVisibility(int id, boolean visible) {
        Button b = (Button) findViewById(id);
        b.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    void addButton(int tone, int layoutId) {
        LinearLayout layout = (LinearLayout) findViewById(layoutId);
        addButtonIn(tone, layoutId == R.id.blacks, layout);
    }

    void addButtonIn(int tone, boolean isBlack, LinearLayout layout) {
        LinearLayout.LayoutParams lp;
        if(layout.getOrientation() == LinearLayout.VERTICAL) {
            lp = new LinearLayout.LayoutParams(100, 0);
        } else {
            lp = new LinearLayout.LayoutParams(0, 100);
        }
        lp.weight = 2;
        Button button = new Button(getApplicationContext());
        button.setTag(R.id.tone, tone);
        button.setTag(R.id.colour, isBlack);
        button.setLayoutParams(lp);
        button.setTextAppearance(R.style.AppTheme);
        button.setAllCaps(false);
        button.setSingleLine(true);
        button.setOnClickListener(this);
        button.setPadding(0,0,0,0);
        button.setEllipsize(TextUtils.TruncateAt.END);
        layout.addView(button);
    }

    void clearLayout(int id) {
        LinearLayout l = (LinearLayout)findViewById(id);
        l.removeAllViews();
    }

    void addStrings(int firstTone) {
        clearLayout(R.id.strings);
        clearLayout(R.id.blacks);
        clearLayout(R.id.whites);


        LinearLayout l = (LinearLayout)findViewById(R.id.strings);
        for(int i = 0; i < 4; i++) {
            LinearLayout string = new LinearLayout(this);
            string.setOrientation(LinearLayout.VERTICAL);
            string.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            l.addView(string);
            for(int j = 0; j < 8; j++) {
                int gPos = (firstTone + j) % 12;
                boolean highlight = false;
                highlight |= gPos == 7;
                highlight |= gPos == 0;
                highlight |= gPos == 2;
                addButtonIn((firstTone + j + i*7)%12, highlight, string);
            }
        }
    }

    void addButtonsPiano2(int tonicAt, int shiftBy, ScaleType tpe, boolean twoRow) {
        clearLayout(R.id.strings);
        clearLayout(R.id.blacks);
        clearLayout(R.id.whites);

        int whites = R.id.whites;
        int blacks = R.id.blacks;

        if(!twoRow) {
            blacks = whites;
        }

        int wh, bl;
        if(tpe.Contains(shiftBy%12)){
            wh = 1;
            bl = 2;
        } else
        {
            wh = 2;
            bl = 1;
        }

        addSpace(wh, whites, twoRow);
        addSpace(bl, blacks, twoRow);
        for(int i = 0; i < 13; i++ ) {
            int j = (i + shiftBy) % 12;
            if(tpe.Contains(j)) {
                addButton((tonicAt+j)%12, whites);
                if(i > 0 && tpe.Contains(j-1)) {
                    addSpace(2, blacks, twoRow);
                }
            } else {
                addButton((tonicAt+j)%12, blacks);
                if(i > 0 && !tpe.Contains(j-1)) {
                    addSpace( 2, whites, twoRow);
                }

            }
        }
        addSpace(wh, whites, twoRow);
        addSpace(bl, blacks, twoRow);
    }

    void addButtonsPiano() {
        addButtonsPiano2(0, 0, ScaleType.major, true);
    }

    void tuneButtonsSync(final int tonicAt) {
        switch(Config.TypeOfLayout) {
            case relative_native:
                addButtonsPiano2(tonicAt, 0, CurrentScaleType, true);
                break;
            case relative_cmaj:
                addButtonsPiano2(tonicAt, 0, ScaleType.major, true);
                break;
            case piano_shifted:
                addButtonsPiano2(0, tonicAt, ScaleType.major, true);
                break;
            case piano_cmaj:
                addButtonsPiano2(0, 0, ScaleType.major, true);
                break;
            case parallel_maj:
                addButtonsPiano2(CurrentScaleType.GetMajorTonic(tonicAt), 0, ScaleType.major, true);
                break;
            case parallel_min:
                addButtonsPiano2(CurrentScaleType.GetMajorTonic(tonicAt) + 9, 0, ScaleType.minor, true);
                break;
            case piano_parallel_maj:
                addButtonsPiano2(0, CurrentScaleType.GetMajorTonic(tonicAt), ScaleType.major, true);
                break;
            case piano_parallel_min:
                addButtonsPiano2(0, CurrentScaleType.GetMajorTonic(tonicAt) + 9, ScaleType.major, true);
                break;
            case linear:
                addButtonsPiano2(tonicAt, 0, ScaleType.major, false);
                break;
            case violin_1:
                addStrings(7);
                break;
            case violin_3:
                addStrings(12);
                break;
            case violin_4:
                addStrings(14);
                break;
        }
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
        if(Config.QType == QuizType.KeyQuiz && Config.InvertedMode) {
            addButtonsFlatSharp();
        } else {
            addButtonsPiano();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        p.setParent(this);

        CurrentSet = 0;
        addButtons();
        newSet();
        UpdateQuestion();

        setButtonVisibility(R.id.cadence, Config.QType == EarQuiz);
        setButtonVisibility(R.id.repeat, Config.QType == EarQuiz);
        setButtonVisibility(R.id.repeatLast, Config.QType == EarQuiz);


        ((TextView)findViewById(R.id.correct)).setTextColor(Config.Green);
        ((TextView)findViewById(R.id.wrooong)).setTextColor(Config.Red);

        if(playThread == null) {
            playThread = new HandlerThread("PlayThread");
            playThread.start();
            playHandler = new Handler(playThread.getLooper());
            listeningThread = new HandlerThread("ListeningThread");
            listeningThread.start();
            listeningHandler = new Handler(playThread.getLooper());
        }
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
    SimpleLabelType[] LabelTypes = new SimpleLabelType[QuestionCount];
    int[] Answers = new int[QuestionCount];
    int[] AnswersOctaved = new int[QuestionCount];
    boolean[] RepeatedMistake = new boolean[QuestionCount];
    int CurrentQuestion = 0;
    int CurrentSet = 0;

    int Answered = 0;
    int Correct = 0;
    int Wrong = 0;
    int WrongAttempt = 0;
    boolean WasCorrect = true;
    boolean WasWrong = true;

    int HighlightTone = -1;

    ScaleType CurrentScaleType = Config.TypeOfScale;

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

    /**
     * @param type, 0 == minor, 1 == major
     * @param position, idx into circleOfFifths
     * @return absolute tonic id
     */
    int decodeKeyFromIdx(int type, int position) {
        return (FifthCenters[type] + (position-Center)*7 + 12*7) % 12;
    }

    SimpleLabelType decodeLabel(int type, int keyPos) {
        if (keyPos > Center) {
            return type == 1 ? SimpleLabelType.major_sharps : SimpleLabelType.minor_sharps;
        } else if (keyPos < Center) {
            return type == 1 ? SimpleLabelType.major_flats : SimpleLabelType.minor_flats;
        } else {
            return type == 1 ? SimpleLabelType.major_any : SimpleLabelType.minor_any;
        }
    }

    void setQuestionCount(int c) {
        QuestionCount = c;
        QuestionStrings = new String[QuestionCount];
        Answers = new int[QuestionCount];
        TonicString = new String[QuestionCount];
        LabelTypes = new SimpleLabelType[QuestionCount];
        TonicIds = new int[QuestionCount];
        TonicTypes = new int[QuestionCount];
        AnswersOctaved = new int[QuestionCount];
        RepeatedMistake = new boolean[QuestionCount];
    }

    void generateQuestion() {
        CurrentSet++;
        if(CurrentSet > Config.Sets) {
            Finish();
        }
        if(Config.QType == QuizType.AlgebraQuiz) {
            generateAlgebraQuestion();
        }
        else if (Config.QType == QuizType.KeyQuiz){
            generateSigQuestion();
        } else if (Config.QType == EarQuiz){
            generateEarQuestion();
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
                int q = j*setSize + i;
                int keyPos = Center-Config.Difficulty + perm[i];
                int sharps = keyPos - Center;
                TonicIds[q] = -1;
                TonicTypes[q] = 0;
                if(Config.InvertedMode) {
                    LabelTypes[q] = SimpleLabelType.flatsharp;
                    TonicString[q] = "";
                    if(sharps >= 0) {
                        Answers[q] = sharps;
                    } else {
                        Answers[q] = 10 - sharps;
                    }
                    QuestionStrings[q] = FifthCircle[type][keyPos];
                }
                else {
                    LabelTypes[q] = type == 1 ? SimpleLabelType.major_any : SimpleLabelType.minor_any;
                    TonicString[q] = type == 1 ? "Major" : "Minor";
                    Answers[q] = decodeKeyFromIdx(type, keyPos);
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
        int tonic = decodeKeyFromIdx(type, tonicPosition);
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

    void generateEarQuestion(){
        int setSize = Config.EarSetSize;
        Pair<Integer, ScaleType> lastKey = TonicIds.length == 0 ? new Pair(-1, CurrentScaleType) : new Pair(TonicIds[0], CurrentScaleType); //needs to be set before setting QC
        setQuestionCount(setSize);
        if(Config.TypeOfRestriction.RequiresChromatic()) {
            Config.ChromaticMode = true; //otherwise learning mode would contain empty set
        }
        if(Config.TypeOfRestriction.BansChromatic()) {
            Config.ChromaticMode = false;
        }
        Pair<Integer, ScaleType> key = lastKey;
        for(int i = 0; i < 10 && lastKey.equals(key); i++) {
            key = Config.TypeOfKey.SelectScale(Config.TypeOfScale);
        }
        CurrentScaleType = key.second;
        int actualTonic = key.first.intValue();
        int sharpcount = CurrentScaleType.GetSharps(actualTonic);
        SimpleLabelType labelType = sharpcount >= 0 ? SimpleLabelType.major_sharps : SimpleLabelType.major_flats;
        for(int i = 0; i < setSize; i++)
        {
            int deg;
            int tone;
            do {
                int range = Config.TypeOfRestriction.To() - Config.TypeOfRestriction.From() + 1;
                deg = Config.TypeOfRestriction.From() + rnd.nextInt(range) + (Config.TypeOfRestriction.Absolute() ? -actualTonic : 0);
                tone = deg + actualTonic;
            } while (
                            (i > 0 && AnswersOctaved[i-1] == tone)
                        ||
                                    (!CurrentScaleType.Contains(deg) && !Config.ChromaticMode)
                        ||
                                    (!Config.TypeOfRestriction.Allows(CurrentScaleType, i, actualTonic, tone))
                        ||
                            (i > 0 && Config.LimitedIntervals && Math.abs(AnswersOctaved[i-1] - tone) > 12)
                    );
            TonicIds[i] = actualTonic;
            TonicTypes[i] = 1;
            TonicString[i] = CurrentScaleType.GetScaleName(actualTonic);
            QuestionStrings[i] = "";
            Answers[i] = (tone + 12) % 12;
            AnswersOctaved[i] = tone;
            LabelTypes[i] = labelType;
        }
        wantTuning = true;
    }

    void clearAllButtonsAsync() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearAllButtonsSync();
            }
        });
    }

    void clearAllButtonsSync() {
        if(wantTuning) {
            wantTuning = false;
            tuneButtonsSync(TonicIds[CurrentQuestion]);
        }
        LinearLayout l = (LinearLayout) findViewById(R.id.buttonBox);
        clearButtonsIn(l);
    }

    void clearButtonsIn(final LinearLayout l) {
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                setupButtonProps((Button) v, false, false);
                //((Button) v).invalidate();
            } else if (v instanceof LinearLayout) {
                clearButtonsIn((LinearLayout) v);
            }
        }
    }

    Button findToneButton(int tone) {
        LinearLayout l = (LinearLayout) findViewById(R.id.buttonBox);
        return findTone(l, (tone + 12*7) % 12);
    }

    Button findTone(LinearLayout l, int tone) {
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                if( (int)v.getTag(R.id.tone) == tone ) {
                    return (Button)v;
                }
            } else if (v instanceof LinearLayout) {
                Button b = findTone((LinearLayout)v, tone);
                if(b != null) {
                    return b;
                }
            }
        }
        return null;
    }

    void setText(int id, String text) {
        ((TextView)findViewById(id)).setText(text);
    }

    void UpdateQuestion() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tonic = (TextView)findViewById(R.id.Tonic);
                TextView question = (TextView)findViewById(R.id.Question);
                tonic.setText(TonicString[CurrentQuestion]);
                question.setText(QuestionStrings[CurrentQuestion]);
                clearAllButtonsSync();
                setText(R.id.correct, "" + Correct);
                setText(R.id.wrooong, "" + Wrong);
                setText(R.id.currentQ, "" + ((CurrentSet-1) * QuestionCount + CurrentQuestion));
                setText(R.id.qCount, "" + (Config.Sets * QuestionCount));
                WasCorrect = true;
                WasWrong = false;
            }
        });
    }

    void newSet() {
        generateQuestion();
        CurrentQuestion = 0;
        newSet = true;
    }

    String determineLabel(int tone) {
        if(Config.InvertedMode) {
            if(Config.QType == QuizType.AlgebraQuiz || Config.QType == EarQuiz) {
                int majorTonic = TonicTypes[CurrentQuestion] == 1 ? TonicIds[CurrentQuestion] : (TonicIds[CurrentQuestion] + 3) % 12;
                int degree = (tone - majorTonic + 7 * 12) % 12;
                return LabelStore.GetDegreeLabel(SimpleLabelType.major_tsd, degree);
            } else {
                if(tone < 10) {
                    return "" + tone + "#";
                } else {
                    return "" + (tone - 10) + "b";
                }
            }
        }
        else {
            if (Config.QType == EarQuiz && Config.HideLabels) {
                return "";
            }
            else if (Config.QType == EarQuiz && Config.EarIntervalLabels && CurrentQuestion > 0) {
                int difference = AnswersOctaved[CurrentQuestion] - AnswersOctaved[CurrentQuestion-1];
                if(difference >= 0) {
                    difference = (tone - AnswersOctaved[CurrentQuestion-1] + 7*12) % 12;
                    return (difference == 0 ? "" : "+") + LabelStore.GetDegreeLabel(SimpleLabelType.interval, difference);
                } else {
                    difference = (AnswersOctaved[CurrentQuestion-1] - tone + 7*12) % 12;
                    return (difference == 0 ? "" : "-") + LabelStore.GetDegreeLabel(SimpleLabelType.interval, difference);
                }
            } else if (Config.QType == EarQuiz) {
                switch(Config.TypeOfStimuli) {
                    case chord:
                    case chord_inversion:
                        return CurrentScaleType.GetToneLabel(ScaleLabelType.chord_name, TonicIds[CurrentQuestion], tone);
                    default:
                        return CurrentScaleType.GetToneLabel(ScaleLabelType.tone_name, TonicIds[CurrentQuestion], tone);
                }
            } {
                return LabelStore.GetToneLabel(LabelTypes[CurrentQuestion], tone);
            }
        }
    }

    public void setupButtonPropsAsync(final Button b, final boolean red, final boolean green) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupButtonProps(b, red, green);
            }
        });
    }

    public void setupButtonProps(final Button b, final boolean red, final boolean green) {
        int tone = (int)b.getTag(R.id.tone);
        int r = red ? 50 : 0;
        int g = green || tone == HighlightTone ? 50 : 0;
        int black = !(boolean)b.getTag(R.id.colour) ? 0 : 50;
        int preview = tone == currentNotePreview % 12 ? 10 : 0;
        int dark = black + preview;

        b.getBackground().setColorFilter(MathUtils.rgb(Config.bgRed -g - dark,Config.bgGreen - r - dark,Config.bgBlue - r - g - dark), PorterDuff.Mode.MULTIPLY);
        if(CurrentQuestion < QuestionCount) {
            b.setText(determineLabel(tone));
        }
    }

    public void setupTrickyQuestion(int previous, int right, int wrong) {
        int c = CurrentQuestion;
        if(c >= QuestionCount || c < 1) {
            return;
        }
        if(right == wrong) {
            if(c+Config.TrickyOffset < QuestionCount && !RepeatedMistake[c + Config.TrickyOffset]) {
                setupTrickyQuestionAt(c + Config.TrickyOffset, previous, right);
            }
            setupTrickyQuestionAt(c + Config.TrickyOffset * 2, previous, right);
            setupTrickyQuestionAt(c + Config.SecondTrickyOffset, previous, right);
        } else {
            if(rnd.nextBoolean()) {
                setupTrickyQuestionAt(c + Config.TrickyOffset, previous, right);
                setupTrickyQuestionAt(c + Config.TrickyOffset * 2, previous, wrong);
            } else {
                setupTrickyQuestionAt(c + Config.TrickyOffset, previous, wrong);
                setupTrickyQuestionAt(c + Config.TrickyOffset * 2, previous, right);
            }
            if(rnd.nextBoolean()) {
                setupTrickyQuestionAt(c + Config.SecondTrickyOffset, previous, right);
                setupTrickyQuestionAt(c + Config.SecondTrickyOffset + Config.TrickyOffset, previous, wrong);
            } else {
                setupTrickyQuestionAt(c + Config.SecondTrickyOffset, previous, wrong);
                setupTrickyQuestionAt(c + Config.SecondTrickyOffset + Config.TrickyOffset, previous, right);
            }
        }
    }

    public void setupTrickyQuestionAt(int at, int previous, int current) {
        if(at >= QuestionCount || at < 1) {
            return;
        }
        if(!RepeatedMistake[at-1] || rnd.nextBoolean()) {
            Answers[at - 1] = (previous + 7*12) % 12;
            AnswersOctaved[at - 1] = previous;
            RepeatedMistake[at - 1] = true;
        }
        Answers[at] = (current + 7*12) % 12;
        AnswersOctaved[at] = current;
        RepeatedMistake[at] = true;
    }


    public void handleNextQuestionAsyncPlayAsyncUI(final View v) {
        playingActive++;
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleNextQuestion(v);
                        playingActive--;
                    }
                });
            }
        };
        playHandler.post(myRunnable);
    }


    public void handleNextQuestion(final View v) {
        CurrentQuestion++;
        Answered++;
        Correct += WasCorrect ? 1 : 0;
        Wrong += WasWrong ? 1 : 0;
        if(CurrentQuestion == QuestionCount) {
            newSet();
        }
        UpdateQuestion();
        if(Config.QType == EarQuiz) {
            PlayQuestion(false);
        }
        questionChangeInProgress = false;
    }

    boolean questionChangeInProgress = false;

    public int guessWrong() {
        int wrongOffset = (WrongAttempt - Answers[CurrentQuestion] + 6 + 7*12) % 12 - 6;
        return AnswersOctaved[CurrentQuestion] + wrongOffset;
    }

    public void onClick(View v) {
        if(questionChangeInProgress) {
            return;
        }
        questionChangeInProgress = true;
        clearAllButtonsAsync();
        int answer = (int)v.getTag(R.id.tone);
        if(answer == Answers[CurrentQuestion]) {
            if(Config.QType == EarQuiz) {
                PlayAnswer();
                if(!WasCorrect && Config.TrickyQuestions && CurrentQuestion > 0) {
                    setupTrickyQuestion(AnswersOctaved[CurrentQuestion-1], AnswersOctaved[CurrentQuestion], WasWrong ? guessWrong() : AnswersOctaved[CurrentQuestion]);
                }
            }
            handleNextQuestionAsyncPlayAsyncUI(v);
        } else {
            WasCorrect = false;
            WasWrong = true;
            if(!WasWrong) {
                WrongAttempt = answer;
            }
            setupButtonPropsAsync((Button)v, true, false);
            questionChangeInProgress = false;
        }
    }

    void giveUpButtons(int layoutId) {
        LinearLayout l = (LinearLayout)findViewById(layoutId);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                setupButtonPropsAsync((Button) v, false, (int)((Button)v).getTag(R.id.tone) == Answers[CurrentQuestion]);
            }
        }
    }

    public void onClickGiveUp(View v) {
        WasCorrect = false;
        giveUpButtons(R.id.whites);
        giveUpButtons(R.id.blacks);
        if(Config.QType == EarQuiz && !Config.NoGiveupResolve) {
            PlayResolution();
        }
    }

    public void Finish() {
        Intent i = new Intent(getApplicationContext(), ScoreActivity.class);
        Config.LastScore = (100 * (1+Correct)) / (Correct+Wrong+1);
        Config.LastScore2 = (100 * (1+Correct)) / (Answered+1);
        stopped = true;
        startActivity(i);
    }


    public void PlayQuestion(boolean repeating) {
        if((CurrentQuestion == 0 || Config.TypeOfQuestion.Cadence()) && !repeating) {
            if(repeating || !Config.NoCadences) {
                playCadenceAsyncPlay();
            }
        }
        playToneAsyncPlay(TonicIds[CurrentQuestion], AnswersOctaved[CurrentQuestion], -1, Config.DelayBetweenTones, 0, false);
        if(Config.TypeOfQuestion.Last2() && CurrentQuestion > 0) {
             playToneAsyncPlay(TonicIds[CurrentQuestion-1], AnswersOctaved[CurrentQuestion-1], Answers[CurrentQuestion -1], Config.DelayBetweenTones, 0, false);
        }
        if(Config.TypeOfQuestion.Next() && CurrentQuestion < QuestionCount - 1) {
            playToneAsyncPlay(TonicIds[CurrentQuestion+1], AnswersOctaved[CurrentQuestion+1], -1, Config.DelayBetweenTones, 0, false);
        }
    }

    public void PlayQuestionLast(int offset) {
        if(CurrentQuestion - offset < 0) {
            return;
        }
        playToneAsyncPlay(
                TonicIds[CurrentQuestion-offset],
                AnswersOctaved[CurrentQuestion-offset],
                (AnswersOctaved[CurrentQuestion-offset] + 7 * 12) % 12,
                Config.DelayBetweenTones,
                0,
                false
        );
    }

    public void PlayAnswer() {
        int currentTone = AnswersOctaved[CurrentQuestion];
        if(Config.Resolving) {
            PlayResolution();
        }
        if(Config.TypeOfQuestion.Tonic()) {
            playTonicAsyncPlay();
        }
        /*
        if(Config.TypeOfQuestion.Last2()) {
            PlayQuestionLast(1);
        }*/
        if(Config.TypeOfQuestion.Last()) {
            PlayQuestionLast(0);
        }
    }

    public int getNearestTonic(int tonic, int answer, boolean inverted) {
        int tonicLower = answer - ((answer - tonic +7*12)%12);
        int tonicHigher = answer - ((answer - tonic +7*12)%12) + 12;

        if (answer - tonicLower < tonicHigher - answer) {
            return inverted ? tonicHigher : tonicLower;
        } else if (answer - tonicLower > tonicHigher - answer) {
            return inverted ?  tonicLower : tonicHigher;
        } else {
            switch(CurrentScaleType.AugFourthDirection()) {
                case -1:
                    return inverted ? tonicHigher : tonicLower;
                case 1:
                    return inverted ?  tonicLower : tonicHigher;
                default:
                    return Config.AugFourthUp ^ inverted ? tonicHigher : tonicLower;
            }
        }
    }

    //todo: refactor prameter choice into the ResolutionType enum
    public void PlayResolution() {
        Boolean resolving = true;
        int answer = AnswersOctaved[CurrentQuestion];
        int tonic = TonicIds[CurrentQuestion];
        int target;
        int from;
        int inc;
        int currentTone;
        int tonicLower = answer - ((answer - tonic +7*12)%12);
        int tonicHigher = answer - ((answer - tonic +7*12)%12) + 12;
        int fifthNearest = tonicLower + 7;
        if(Config.TypeOfResolution == ResolutionType.last_current && CurrentQuestion > 0) {
            from = AnswersOctaved[CurrentQuestion-1];
            target = answer;
            playToneAsyncPlay(tonic, answer, (answer + 7*12) % 12, 0, 0, true);
        } else if(Config.TypeOfResolution == ResolutionType.current_lats && CurrentQuestion > 0) {
            from = answer;
            target = AnswersOctaved[CurrentQuestion-1];
        } else if (Config.TypeOfResolution == ResolutionType.inverted) {
            from = answer;
            target = getNearestTonic(tonic, answer, true);
        } else if (Config.TypeOfResolution == ResolutionType.fifth) {
            from = answer;
            target = fifthNearest;
        } else if (Config.TypeOfResolution == ResolutionType.fifth_lower) {
            from = answer;
            target = answer > fifthNearest ? fifthNearest : tonicLower;
        } else if (Config.TypeOfResolution == ResolutionType.fifth_upper) {
            from = answer;
            target = answer < fifthNearest ? fifthNearest : tonicHigher;
        } else if (Config.TypeOfResolution == ResolutionType.inverted2) {
            from = getNearestTonic(tonic, answer, false);
            target = answer;
            playToneAsyncPlay(tonic, answer, (answer + 7*12) % 12, 0, 0, true);
        } else {
            from = answer;
            target = getNearestTonic(tonic, answer, false);
        }
        currentTone = from;
        inc = target < from ? -1 : +1;
        while ( true ) {
            if(
                    Config.TypeOfResolution.ShouldResolveTone(CurrentScaleType, tonic, from, target, currentTone)
                    || (currentTone + 7*12) % 12 == (answer + 7*12) % 12
                    ) {
                if(Config.InterleavedResolutions) {
                    playTonicAsyncPlay();
                }
                playToneAsyncPlay(tonic, currentTone, (currentTone + 7*12) % 12, 0, 0, true);
            }
            if(currentTone == target) {
                break;
            }
            currentTone = currentTone + inc;
        }
    }

    public void playTonicAsyncPlay() {
        playingActive++;
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                int length = 60000/Config.TypeOfTempo.Tempo();
                p.playChordByTones(TonicIds[CurrentQuestion], CurrentScaleType.ResolveChord(1, 3), length);
                SystemClock.sleep(Config.DelayBetweenTones);
                playingActive--;
            }
        };
        playHandler.post(myRunnable);
    }

    public void playCadenceAsyncPlay() {
        playingActive++;
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                int length = 60000/Config.TypeOfTempo.Tempo();
                p.playChordByTones(TonicIds[CurrentQuestion], CurrentScaleType.ResolveChord(1, 3), length);
                p.playChordByTones(TonicIds[CurrentQuestion], CurrentScaleType.ResolveChord(4, 3), length);
                p.playChordByTones(TonicIds[CurrentQuestion], CurrentScaleType.ResolveChord(5, 3), length);
                p.playChordByTones(TonicIds[CurrentQuestion], CurrentScaleType.ResolveChord(1, 3), length);
                SystemClock.sleep(Config.DelayBetweenTones);
                playingActive--;
            }
        };
        playHandler.post(myRunnable);
    }

    public void onClickCadence(View v) {
        playCadenceAsyncPlay();
    }

    public void onClickRepeat(View v) {
        PlayQuestion(true);
    }

    public void onClickRepeatLast(View v) {
        PlayQuestionLast(1);
    }

    int playStimuliOffset = 0;
    int playStimuliInversion = 0;
    int playStimuliLastQuestion = 0;

    public void playStimuliSync(int tonic, int tone, int length, int delay, Boolean resolving) {
        switch(Config.TypeOfStimuli) {
            case tones:
                p.playNote(tone, length);
                break;
            case accompanied_tones:
            case accompanied_tones_inversion:
                if(AnswersOctaved[CurrentQuestion] != playStimuliLastQuestion) {
                    playStimuliLastQuestion = AnswersOctaved[CurrentQuestion];
                    playStimuliOffset = -RandUtils.NextInt("playStimuli", 3) * 2;
                    playStimuliInversion = RandUtils.NextInt("chordInversion", 3);
                }
                //this should be probably located with cadences
                if(!resolving) {
                    //SystemClock.sleep(length);
                    p.playChordByTones(
                            tonic,
                            CurrentScaleType.ResolveChordInKey(
                                    tonic,
                                    CurrentScaleType.GetNthToneInKey(tonic, tone, playStimuliOffset),
                                    3,
                                    Config.TypeOfStimuli == StimuliType.accompanied_tones ? 0 : playStimuliInversion
                            ),
                            length
                    );
                }
                SystemClock.sleep(delay);
                p.playNote(tone, length);
                break;
            case chord:
                p.playChordByTones(tonic, CurrentScaleType.ResolveChordInKey(tonic, tone, 3, 0), length);
                break;
            case chord_inversion:
                p.playChordByTones(tonic, CurrentScaleType.ResolveChordInKey(tonic, tone, 3, RandUtils.NextInt("chordInversion", 3)), length);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void playToneAsyncPlay(final int tonic, final int tone, final int highlight, final int dly1, final int dly2, final Boolean resolving) {
        if(Config.QType == EarQuiz) {
            playingActive++;
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    HighlightTone = highlight;
                    clearAllButtonsAsync();
                    SystemClock.sleep(dly1);
                    playStimuliSync(tonic, tone, 60000/Config.TypeOfTempo.Tempo(), dly1, resolving);
                    SystemClock.sleep(dly2);
                    HighlightTone = -1;
                    clearAllButtonsAsync();
                    playingActive--;
                }
            };
            playHandler.post(myRunnable);
        }
    }


    public void markChordToneStart(int tone) {
        if(Config.ArpeggioChords && Config.ArpeggioMarking) {
            HighlightTone = (tone + 7 * 12) % 12;
            clearAllButtonsAsync();
        }
    }

    public void markChordToneReset() {
        if(Config.ArpeggioChords && Config.ArpeggioMarking) {
            HighlightTone = -1;
            clearAllButtonsAsync();
        }
    }


    public void previewToneAsync(final int tone, final boolean start) {
        if(Config.QType == EarQuiz) {
            playingActive++;
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    if(start) {
                        p.StartNote(tone);
                    } else {
                        p.StopNote(tone);
                    }
                    playingActive--;
                }
            };
            playHandler.post(myRunnable);
        }
    }

    public void newInstrument() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                p.randomInstrument();
                SystemClock.sleep(100);
            }
        };
        playHandler.post(myRunnable);
    }

    public void startAnswerer() {
        if(stopped) {
            return;
        }
        if(Config.TypeOfQuestion.Continuu()) {
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    if(playingActive > 0) {
                        SystemClock.sleep(10);
                        startAnswerer();
                    } else {
                        playingActive++;
                        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(Config.DelayBetweenTones);
                                if ( Config.TypeOfQuestion.Delay() > 0 ) {
                                    SystemClock.sleep((long)(Config.NoteLength * Config.TypeOfQuestion.Delay()));
                                }
                                Button b = findToneButton(AnswersOctaved[CurrentQuestion]);
                                onClick(b);
                                playingActive--;
                                startAnswerer();
                            }
                        };
                        mainHandler.post(myRunnable);
                    }
                }
            };
            listeningHandler.post(myRunnable);
        }
    }

    public void startEarQuiz() {
        if(Config.QType == EarQuiz) {
            newInstrument();
            PlayQuestion(false);
            stopped = false;
            startAnswerer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        p.onResume();
        if(newSet) {
            newSet = false;
            startEarQuiz();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        p.onPause();
        stopped = true;
    }


    @Override
    public void onBackPressed() {
        Intent i;
        if(Config.QType == EarQuiz) {
            i = new Intent(getApplicationContext(), MenuEarActivity.class);
        } else {
            i = new Intent(getApplicationContext(), MenuKeyActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        stopped = true;
    }

    long firstPreviewUpdate = 0;
    long lastPreviewUpdate = 0;
    int lastNotePreview = -1;
    int currentNotePreview = -1;
    boolean previewing = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(super.onTouchEvent(e)) {
            return true;
        }
        if(Config.QType != EarQuiz) {
            return false;
        }
        gestureDetector.HandleEvent(e);
        if(gestureDetector.UpdateInput(TonicIds[CurrentQuestion], CurrentScaleType)) {
            lastPreviewUpdate = System.currentTimeMillis();
            currentNotePreview = gestureDetector.GetInput();
            if(gestureDetector.Valid()) {
                clearAllButtonsAsync();
            }
            Log.d("gdetector", "updated detection to " + gestureDetector.GetInput());
        }
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            previewing = false;
            lastPreviewUpdate = System.currentTimeMillis();
            firstPreviewUpdate = System.currentTimeMillis();
        }
        long timeSinceLastUpdate = System.currentTimeMillis() - lastPreviewUpdate;
        long timeSincefirstUpdate = System.currentTimeMillis() - firstPreviewUpdate;
        boolean timeFulfilled = (timeSinceLastUpdate > 100 && previewing) || (timeSincefirstUpdate > 700 && !previewing);
        if(gestureDetector.Valid() && timeFulfilled && (gestureDetector.GetInput() != lastNotePreview || !previewing)) {
            previewToneAsync(lastNotePreview, false);
            lastNotePreview = gestureDetector.GetInput();
            previewToneAsync(lastNotePreview, true);
            previewing = true;
            lastPreviewUpdate = System.currentTimeMillis();
        }
        if(e.getAction() == MotionEvent.ACTION_UP && gestureDetector.Finished() & gestureDetector.Valid()){
            previewToneAsync(lastNotePreview, false);
            Button b = findToneButton(gestureDetector.GetInput());
            onClick(b);
        }
        if(e.getAction() == MotionEvent.ACTION_UP){
            lastNotePreview = -1;
            currentNotePreview = -1;
        }
        return true;
    }

}
