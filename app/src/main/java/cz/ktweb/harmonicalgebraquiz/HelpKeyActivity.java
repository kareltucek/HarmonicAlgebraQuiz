package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class HelpKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        ((TextView)findViewById(R.id.helpText)).setText(Html.fromHtml( getString(R.string.helpKey)));
    }

    public void  onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuKeyActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
