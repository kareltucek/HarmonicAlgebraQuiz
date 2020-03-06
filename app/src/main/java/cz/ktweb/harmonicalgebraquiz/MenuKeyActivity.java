package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;


public class MenuKeyActivity extends AppCompatActivity {
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_menu);
        highlightDifficulty();
    }


    public void onClickHarmonicQuiz(View v) {
        Cfg.c.QType = QuizType.AlgebraQuiz;
        Cfg.c.InvertedMode = false;
        Cfg.c.Sets = Cfg.c.SetsInFunctionQuiz;
        startQuiz();
    }

    public void onClickSigQuiz(View v) {
        Cfg.c.QType = QuizType.KeyQuiz;
        Cfg.c.InvertedMode = false;
        Cfg.c.Sets = Cfg.c.SetsInSigQuiz;
        startQuiz();
    }

    public void onClickHarmonicQuizInv(View v) {
        Cfg.c.QType = QuizType.AlgebraQuiz;
        Cfg.c.InvertedMode = true;
        Cfg.c.Sets = Cfg.c.SetsInFunctionQuiz;
        startQuiz();
    }

    public void onClickSigQuizInv(View v) {
        Cfg.c.QType = QuizType.KeyQuiz;
        Cfg.c.InvertedMode = true;
        Cfg.c.Sets = Cfg.c.SetsInSigQuiz;
        startQuiz();
    }

    public void onClickHelp(View v) {
        Intent i = new Intent(getApplicationContext(), HelpKeyActivity.class);
        startActivity(i);
    }

    public void  onClickBack(View v) {
        onBackPressed() ;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void startQuiz() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void onClickDifficulty(View v) {
        Cfg.c.Difficulty = (int)Integer.parseInt((String)v.getTag());
        Cfg.c.Difficulty = Cfg.c.Difficulty > 7 || Cfg.c.Difficulty < 0 ? 7 : Cfg.c.Difficulty;
        highlightDifficulty();
    }


    public void onClickPasses(View v) {
        Cfg.c.Passes = (int)Integer.parseInt((String)v.getTag());
        highlightDifficulty();
    }


    public void onClickInverted(View v) {
        Cfg.c.InvertedMode = !Cfg.c.InvertedMode;
        highlightDifficulty();
    }

    public void resetButtonColor(Button b, boolean d) {
        int r = false ? 50 : 0;
        int g = false ? 50 : 0;
        int dark = d ? 50 : 0;
        b.getBackground().setColorFilter(MathUtils.rgb(Cfg.c.bgRed -g - dark,Cfg.c.bgGreen - r - dark,Cfg.c.bgBlue - r - g - dark), PorterDuff.Mode.MULTIPLY);
    }

    public void highlightDifficulty() {
        LinearLayout l;
        l = (LinearLayout)findViewById(R.id.difficulties);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                resetButtonColor((Button) v, Integer.parseInt((String)((Button)v).getTag()) == Cfg.c.Difficulty);
            }
        }
        l = (LinearLayout)findViewById(R.id.menu);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                resetButtonColor((Button) v, false);
            }
        }
        l = (LinearLayout)findViewById(R.id.multiplicities);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                resetButtonColor((Button) v, Integer.parseInt((String)((Button)v).getTag()) == Cfg.c.Passes);
            }
        }
        Button b = (Button)findViewById(R.id.inverted);
        resetButtonColor(b, Cfg.c.InvertedMode);
    }

}
