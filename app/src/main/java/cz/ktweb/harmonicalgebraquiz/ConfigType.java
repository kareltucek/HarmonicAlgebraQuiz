package cz.ktweb.harmonicalgebraquiz;

import android.graphics.Color;

import java.io.Serializable;

//refactor this?
enum QuizType {
    AlgebraQuiz,
    KeyQuiz,
    EarQuiz
}



public class ConfigType implements Serializable {
    public String name = "Default";
    public boolean fromSave = false;

    //Stateful config
    public QuizType QType = QuizType.AlgebraQuiz;
    public int Sets = 0; //how many sets should be generated (set size is determined by question type and parameters)
    public int Passes = 1; //how many times each set should be repeated (applies to harmonic function quiz)

    public int EarSetSize = 100;

    public int LastScore = 0;
    public int LastScore2 = 0;

    //Constants
    //orange 255,180,50
    public int basicGray = 220;
    public int bgRed = basicGray;
    public int bgGreen = basicGray;
    public int bgBlue = basicGray;

    public int Green = Color.rgb(60,200,60);
    public int Orange = Color.rgb(220,200,0);
    public int Red = Color.rgb(200,60,60);

    public int NoteLength = 600;
    public int DelayBetweenTones = 0;

    //Persistent config - i.e., what we might want to save to
    //========================

    //Sig/Function quiz
    public int Difficulty = 4;

    public int SetsInFunctionQuiz = 8;
    public int SetsInSigQuiz = 1;
    public boolean ShuffleInvertedSigMode = true;

    public boolean InvertedMode = false;

    //Ear trainer
    public boolean TrickyQuestions = true;
    public int TrickyOffset = 8;
    public int SecondTrickyOffset = 50;
    public boolean EarIntervalLabels = false;
    public boolean ChromaticMode = false;
    public boolean Resolving = true;
    public boolean LimitedIntervals = true;
    public boolean InterleavedResolutions = false;
    public boolean NoCadences = false;
    public boolean AugFourthUp = false;
    public boolean NoGiveupResolve = true;
    public boolean HideLabels = false; //should be refactored to full-featured label type (letters, numbers, static do-re-mi, movable do-re-mi, hidden, all-letters)
    public boolean ArpeggioChords = false;
    public boolean ArpeggioMarking = false;
    public ScaleType TypeOfScale = ScaleType.major;
    public ResolutionType TypeOfResolution = ResolutionType.basic;
    public RestrictionType TypeOfRestriction = RestrictionType.basic1;
    public QuestionPlayType TypeOfQuestion = QuestionPlayType.play_last;
    public KeyType TypeOfKey = KeyType.c;
    public LayoutType TypeOfLayout = LayoutType.piano_cmaj;
    public SetCountType TypeOfSetCount = SetCountType.c_100;
    public TempoType TypeOfTempo = TempoType.moderato;
    public StimuliType TypeOfStimuli = StimuliType.tones;


    //Progression trainer
    public ScaleType ProgressionScale = ScaleType.major;
    public ProgressionLabelType ProgressionLabels = ProgressionLabelType.chord_degree;
    public KeyType ProgressionKey = KeyType.c;
    public int EnableNextAfter = 10;
    public boolean NormalizedChords = true;
    public boolean ProgressionInversions = true;
    public boolean ProgressionArpeggio = false;
    public boolean FreeMode = false;
}


