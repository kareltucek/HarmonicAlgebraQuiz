package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }



    public void onClickEarQuiz(View v) {
        Intent i = new Intent(getApplicationContext(), MenuEarActivity.class);
        startActivity(i);
    }



    public void onClickKeyQuiz(View v) {
        Intent i = new Intent(getApplicationContext(), MenuKeyActivity.class);
        startActivity(i);
    }

    public void onClickProgression(View v) {
        Intent i = new Intent(getApplicationContext(), MenuProgressionActivity.class);
        startActivity(i);
    }


    public void onClickHelp(View v) {
        Intent i = new Intent(getApplicationContext(), HelpIntroActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
