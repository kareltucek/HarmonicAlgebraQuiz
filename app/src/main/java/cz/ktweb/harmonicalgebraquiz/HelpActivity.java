package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        ((TextView)findViewById(R.id.helpText)).setText(Html.fromHtml( getString(R.string.help)));
    }

    public void  onClick(View v) {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(i);
    }
}
