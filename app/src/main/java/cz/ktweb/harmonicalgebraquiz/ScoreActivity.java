package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView tw = (TextView)findViewById(R.id.score);
        tw.setText(" " + Cfg.c.LastScore + "%");
        if(Cfg.c.LastScore > 90) {
            tw.setTextColor(Cfg.c.Green);
        } else if (Cfg.c.LastScore > 60) {
            tw.setTextColor(Cfg.c.Orange);
        } else {
            tw.setTextColor(Cfg.c.Red);
        }
    }

    public void onClickHarmonicQuiz(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i;
        if(Cfg.c.QType == QuizType.EarQuiz) {
            i = new Intent(getApplicationContext(), MenuEarActivity.class);
        } else {
            i = new Intent(getApplicationContext(), MenuKeyActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
