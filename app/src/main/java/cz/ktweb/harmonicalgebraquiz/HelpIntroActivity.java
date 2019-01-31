package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class HelpIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        ((TextView)findViewById(R.id.helpText)).setText(Html.fromHtml( getString(R.string.helpIntro)));
    }

    public void  onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
