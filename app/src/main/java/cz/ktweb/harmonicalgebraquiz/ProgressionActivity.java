package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

            R.id.chord_8,
            R.id.chord_9,
            R.id.chord_10,
            R.id.chord_11,

            R.id.chord_12,
            R.id.chord_13,
    };

    Chord[][] algebras = {
            {
                    new Chord(Degree.I, ChordType.minor, "i"),
                    new Chord(Degree.II, ChordType.dim, "ii*"),
                    new Chord(Degree.iii, ChordType.major, "III"),
                    new Chord(Degree.IV, ChordType.minor, "iv"),
                    new Chord(Degree.V, ChordType.minor, "v"),
                    new Chord(Degree.vi, ChordType.major, "VI"),
                    new Chord(Degree.vii, ChordType.major, "VII"),

                    new Chord(Degree.II, ChordType.major, "II"),
                    new Chord(Degree.IV, ChordType.major, "IV"),
                    new Chord(Degree.III, ChordType.dummy, ""),
                    new Chord(Degree.V, ChordType.major7, "V7"),

                    new Chord(Degree.III, ChordType.dummy, ""),
                    new Chord(Degree.V, ChordType.major, "V"),
            },
            {
                    new Chord(Degree.I, ChordType.major, "I"),
                    new Chord(Degree.II, ChordType.minor, "ii"),
                    new Chord(Degree.III, ChordType.minor, "iii"),
                    new Chord(Degree.IV, ChordType.major, "IV"),
                    new Chord(Degree.V, ChordType.major, "V"),
                    new Chord(Degree.VI, ChordType.minor, "vi"),
                    new Chord(Degree.VII, ChordType.dim, "vii*"),

                    new Chord(Degree.II, ChordType.major, "II"),
                    new Chord(Degree.III, ChordType.major, "III"),
                    new Chord(Degree.III, ChordType.major7, "III7"),
                    new Chord(Degree.VII, ChordType.major, "VII"),

                    new Chord(Degree.III, ChordType.dummy, ""),
                    new Chord(Degree.V, ChordType.major7, "V7"),
            }
    };

    int[] EnablingOrder = {0, 4, 3, 5, 2, 1, 6, 7, 8, 9, 10, 11, 12};

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
            return Cfg.c.ProgressionScale.GetCustomChordToneLabel(Tonic, algebras[type][idx]);
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

    void setupButtons() {
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
        b.setText(determineLabel(idx));
    }

    void clearAllButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            for(int i = 0; i < buttonIds.length; i++) {
                Button b = (Button)findViewById(buttonIds[EnablingOrder[i]]);
                setupButtonProps(b, EnablingOrder[i], i);
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
        if(Cfg.c.ProgressionKey == KeyType.c) {
            Tonic = 0;
        } else {
            int diff = Cfg.c.ProgressionKey.Difficulty();
            int sharps = - diff + rnd.nextInt(2*diff + 1);
            Tonic = Cfg.c.ProgressionScale.GetTonicFromSharps(sharps);
        }
        Type = Cfg.c.ProgressionScale == ScaleType.minor ? 0 : 1;
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
            Question = EnablingOrder[rnd.nextInt(Enabled)];
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
                    Enabled = Enabled < algebras[Type].length - 1 ? Enabled + 1 : Enabled;
                    while ( Enabled < algebras[Type].length - 1 && algebras[Cfg.c.ProgressionScale == minor ? 0 : 1][Enabled].tpe == ChordType.dummy ) {
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
        if( Cfg.c.ProgressionInversions) {
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
