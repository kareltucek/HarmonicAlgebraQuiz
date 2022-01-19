package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import static cz.ktweb.harmonicalgebraquiz.ScaleType.minor;

public class ProgressionActivity extends AppCompatActivity implements View.OnClickListener {
    Random rnd = new Random();
    public static Player p = MainActivity.p;
    public static HandlerThread playThread;
    public static Handler playHandler;
    public static boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progression);
        gameStarted = false;

        setupButtons();

        if(playThread == null) {
            playThread = new HandlerThread("ProgressionThread");
            playThread.start();
            playHandler = new Handler(playThread.getLooper());
        }

        //startGame();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuProgressionActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    int[] buttonIds = {
            R.id.chord_1,
            R.id.chord_2,
            R.id.chord_3,
            R.id.chord_4,
            R.id.chord_5,
            R.id.chord_6,

            R.id.chord_7,
            R.id.chord_7a,
            R.id.chord_7b,

            R.id.chord_5b,
            R.id.chord_3b,
            R.id.chord_2b,
    };

    Chord[][] algebras = {
            {
                //minor
                    new Chord(Degree.iii, ChordType.major, "III", "I"),
                    new Chord(Degree.IV, ChordType.minor, "iv", "iv"),
                    new Chord(Degree.V, ChordType.minor, "v", "v"),
                    new Chord(Degree.vi, ChordType.major, "VI", "IV"),
                    new Chord(Degree.vii, ChordType.major, "VII", "VI"),
                    new Chord(Degree.I, ChordType.minor, "i", "i"),

                    new Chord(Degree.II, ChordType.dim7, "ii*", "*7"),
                    new Chord(Degree.ii, ChordType.major, "II", "*1-"),
                    new Chord(Degree.II, ChordType.minor, "ii", "*5+"),

                    new Chord(Degree.vii, ChordType.major7, "VII7", "V7"),
                    new Chord(Degree.V, ChordType.major7, "V7", "v3+7"),
                    new Chord(Degree.IV, ChordType.major, "IV", "iv3+"),
            },
            {
                //major
                    new Chord(Degree.I, ChordType.major, "I", "I"),
                    new Chord(Degree.II, ChordType.minor, "ii", "iv"),
                    new Chord(Degree.III, ChordType.minor, "iii", "v"),
                    new Chord(Degree.IV, ChordType.major, "IV", "IV"),
                    new Chord(Degree.V, ChordType.major, "V", "V"),
                    new Chord(Degree.VI, ChordType.minor, "vi", "i"),

                    new Chord(Degree.VII, ChordType.dim7, "vii*", "*7"),
                    new Chord(Degree.vii, ChordType.major, "VII", "*1-"),
                    new Chord(Degree.VII, ChordType.minor, "vii", "*5+"),

                    new Chord(Degree.V, ChordType.major7, "V7", "V7"),
                    new Chord(Degree.III, ChordType.major7, "III7", "v3+7"),
                    new Chord(Degree.II, ChordType.major, "II", "iv3+"),
            }
    };

    int[][] EnablingOrder = {
            {5, 1, 2, 9, 0, 3, 9, 10, 11, 6, 7, 8}, //minor
            {0, 3, 4, 5, 1, 2, 9, 10, 11, 6, 7, 8}, //major
    };

    int Type = 0;
    int Tonic = 0;
    int Enabled = 2;
    int NextEnabledAfter = Cfg.c.EnableNextAfter;

    int PrevQuestion = 0;
    int Question = 0;
    int Inversion = 0;
    int InversionDir = 0;

    int GreenHighlight = -1;
    int RedHighlight = -1;

    int errors = 0;
    boolean errorAlreadyCounted = false;


    String determineLabel(int idx) {
        int type = Cfg.c.ProgressionScale == minor ? 0 : 1;
        if ( Cfg.c.ProgressionLabels == ProgressionLabelType.chord_name) {
            return Cfg.c.ProgressionScale.GetCustomChordToneLabel(Tonic, algebras[type][idx], false);
        } else if ( Cfg.c.ProgressionLabels == ProgressionLabelType.chord_functions) {
            return algebras[type][idx].label2;
        } else {
            return algebras[type][idx].label;
        }
    }

    void setupButton(int id, int idx) {
        int type = Cfg.c.ProgressionScale == minor ? 0 : 1;
        if ( algebras[type].length > idx) {
            Button b = findViewById(id);
            b.setVisibility(View.VISIBLE);
            b.setBackgroundResource(R.drawable.round_button);
            b.setTag(idx);
            b.setOnClickListener(this);
            b.setText(determineLabel(idx));
            b.setTextAppearance(R.style.AppTheme);
            b.setAllCaps(false);
            b.setSingleLine(true);
        } else {
            Button b = findViewById(id);
            b.setVisibility(View.INVISIBLE);
        }
    }

    void setupStopButton() {
        Button b = findViewById(R.id.btnStop);
        b.setVisibility(Cfg.c.FreeMode ? View.VISIBLE : View.INVISIBLE);
        b.setBackgroundResource(R.drawable.round_button);
        b.setTextAppearance(R.style.AppTheme);
        b.setAllCaps(false);
    }

    void setupButtons() {
        setupStopButton();
        for(int i = 0; i < buttonIds.length; i++) {
            setupButton(buttonIds[i], i);
        }
    }

    void setupButtonProps(Button b, int idx, int enablingOrder) {
        int r = RedHighlight == idx ? 50 : 0;
        int g = GreenHighlight == idx ? 50 : 0;
        int dark = enablingOrder < Enabled ? 0 : -30;
        //b.setClickable(enablingOrder < Enabled);
        b.getBackground().setColorFilter(MathUtils.rgb(Cfg.c.bgRed -g - dark,Cfg.c.bgGreen - r - dark,Cfg.c.bgBlue - r - g - dark), PorterDuff.Mode.MULTIPLY);
        boolean visible = enablingOrder < Enabled && b.getText() != "";
        //b.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        b.setText(determineLabel(idx));
    }

    void clearAllButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int type = Cfg.c.ProgressionScale == minor ? 0 : 1;
                for(int i = 0; i < buttonIds.length; i++) {
                    Button b = (Button)findViewById(buttonIds[EnablingOrder[type][i]]);
                    setupButtonProps(b, EnablingOrder[type][i], i);
                }
            }
        });
    }

    void startGame() {
        if(gameStarted)
        {
            return;
        }
        gameStarted = true;
        Enabled = 3;
        NextEnabledAfter = Cfg.c.EnableNextAfter;
        Inversion = 0;
        InversionDir = 0;
        Pair<Integer, ScaleType> randomKey = Cfg.c.ProgressionKey.SelectScale(Cfg.c.ProgressionScale);
        Tonic = randomKey.first;
        Type = randomKey.second == ScaleType.minor ? 0 : 1;
        setupButtons();
        newInstrument();
        if(!Cfg.c.FreeMode) {
            clearAllButtons();
            playQuestion(true);
        } else {
            Enabled = 7;
            clearAllButtons();
        }
    }

    void playQuestion(boolean first) {
        int type = Cfg.c.ProgressionScale == minor ? 0 : 1;
        int prev2Q = PrevQuestion;
        PrevQuestion = Question;
        int previousQ = Question;
        int previousInv = Inversion;
        int previousDir = InversionDir;
        errorAlreadyCounted = false;
        if(first) {
            previousQ = 2;
            Question = 0;
            GreenHighlight = 0;
            clearAllButtons();
        }
        while ( false
                    || (prev2Q == previousQ && previousQ == Question)
                    || (previousQ == Question && previousInv == Inversion && (previousDir == InversionDir || Inversion == 0))
                    || (algebras[Cfg.c.ProgressionScale == minor ? 0 : 1][Question].tpe == ChordType.dummy)
                ) {
            Question = EnablingOrder[type][rnd.nextInt(Enabled)];
            if(Cfg.c.ProgressionInversions) {
                Inversion = rnd.nextInt(4);
                InversionDir = rnd.nextInt(2) * 2 - 1;
            } else {
                Inversion = 0;
                InversionDir = 0;
            }
        }
        playChord(algebras[Type][Question]);
    }

    public void onStop(View v)
    {
        p.endAll();

    }

    public void onClick(View v)
    {
        GreenHighlight = -1;
        RedHighlight = -1;
        int idx = (int)v.getTag();

        if(Cfg.c.FreeMode) {
            playFreeChord(algebras[Type][idx]);
        } else {
            if (idx == Question) {
                NextEnabledAfter--;
                GreenHighlight = idx;
                if (errors >= 3) {
                    errors = 0;
                    Enabled = Enabled > 3 ? Enabled - 1 : Enabled;
                    NextEnabledAfter = Cfg.c.EnableNextAfter;
                }
                else if (NextEnabledAfter == 0) {
                    errors = 0;
                    Enabled = Enabled < algebras[Type].length ? Enabled + 1 : Enabled;
                    while ( Enabled < algebras[Type].length && algebras[Cfg.c.ProgressionScale == minor ? 0 : 1][Enabled-1].tpe == ChordType.dummy ) {
                        Enabled = Enabled + 1;
                    }
                    NextEnabledAfter = Cfg.c.EnableNextAfter;
                }
                clearAllButtons();
                playQuestion(false);
            } else {
                if(!errorAlreadyCounted) {
                    errorAlreadyCounted = true;
                    errors++;
                }
                /*
                if (NextEnabledAfter < Cfg.c.EnableNextAfter / 2) {
                    Enabled = Enabled > 3 ? Enabled - 1 : Enabled;
                    NextEnabledAfter = Cfg.c.EnableNextAfter;
                }*/
                RedHighlight = idx;
                clearAllButtons();
            }
        }
    }

    public int[] resolveChord(final Chord ch) {
        int[] chord;
        if(Cfg.c.ProgressionGuitarChords) {
            chord = ch.ResolveAsGuitarChord(Tonic);
        } else if( Cfg.c.ProgressionInversions) {
            chord = ch.Resolve(Tonic, false, Inversion, InversionDir);
        } else {
            chord = ch.Resolve(Tonic, Cfg.c.NormalizedChords, 0, 0) ;
        }
        return chord;
    }

    public void playChord(final Chord ch) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(Cfg.c.DelayBetweenTones);
                p.playChordByTones(0, resolveChord(ch), Cfg.c.NoteLength, Cfg.c.ProgressionArpeggio);
                GreenHighlight = -1;
                RedHighlight = -1;
                clearAllButtons();
            }
        };
        playHandler.post(myRunnable);
    }

    public void playFreeChord(final Chord ch) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(Cfg.c.DelayBetweenTones);
                p.endAll();
                p.startChordByTones(0, resolveChord(ch));
                GreenHighlight = -1;
                RedHighlight = -1;
                clearAllButtons();
            }
        };
        playHandler.post(myRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        p.onResume();
        startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        p.onPause();
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
}
