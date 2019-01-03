package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        highlightDifficulty();
    }


    public void onClickHarmonicQuiz(View v) {
        Config.QType = 0;
        Config.Sets = 5;
        startQuiz();
    }

    public void onClickSigQuiz(View v) {
        Config.QType = 1;
        Config.Sets = 1;
        startQuiz();
    }

    public void onClickHelp(View v) {
        Intent i = new Intent(getApplicationContext(), HelpActivity.class);
        startActivity(i);
    }

    public void startQuiz() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void onClickDifficulty(View v) {
        Config.Difficulty = (int)Integer.parseInt((String)v.getTag());
        Config.Difficulty = Config.Difficulty > 6 || Config.Difficulty < 0 ? 6 : Config.Difficulty;
        highlightDifficulty();
    }

    public void resetButtonColor(Button b, boolean d) {
        int r = false ? 50 : 0;
        int g = false ? 50 : 0;
        int dark = d ? 50 : 0;
        b.getBackground().setColorFilter(Color.rgb(Config.BasicDarkness -g - dark,Config.BasicDarkness - r - dark,Config.BasicDarkness - r - g - dark), PorterDuff.Mode.MULTIPLY);
    }

    public void highlightDifficulty() {
        LinearLayout l;
        l = (LinearLayout)findViewById(R.id.difficulties);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                resetButtonColor((Button) v, Integer.parseInt((String)((Button)v).getTag()) == Config.Difficulty);
            }
        }
        l = (LinearLayout)findViewById(R.id.menu);
        for (int i = 0; i < l.getChildCount(); i++) {
            View v = l.getChildAt(i);
            if (v instanceof Button) {
                resetButtonColor((Button) v, false);
            }
        }
    }
}
