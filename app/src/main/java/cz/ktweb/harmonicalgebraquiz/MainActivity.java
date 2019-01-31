package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.Random;


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


    void addSpace(int weight, int layoutId) {
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

    void addButtonsPiano2(int tonicAt, int shiftBy, ScaleType tpe) {
        clearLayout(R.id.strings);
        clearLayout(R.id.blacks);
        clearLayout(R.id.whites);

        int wh, bl;
        if(tpe.Contains(shiftBy%12)){
            wh = 1;
            bl = 2;
        } else
        {
            wh = 2;
            bl = 1;
        }

        addSpace(wh, R.id.whites);
        addSpace(bl, R.id.blacks);
        for(int i = 0; i < 13; i++ ) {
            int j = (i + shiftBy) % 12;
            if(tpe.Contains(j)) {
                addButton((tonicAt+j)%12, R.id.whites);
                if(i > 0 && tpe.Contains(j-1)) {
                    addSpace(2, R.id.blacks);
                }
            } else {
                addButton((tonicAt+j)%12, R.id.blacks);
                if(i > 0 && !tpe.Contains(j-1)) {
                    addSpace( 2, R.id.whites);
                }

            }
        }
        addSpace(wh, R.id.whites);
        addSpace(bl, R.id.blacks);
    }

    void addButtonsPiano() {
        addButtonsPiano2(0, 0, ScaleType.major);
    }

    void tuneButtonsSync(final int tonicAt) {
        switch(Config.TypeOfLayout) {
            case relative_native:
                addButtonsPiano2(tonicAt, 0, Config.TypeOfScale);
                break;
            case relative_cmaj:
                addButtonsPiano2(tonicAt, 0, ScaleType.major);
                break;
            case piano_shifted:
                addButtonsPiano2(0, tonicAt, ScaleType.major);
                break;
            case piano_cmaj:
                addButtonsPiano2(0, 0, ScaleType.major);
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

        CurrentSet = 0;
        addButtons();
        newSet();
        UpdateQuestion();

        setButtonVisibility(R.id.cadence, Config.QType == QuizType.EarQuiz);
        setButtonVisibility(R.id.repeat, Config.QType == QuizType.EarQuiz);
        setButtonVisibility(R.id.repeatLast, Config.QType == QuizType.EarQuiz);


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
    int CurrentQuestion = 0;
    int CurrentSet = 0;

    int Answered = 0;
    int Correct = 0;
    int Wrong = 0;
    boolean WasCorrect = true;
    boolean WasWrong = true;

    int HighlightTone = -1;

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
        } else if (Config.QType == QuizType.EarQuiz){
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
        setQuestionCount(setSize);
        int difficulty = Config.TypeOfKey.Difficulty();
        int sharpcount;
        if(Config.TypeOfKey == KeyType.c) {
            sharpcount = Config.TypeOfScale.GetSharps(0);
        } else {
            int lastSharpCount = Config.TypeOfScale.GetSharps(TonicIds[0]);
            do {
                sharpcount = -difficulty + rnd.nextInt(1 + 2 * difficulty);
            } while (sharpcount == lastSharpCount && difficulty > 0);
        }
        SimpleLabelType labelType = sharpcount >= 0 ? SimpleLabelType.major_sharps : SimpleLabelType.major_flats;
        int actualTonic = Config.TypeOfScale.GetTonicFromSharps(sharpcount);
        for(int i = 0; i < setSize; i++)
        {
            int deg;
            int tone;
            do {
                int range = Config.TypeOfRestriction.To() - Config.TypeOfRestriction.From();
                deg = Config.TypeOfRestriction.From() + rnd.nextInt(range) + (Config.TypeOfRestriction.Absolute() ? -actualTonic : 0);
                tone = deg + actualTonic;
            } while (
                            (i > 1 && (AnswersOctaved[i-1] == tone || AnswersOctaved[i-2] == tone))
                        ||
                            (!Config.TypeOfScale.Contains(deg) && !Config.ChromaticMode)
                        ||
                            (i > 0 && Config.LimitedIntervals && Math.abs(AnswersOctaved[i-1] - tone) > 12)
                    );
            TonicIds[i] = actualTonic;
            TonicTypes[i] = 1;
            TonicString[i] = Config.TypeOfScale.GetScaleName(actualTonic);
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

    void UpdateQuestion() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tonic = (TextView)findViewById(R.id.Tonic);
                TextView question = (TextView)findViewById(R.id.Question);
                tonic.setText(TonicString[CurrentQuestion]);
                question.setText(QuestionStrings[CurrentQuestion]);
                clearAllButtonsSync();
                ((TextView)findViewById(R.id.correct)).setText("" + Correct);
                ((TextView)findViewById(R.id.wrooong)).setText("" + Wrong);
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
            if(Config.QType == QuizType.AlgebraQuiz || Config.QType == QuizType.EarQuiz) {
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
            if (Config.QType == QuizType.EarQuiz && Config.EarIntervalLabels && CurrentQuestion > 0) {
                int difference = AnswersOctaved[CurrentQuestion] - AnswersOctaved[CurrentQuestion-1];
                if(difference >= 0) {
                    difference = (tone - AnswersOctaved[CurrentQuestion-1] + 7*12) % 12;
                    return (difference == 0 ? "" : "+") + LabelStore.GetDegreeLabel(SimpleLabelType.interval, difference);
                } else {
                    difference = (AnswersOctaved[CurrentQuestion-1] - tone + 7*12) % 12;
                    return (difference == 0 ? "" : "-") + LabelStore.GetDegreeLabel(SimpleLabelType.interval, difference);
                }
            } else if (Config.QType == QuizType.EarQuiz) {
                return Config.TypeOfScale.GetToneLabel(ScaleLabelType.tone_name, TonicIds[CurrentQuestion], tone);
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
        int dark = black;
        b.getBackground().setColorFilter(MathUtils.rgb(Config.bgRed -g - dark,Config.bgGreen - r - dark,Config.bgBlue - r - g - dark), PorterDuff.Mode.MULTIPLY);
        if(CurrentQuestion < QuestionCount) {
            b.setText(determineLabel(tone));
        }
    }

    public void setupTrickyQuestion() {
        Answers[CurrentQuestion+Config.TrickyOffset] = Answers[CurrentQuestion];
        Answers[CurrentQuestion+Config.TrickyOffset-1] = Answers[CurrentQuestion-1];
        AnswersOctaved[CurrentQuestion+Config.TrickyOffset] = AnswersOctaved[CurrentQuestion];
        AnswersOctaved[CurrentQuestion+Config.TrickyOffset-1] = AnswersOctaved[CurrentQuestion-1];
    }

    //because we love asynchronous systems
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
        if(Config.QType == QuizType.EarQuiz) {
            PlayQuestion(false);
        }
    }

    public void onClick(View v) {
        clearAllButtonsAsync();
        int answer = (int)v.getTag(R.id.tone);
        if(answer == Answers[CurrentQuestion]) {
            if(Config.QType == QuizType.EarQuiz) {
                PlayAnswer();
                if(!WasCorrect && Config.TrickyQuestions && CurrentQuestion > 0 && CurrentQuestion < QuestionCount - Config.TrickyOffset) {
                    setupTrickyQuestion();
                }
                handleNextQuestionAsyncPlayAsyncUI(v);
            }
        } else {
            WasCorrect = false;
            WasWrong = true;
            setupButtonPropsAsync((Button)v, true, false);
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
        if(Config.QType == QuizType.EarQuiz) {
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
            playCadenceAsyncPlay();
        }
        if(CurrentQuestion != 0 && Config.TypeOfQuestion.Tonic() && !repeating) {
            playTonicAsyncPlay();
        }
        if(Config.TypeOfQuestion.Last() && !repeating) {
            PlayQuestionLast();
        }
        playToneAsyncPlay(AnswersOctaved[CurrentQuestion], -1, Config.DelayBetweenTones, 0);
    }

    public void PlayQuestionLast() {
        if(CurrentQuestion - 1 < 0) {
            return;
        }
        playToneAsyncPlay(
                AnswersOctaved[CurrentQuestion-1],
                (AnswersOctaved[CurrentQuestion-1] + 7 * 12) % 12,
                Config.DelayBetweenTones,
                0
        );
    }

    public void PlayAnswer() {
        int currentTone = AnswersOctaved[CurrentQuestion];
        if(Config.Resolving) {
            PlayResolution();
        }
    }

    public void PlayResolution() {
        int answer = AnswersOctaved[CurrentQuestion];
        int tonic = TonicIds[CurrentQuestion];
        int tonicLower = answer - ((answer - tonic +7*12)%12);
        int tonicHigher = answer - ((answer - tonic +7*12)%12) + 12;
        int target;
        int from;
        int inc;
        int currentTone;
        if(Config.TypeOfResolution == ResolutionType.last_current && CurrentQuestion > 0) {
            currentTone = AnswersOctaved[CurrentQuestion-1];
            from = AnswersOctaved[CurrentQuestion-1];
            target = from < answer ? tonicHigher : tonicLower;
            playToneAsyncPlay(answer, (answer + 7*12) % 12, 0, 0);
        } else {
            currentTone = answer;
            from = answer;
            target = (answer - tonicLower < tonicHigher - answer) ? tonicLower : tonicHigher;
        }
        inc = target < from ? -1 : +1;
        while ( true ) {
            if(
                    Config.TypeOfResolution.ShouldResolve(Config.TypeOfScale,from - tonic,target - tonic,currentTone - tonic)
                    || (currentTone + 7*12) % 12 == (answer + 7*12) % 12
                    ) {
                if(Config.InterleavedResolutions) {
                    playTonicAsyncPlay();
                }
                playToneAsyncPlay(currentTone, (currentTone + 7*12) % 12, 0, 0);
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
                p.playChordByTones(TonicIds[CurrentQuestion], Config.TypeOfScale.ResolveChord(1, 3), length);
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
                p.playChordByTones(TonicIds[CurrentQuestion], Config.TypeOfScale.ResolveChord(1, 3), length);
                p.playChordByTones(TonicIds[CurrentQuestion], Config.TypeOfScale.ResolveChord(4, 3), length);
                p.playChordByTones(TonicIds[CurrentQuestion], Config.TypeOfScale.ResolveChord(5, 3), length);
                p.playChordByTones(TonicIds[CurrentQuestion], Config.TypeOfScale.ResolveChord(1, 3), length);
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
        PlayQuestionLast();
    }

    public void playToneAsyncPlay(final int tone, final int highlight, final int dly1, final int dly2) {
        if(Config.QType == QuizType.EarQuiz) {
            playingActive++;
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    HighlightTone = highlight;
                    clearAllButtonsAsync();
                    SystemClock.sleep(dly1);
                    p.playNote(tone, 60000/Config.TypeOfTempo.Tempo());
                    SystemClock.sleep(dly2);
                    HighlightTone = -1;
                    clearAllButtonsAsync();
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
                                for (int i = Config.TypeOfQuestion.Delay(); i > 0; i--) {
                                    SystemClock.sleep(Config.NoteLength);
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
        if(Config.QType == QuizType.EarQuiz) {
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
        if(Config.QType == QuizType.EarQuiz) {
            i = new Intent(getApplicationContext(), MenuEarActivity.class);
        } else {
            i = new Intent(getApplicationContext(), MenuKeyActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        stopped = true;
    }
}
