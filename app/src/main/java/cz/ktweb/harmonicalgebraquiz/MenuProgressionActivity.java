package cz.ktweb.harmonicalgebraquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

public class MenuProgressionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_progression);

        setupDropdownBox(R.id.labelType);
        setupDropdownBox(R.id.keyType);
        setupCheckboxes();
    }


    public void setupCheckboxes()
    {
        CheckBox checkbox = (CheckBox)findViewById(R.id.normalize);
        checkbox.setChecked(Cfg.c.NormalizedChords);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.NormalizedChords = isChecked;
            }
        });


        checkbox = (CheckBox)findViewById(R.id.inversions);
        checkbox.setChecked(Cfg.c.ProgressionInversions);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.ProgressionInversions = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.progressionGuitarMode);
        checkbox.setChecked(Cfg.c.ProgressionGuitarChords);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.ProgressionGuitarChords = isChecked;
            }
        });


        checkbox = (CheckBox)findViewById(R.id.progressionArpeggio);
        checkbox.setChecked(Cfg.c.ProgressionArpeggio);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.ProgressionArpeggio = isChecked;
            }
        });


        checkbox = (CheckBox)findViewById(R.id.freeMode);
        checkbox.setChecked(Cfg.c.FreeMode);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.FreeMode = isChecked;
            }
        });
    }

    public void onClickBack(View v) {
        onBackPressed();
    }

    public void start() {
        Intent i = new Intent(getApplicationContext(), ProgressionActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void onClickMajor(View v) {
        Cfg.c.ProgressionScale = ScaleType.major;
        start();
    }

    public void onClickMinor(View v) {
        Cfg.c.ProgressionScale = ScaleType.minor;
        start();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    public void setupDropdownBox(int id) {
        final Spinner dynamicSpinner = (Spinner) findViewById(id);

        String[] lab = {"Oops."};
        int pos = 0;
        if(id == R.id.labelType) {
            lab = Cfg.c.ProgressionLabels.GetLabelArray();
            pos = Cfg.c.ProgressionLabels.Position();
        }
        if(id == R.id.keyType) {
            lab = Cfg.c.ProgressionKey.GetLabelArray();
            pos = Cfg.c.ProgressionKey.Position();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lab);

        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setSelection(pos);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(parent.getId() == R.id.labelType) {
                    Cfg.c.ProgressionLabels = ProgressionLabelType.ByValue(position);
                }
                if(parent.getId() == R.id.keyType) {
                    Cfg.c.ProgressionKey = KeyType.ByValue(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
