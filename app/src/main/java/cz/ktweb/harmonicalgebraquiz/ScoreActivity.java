package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.graphics.Color;
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
        tw.setText(" " + Config.LastScore + "%");
        if(Config.LastScore > 90) {
            tw.setTextColor(Config.Green);
        } else if (Config.LastScore > 60) {
            tw.setTextColor(Config.Orange);
        } else {
            tw.setTextColor(Config.Red);
        }
    }

    public void onClickHarmonicQuiz(View v) {
        Intent i = new Intent(getApplicationContext(),MenuActivity.class);
        startActivity(i);
    }
}
