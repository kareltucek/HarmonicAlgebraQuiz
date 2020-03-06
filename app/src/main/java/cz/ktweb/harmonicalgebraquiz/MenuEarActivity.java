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


public class MenuEarActivity extends AppCompatActivity {
    boolean cfgChanged = false;

    void updateValues() {
        setupDropdownBox(R.id.resolutionType);
        setupDropdownBox(R.id.scaleType);
        setupDropdownBox(R.id.restrictionType);
        setupDropdownBox(R.id.questionType);
        setupDropdownBox(R.id.keyType);
        setupDropdownBox(R.id.layoutType);
        setupDropdownBox(R.id.setCountType);
        setupDropdownBox(R.id.tempoType);
        setupDropdownBox(R.id.stimuliType);

        setupCheckboxes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_menu);

        if(!Cfg.c.fromSave) {
            Cfg.Load();
        }
        updateValues();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Cfg.Save();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        //Cfg.Save();
        super.onDestroy();
    }

    public void setupCheckboxes()
    {
        CheckBox checkbox = (CheckBox)findViewById(R.id.chromatics);
        checkbox.setChecked(Cfg.c.ChromaticMode);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.ChromaticMode = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.resolving);
        checkbox.setChecked(Cfg.c.Resolving);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.Resolving = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.interleavedResolutions);
        checkbox.setChecked(Cfg.c.InterleavedResolutions);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.InterleavedResolutions = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.trickyQuestions);
        checkbox.setChecked(Cfg.c.TrickyQuestions);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.TrickyQuestions = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.limitedIntervals);
        checkbox.setChecked(Cfg.c.LimitedIntervals);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.LimitedIntervals = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.intervalMode);
        checkbox.setChecked(Cfg.c.EarIntervalLabels);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.EarIntervalLabels = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.noCadences);
        checkbox.setChecked(Cfg.c.NoCadences);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.NoCadences = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.augFouthUp);
        checkbox.setChecked(Cfg.c.AugFourthUp);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.AugFourthUp = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.noGiveUpResolve);
        checkbox.setChecked(Cfg.c.NoGiveupResolve);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.NoGiveupResolve = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.hideLabels);
        checkbox.setChecked(Cfg.c.HideLabels);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.HideLabels = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.arpeggioChords);
        checkbox.setChecked(Cfg.c.ArpeggioChords);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.ArpeggioChords = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.arpeggioMarking);
        checkbox.setChecked(Cfg.c.ArpeggioMarking);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Cfg.c.ArpeggioMarking = isChecked;
            }
        });
    }

    public void onClickEarQuiz(View v) {
        Cfg.c.QType = QuizType.EarQuiz;
        Cfg.c.InvertedMode = false;
        Cfg.c.Sets = Cfg.c.TypeOfSetCount.QuestionsInQuiz() / Cfg.c.TypeOfSetCount.QuestionsInSet();
        Cfg.c.EarSetSize = Cfg.c.TypeOfSetCount.QuestionsInSet();
        startQuiz();
    }

    public void startQuiz() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void onClickHelp(View v) {
        Intent i = new Intent(getApplicationContext(), HelpEarActivity.class);
        startActivity(i);
    }

    public void onClickBack(View v) {
        onBackPressed();
    }

    public void onClickExpand(View view) {
        View v = findViewById(R.id.advanced);
        if(v.isShown()) {
           v.setVisibility(View.GONE);
        } else {
            v.setVisibility(v.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void updateChromaticState() {
        boolean checked = Cfg.c.TypeOfRestriction.RequiresChromatic() &&  Cfg.c.TypeOfStimuli == StimuliType.tones;
        boolean active = !Cfg.c.TypeOfRestriction.BansChromatic() && !Cfg.c.TypeOfRestriction.RequiresChromatic() && Cfg.c.TypeOfStimuli == StimuliType.tones;
        ((CheckBox)findViewById(R.id.chromatics)).setChecked(checked);
        ((CheckBox)findViewById(R.id.chromatics)).setEnabled(active);
    }

    public void updateSpinner(int id) {;
        int value = 0;
        if(id == R.id.resolutionType) {
            value = Cfg.c.TypeOfResolution.Position();
        }
        if( id == R.id.scaleType) {
            value = Cfg.c.TypeOfScale.Position();
        }
        if(id == R.id.restrictionType) {
            value = Cfg.c.TypeOfRestriction.Position();
        }
        if(id == R.id.questionType) {
            value = Cfg.c.TypeOfQuestion.Position();
        }
        if(id == R.id.keyType) {
            value = Cfg.c.TypeOfKey.Position();
        }
        if(id == R.id.layoutType) {
            value = Cfg.c.TypeOfLayout.Position();
        }
        if(id == R.id.setCountType) {
            value = Cfg.c.TypeOfSetCount.Position();
        }
        if(id == R.id.tempoType) {
            value = Cfg.c.TypeOfTempo.Position();
        }
        if(id == R.id.stimuliType) {
            value = Cfg.c.TypeOfStimuli.Position();
        }
        final Spinner dynamicSpinner = (Spinner) findViewById(id);
        dynamicSpinner.setSelection(value);
    }

    public void setupDropdownBox(int id) {
        final Spinner dynamicSpinner = (Spinner) findViewById(id);

        String[] labels = {"Oops."};
        if(id == R.id.resolutionType) {
            labels = Cfg.c.TypeOfResolution.GetLabelArray();
        }
        if( id == R.id.scaleType) {
            labels = Cfg.c.TypeOfScale.GetLabelArray();
        }
        if(id == R.id.restrictionType) {
            labels = Cfg.c.TypeOfRestriction.GetLabelArray();
        }
        if(id == R.id.questionType) {
            labels = Cfg.c.TypeOfQuestion.GetLabelArray();
        }
        if(id == R.id.keyType) {
            labels = Cfg.c.TypeOfKey.GetLabelArray();
        }
        if(id == R.id.layoutType) {
            labels = Cfg.c.TypeOfLayout.GetLabelArray();
        }
        if(id == R.id.setCountType) {
            labels = Cfg.c.TypeOfSetCount.GetLabelArray();
        }
        if(id == R.id.tempoType) {
            labels = Cfg.c.TypeOfTempo.GetLabelArray();
        }
        if(id == R.id.stimuliType) {
            labels = Cfg.c.TypeOfStimuli.GetLabelArray();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        dynamicSpinner.setAdapter(adapter);
        updateSpinner(id);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(parent.getId() == R.id.scaleType) {
                    Cfg.c.TypeOfScale = ScaleType.ByValue(position);
                }
                if(parent.getId() == R.id.resolutionType) {
                    Cfg.c.TypeOfResolution = ResolutionType.ByValue(position);
                }
                if(parent.getId() == R.id.restrictionType) {
                    Cfg.c.TypeOfRestriction = RestrictionType.ByValue(position);
                    updateChromaticState();
                }
                if(parent.getId() == R.id.questionType) {
                    Cfg.c.TypeOfQuestion = QuestionPlayType.ByValue(position);
                }
                if(parent.getId() == R.id.keyType) {
                    Cfg.c.TypeOfKey = KeyType.ByValue(position);
                }
                if(parent.getId() == R.id.layoutType) {
                    Cfg.c.TypeOfLayout = LayoutType.ByValue(position);
                }
                if(parent.getId() == R.id.setCountType) {
                    Cfg.c.TypeOfSetCount = SetCountType.ByValue(position);
                }
                if(parent.getId() == R.id.tempoType) {
                    Cfg.c.TypeOfTempo = TempoType.ByValue(position);
                }
                if(parent.getId() == R.id.stimuliType) {
                    Cfg.c.TypeOfStimuli = StimuliType.ByValue(position);
                    updateChromaticState();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
