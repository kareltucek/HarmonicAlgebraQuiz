package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.Color;
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
            R.id.chord_7
    };

    Chord[][] algebras = {
            {
                    new Chord(Degree.I, ChordType.minor),
                    new Chord(Degree.II, ChordType.dim),
                    new Chord(Degree.iii, ChordType.major),
                    new Chord(Degree.IV, ChordType.minor),
                    new Chord(Degree.V, ChordType.major),
                    new Chord(Degree.vi, ChordType.major),
                    new Chord(Degree.vii, ChordType.major),
            },
            {
                    new Chord(Degree.I, ChordType.major),
                    new Chord(Degree.II, ChordType.minor),
                    new Chord(Degree.III, ChordType.minor),
                    new Chord(Degree.IV, ChordType.major),
                    new Chord(Degree.V, ChordType.major),
                    new Chord(Degree.VI, ChordType.minor),
                    new Chord(Degree.VII, ChordType.dim),
            }
    };

    int[] EnablingOrder = {0, 4, 3, 5, 2, 1, 6};

    int Type = 0;
    int Tonic = 0;
    int Enabled = 2;
    int NextEnabledAfter = Config.EnableNextAfter;
    int Question = 0;

    int GreenHighlight = -1;
    int RedHighlight = -1;


    String determineLabel(int idx) {
        int type = Config.ProgressionScale == minor ? 0 : 1;
        return Config.ProgressionScale.GetDegreeLabel(Config.ProgressionLabels, Tonic, algebras[type][idx].degree.Value());
    }

    void setupButton(int id, int idx) {
        Button b = findViewById(id);
        b.setBackgroundResource(R.drawable.round_button);
        b.setTag(idx);
        b.setOnClickListener(this);
        b.setText(determineLabel(idx));
        b.setTextAppearance(R.style.AppTheme);
        b.setAllCaps(false);
        b.setSingleLine(true);
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
        b.getBackground().setColorFilter(MathUtils.rgb(Config.bgRed -g - dark,Config.bgGreen - r - dark,Config.bgBlue - r - g - dark), PorterDuff.Mode.MULTIPLY);
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
        NextEnabledAfter = Config.EnableNextAfter;
        if(Config.ProgressionKey == KeyType.c) {
            Tonic = 0;
        } else {
            int diff = Config.ProgressionKey.Difficulty();
            int sharps = - diff + rnd.nextInt(2*diff + 1);
            Tonic = Config.ProgressionScale.GetTonicFromSharps(sharps);
        }
        Type = Config.ProgressionScale == ScaleType.minor ? 0 : 1;
        newInstrument();
        if(!Config.FreeMode) {
            clearAllButtons();
            playQuestion(true);
        } else {
            Enabled = 7;
            clearAllButtons();
        }
    }

    void playQuestion(boolean first) {
        int previous = Question;
        if(first) {
            previous = 2;
            Question = 0;
            GreenHighlight = 0;
            clearAllButtons();
        }
        while (previous == Question) {
            Question = EnablingOrder[rnd.nextInt(Enabled)];
        }
        playChord(algebras[Type][Question]);
    }

    public void onClick(View v)
    {
        GreenHighlight = -1;
        RedHighlight = -1;
        int idx = (int)v.getTag();
        if(Config.FreeMode) {
            playFreeChord(algebras[Type][idx]);
        } else {
            if (idx == Question) {
                NextEnabledAfter--;
                GreenHighlight = idx;
                if (NextEnabledAfter == 0) {
                    Enabled = Enabled < 7 ? Enabled + 1 : Enabled;
                    NextEnabledAfter = Config.EnableNextAfter;
                }
                clearAllButtons();
                playQuestion(false);
            } else {
                if (NextEnabledAfter < Config.EnableNextAfter / 2) {
                    Enabled = Enabled > 3 ? Enabled - 1 : Enabled;
                    NextEnabledAfter = Config.EnableNextAfter;
                }
                RedHighlight = idx;
                clearAllButtons();
            }
        }
    }

    public void playChord(final Chord ch) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(Config.DelayBetweenTones);
                p.playChordByTones(0, ch.Resolve(Tonic, Config.NormalizedChords), Config.NoteLength*2);
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
                SystemClock.sleep(Config.DelayBetweenTones);
                p.endAll();
                p.startChordByTones(0, ch.Resolve(Tonic, Config.NormalizedChords));
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
