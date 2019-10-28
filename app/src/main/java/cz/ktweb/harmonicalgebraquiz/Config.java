package cz.ktweb.harmonicalgebraquiz;

import android.graphics.Color;

//refactor this?
enum QuizType {
    AlgebraQuiz,
    KeyQuiz,
    EarQuiz
}

public class Config {
    //Stateful config
    public static QuizType QType = QuizType.AlgebraQuiz;
    public static int Sets = 0; //how many sets should be generated (set size is determined by question type and parameters)
    public static int Passes = 1; //how many times each set should be repeated (applies to harmonic function quiz)

    public static int EarSetSize = 100;

    public static int LastScore = 0;
    public static int LastScore2 = 0;

    //Constants
    //orange 255,180,50
    public static int basicGray = 220;
    public static int bgRed = basicGray;
    public static int bgGreen = basicGray;
    public static int bgBlue = basicGray;

    public static int Green = Color.rgb(60,200,60);
    public static int Orange = Color.rgb(220,200,0);
    public static int Red = Color.rgb(200,60,60);

    public static int NoteLength = 600;
    public static int DelayBetweenTones = 0;

    //Persistent config - i.e., what we might want to save to
    //========================

    //Sig/Function quiz
    public static int Difficulty = 4;

    public static int SetsInFunctionQuiz = 8;
    public static int SetsInSigQuiz = 1;
    public static boolean ShuffleInvertedSigMode = true;

    public static boolean InvertedMode = false;

    //Ear trainer
    public static boolean TrickyQuestions = true;
    public static int TrickyOffset = 8;
    public static int SecondTrickyOffset = 50;
    public static boolean EarIntervalLabels = false;
    public static boolean ChromaticMode = false;
    public static boolean Resolving = true;
    public static boolean LimitedIntervals = true;
    public static boolean InterleavedResolutions = false;
    public static boolean NoCadences = false;
    public static boolean AugFourthUp = false;
    public static boolean NoGiveupResolve = true;
    public static boolean HideLabels = false; //should be refactored to full-featured label type (letters, numbers, static do-re-mi, movable do-re-mi, hidden, all-letters)
    public static boolean ArpeggioChords = false;
    public static boolean ArpeggioMarking = false;
    public static ScaleType TypeOfScale = ScaleType.major;
    public static ResolutionType TypeOfResolution = ResolutionType.basic;
    public static RestrictionType TypeOfRestriction = RestrictionType.basic1;
    public static QuestionPlayType TypeOfQuestion = QuestionPlayType.play_last;
    public static KeyType TypeOfKey = KeyType.c;
    public static LayoutType TypeOfLayout = LayoutType.piano_cmaj;
    public static SetCountType TypeOfSetCount = SetCountType.c_100;
    public static TempoType TypeOfTempo = TempoType.moderato;
    public static StimuliType TypeOfStimuli = StimuliType.tones;


    //Progression trainer
    public static ScaleType ProgressionScale = ScaleType.major;
    public static ScaleLabelType ProgressionLabels = ScaleLabelType.chord_degree;
    public static KeyType ProgressionKey = KeyType.c;
    public static int EnableNextAfter = 10;
    public static boolean NormalizedChords = true;
    public static boolean FreeMode = false;
}
