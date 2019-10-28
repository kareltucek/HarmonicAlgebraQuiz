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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_menu);
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

    public void setupCheckboxes()
    {
        CheckBox checkbox = (CheckBox)findViewById(R.id.chromatics);
        checkbox.setChecked(Config.ChromaticMode);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.ChromaticMode = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.resolving);
        checkbox.setChecked(Config.Resolving);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.Resolving = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.interleavedResolutions);
        checkbox.setChecked(Config.InterleavedResolutions);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.InterleavedResolutions = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.trickyQuestions);
        checkbox.setChecked(Config.TrickyQuestions);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.TrickyQuestions = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.limitedIntervals);
        checkbox.setChecked(Config.LimitedIntervals);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.LimitedIntervals = isChecked;
            }
        });

        checkbox = (CheckBox)findViewById(R.id.intervalMode);
        checkbox.setChecked(Config.EarIntervalLabels);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.EarIntervalLabels = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.noCadences);
        checkbox.setChecked(Config.NoCadences);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.NoCadences = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.augFouthUp);
        checkbox.setChecked(Config.AugFourthUp);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.AugFourthUp = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.noGiveUpResolve);
        checkbox.setChecked(Config.NoGiveupResolve);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.NoGiveupResolve = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.hideLabels);
        checkbox.setChecked(Config.HideLabels);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.HideLabels = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.arpeggioChords);
        checkbox.setChecked(Config.ArpeggioChords);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.ArpeggioChords = isChecked;
            }
        });
        checkbox = (CheckBox)findViewById(R.id.arpeggioMarking);
        checkbox.setChecked(Config.ArpeggioMarking);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Config.ArpeggioMarking = isChecked;
            }
        });
    }

    public void onClickEarQuiz(View v) {
        Config.QType = QuizType.EarQuiz;
        Config.InvertedMode = false;
        Config.Sets = Config.TypeOfSetCount.QuestionsInQuiz() / Config.TypeOfSetCount.QuestionsInSet();
        Config.EarSetSize = Config.TypeOfSetCount.QuestionsInSet();
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

    public void updateResolving() {
        if(Config.TypeOfStimuli != StimuliType.tones && Config.TypeOfStimuli != StimuliType.accompanied_tones &&  Config.TypeOfStimuli != StimuliType.accompanied_tones_inversion) {
            ((CheckBox) findViewById(R.id.resolving)).setChecked(false);
            Config.Resolving = false;
        }

    }

    public void updateChromaticState() {
        boolean checked = Config.TypeOfRestriction.RequiresChromatic() &&  Config.TypeOfStimuli == StimuliType.tones;
        boolean active = !Config.TypeOfRestriction.BansChromatic() && !Config.TypeOfRestriction.RequiresChromatic() && Config.TypeOfStimuli == StimuliType.tones;
        ((CheckBox)findViewById(R.id.chromatics)).setChecked(checked);
        ((CheckBox)findViewById(R.id.chromatics)).setEnabled(active);
    }

    public void updateSpinner(int id) {;
        int value = 0;
        if(id == R.id.resolutionType) {
            value = Config.TypeOfResolution.Position();
        }
        if( id == R.id.scaleType) {
            value = Config.TypeOfScale.Position();
        }
        if(id == R.id.restrictionType) {
            value = Config.TypeOfRestriction.Position();
        }
        if(id == R.id.questionType) {
            value = Config.TypeOfQuestion.Position();
        }
        if(id == R.id.keyType) {
            value = Config.TypeOfKey.Position();
        }
        if(id == R.id.layoutType) {
            value = Config.TypeOfLayout.Position();
        }
        if(id == R.id.setCountType) {
            value = Config.TypeOfSetCount.Position();
        }
        if(id == R.id.tempoType) {
            value = Config.TypeOfTempo.Position();
        }
        if(id == R.id.stimuliType) {
            value = Config.TypeOfStimuli.Position();
        }
        final Spinner dynamicSpinner = (Spinner) findViewById(id);
        dynamicSpinner.setSelection(value);
    }

    public void setupDropdownBox(int id) {
        final Spinner dynamicSpinner = (Spinner) findViewById(id);

        String[] labels = {"Oops."};
        if(id == R.id.resolutionType) {
            labels = Config.TypeOfResolution.GetLabelArray();
        }
        if( id == R.id.scaleType) {
            labels = Config.TypeOfScale.GetLabelArray();
        }
        if(id == R.id.restrictionType) {
            labels = Config.TypeOfRestriction.GetLabelArray();
        }
        if(id == R.id.questionType) {
            labels = Config.TypeOfQuestion.GetLabelArray();
        }
        if(id == R.id.keyType) {
            labels = Config.TypeOfKey.GetLabelArray();
        }
        if(id == R.id.layoutType) {
            labels = Config.TypeOfLayout.GetLabelArray();
        }
        if(id == R.id.setCountType) {
            labels = Config.TypeOfSetCount.GetLabelArray();
        }
        if(id == R.id.tempoType) {
            labels = Config.TypeOfTempo.GetLabelArray();
        }
        if(id == R.id.stimuliType) {
            labels = Config.TypeOfStimuli.GetLabelArray();
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
                    Config.TypeOfScale = ScaleType.ByValue(position);
                }
                if(parent.getId() == R.id.resolutionType) {
                    Config.TypeOfResolution = ResolutionType.ByValue(position);
                }
                if(parent.getId() == R.id.restrictionType) {
                    Config.TypeOfRestriction = RestrictionType.ByValue(position);
                    updateChromaticState();
                }
                if(parent.getId() == R.id.questionType) {
                    Config.TypeOfQuestion = QuestionPlayType.ByValue(position);
                }
                if(parent.getId() == R.id.keyType) {
                    Config.TypeOfKey = KeyType.ByValue(position);
                }
                if(parent.getId() == R.id.layoutType) {
                    Config.TypeOfLayout = LayoutType.ByValue(position);
                }
                if(parent.getId() == R.id.setCountType) {
                    Config.TypeOfSetCount = SetCountType.ByValue(position);
                }
                if(parent.getId() == R.id.tempoType) {
                    Config.TypeOfTempo = TempoType.ByValue(position);
                }
                if(parent.getId() == R.id.stimuliType) {
                    Config.TypeOfStimuli = StimuliType.ByValue(position);
                    updateChromaticState();
                    updateResolving();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
