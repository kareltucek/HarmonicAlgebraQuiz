package cz.ktweb.harmonicalgebraquiz;

import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import org.billthefarmer.mididriver.MidiDriver;

import java.util.Random;

public class Player implements MidiDriver.OnMidiStartListener{

    private Random rnd = new Random();
    private MidiDriver midiDriver;
    private byte[] event;
    private int[] config;
    public boolean active = false;

    public static int offset = 0;

    public static int currentInstrument = 0;

    public static Object[] instruments = {
            //new Pair<>(0, "Grand Piano"),
            new Pair<>(1, "Bright Piano"),
            //new Pair<>(6, "Harpsichord"),
            new Pair<>(11, "Vibraphone"),
            new Pair<>(12, "Marimba"),
            new Pair<>(13, "Xylophone"),
            new Pair<>(20, "Organ"),
            new Pair<>(24, "Guitar"),
            //new Pair<>(40, "Violin"),
            //new Pair<>(45, "Pizzicato Strings"),
            //new Pair<>(56, "Trumpets"),
            //new Pair<>(58, "Tuba"),
            new Pair<>(66, "Sax"),
            new Pair<>(71, "Clarinet"),
            new Pair<>(75, "Pan Flute"),
            //new Pair<>(76, "Blown Bottle"),
            new Pair<>(79, "Ocarina"),
            //new Pair<>(111, "Shanai")
    };

    Player() {
        init();
    }

    public void init() {
        // Instantiate the driver.
        midiDriver = new MidiDriver();
        // Set the listener.
        midiDriver.setOnMidiStartListener(this);
        Log.d("midi", "init");
    }

    public void onResume() {
        midiDriver.start();

        // Get the configuration.
        config = midiDriver.config();
        sendInstrumentMsg();
        Log.d("midi", "resumed");
    }

    public void onPause() {
        midiDriver.stop();
        active = false;
        Log.d("midi", "paused");
    }

    @Override
    public void onMidiStart() {
        active = true;
        Log.d("midi", "started");
    }

    public void playNote(int note, int length) {
        startNote(note, 70);
        SystemClock.sleep(length);
        stopNote(note);
    }

    public void playChord(int note, boolean major, int duration) {
        startNote(note, 50);
        startNote(note + (major ? 4 : 3),50);
        startNote(note + 7, 50);
        SystemClock.sleep(duration);
        stopNote(note);
        stopNote(note + (major ? 4 : 3));
        stopNote(note + 7);
    }

    public void playChordByTones(int tonic, int[] tones, int duration) {
        for(int i = 0; i < tones.length; i++) {
            startNote(tonic + tones[i], 50);
            SystemClock.sleep(rnd.nextInt(30));
        }
        SystemClock.sleep(duration);
        for(int i = 0; i < tones.length; i++) {
            stopNote(tonic + tones[i]);
        }
    }

    private void startNote(int note, int velocity) {

        // Construct a note ON message for the middle C at maximum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (0x90 | 0x00);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) (int)(0x3C + note + offset);  // 0x3C = middle C
        event[2] = (byte) velocity;  // 0x7F = the maximum velocity (127)

        // Internally this just calls write() and can be considered obsoleted:
        //midiDriver.queueEvent(event);

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }

    private void stopNote(int note) {

        // Construct a note OFF message for the middle C at minimum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (0x80 | 0x00);  // 0x80 = note Off, 0x00 = channel 1
        event[1] = (byte) (int)(0x3C + note + offset);  // 0x3C = middle C
        event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }

    private void sendInstrumentMsg() {
        event = new byte[2];
        event[0] = (byte) (0xC0 | 0x00);
        event[1] = (byte) (int)((Pair<Integer, String>)instruments[currentInstrument]).first;

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);
    }

    public String getInstrument() {
        return ((Pair<Integer, String>)instruments[currentInstrument]).second;
    }

    public void randomInstrument(){
        int instr = rnd.nextInt(instruments.length);
        currentInstrument = instr;
        sendInstrumentMsg();
    }

}
