package cz.ktweb.harmonicalgebraquiz;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.SystemClock;

public class GuitarPlayer {
    int[][] resources = {
            {
                R.raw.cmi,
                    R.raw.cismi,
                    R.raw.dmi,
                    R.raw.dismi,
                    R.raw.emi,
                    R.raw.fmi,
                    R.raw.fismi,
                    R.raw.gmi,
                    R.raw.gismi,
                    R.raw.ami,
                    R.raw.bbmi,
                    R.raw.bmi
            },
            {
                    R.raw.cmaj,
                    R.raw.dbmaj,
                    R.raw.dmaj,
                    R.raw.ebmaj,
                    R.raw.emaj,
                    R.raw.fmaj,
                    R.raw.fismaj,
                    R.raw.gmaj,
                    R.raw.abmaj,
                    R.raw.amaj,
                    R.raw.bbmaj,
                    R.raw.bmaj
            }
    };

    int[][] resources_short = {
            {
                    R.raw.cmi_short,
                    R.raw.cismi_short,
                    R.raw.dmi_short,
                    R.raw.dismi_short,
                    R.raw.emi_short,
                    R.raw.fmi_short,
                    R.raw.fismi_short,
                    R.raw.gmi_short,
                    R.raw.gismi_short,
                    R.raw.ami_short,
                    R.raw.bbmi_short,
                    R.raw.bmi_short
            },
            {
                    R.raw.cmaj_short,
                    R.raw.dbmaj_short,
                    R.raw.dmaj_short,
                    R.raw.ebmaj_short,
                    R.raw.emaj_short,
                    R.raw.fmaj_short,
                    R.raw.fismaj_short,
                    R.raw.gmaj_short,
                    R.raw.abmaj_short,
                    R.raw.amaj_short,
                    R.raw.bbmaj_short,
                    R.raw.bmaj_short
            }
    };

    MediaPlayer[][] players = new MediaPlayer[2][12];

    public GuitarPlayer(Context ctx) {
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 12; j++) {
                players[i][j] = MediaPlayer.create(ctx, resources_short[i][j]);
            }
        }
    }

    public void playChord(int tonic, int[] chord) {
        boolean isMajor = chord[2] - chord[0] == 6 || chord[1] - chord[0] == 4;
        int tone = (tonic + chord[0] + 12*12) % 12;
        players[isMajor ? 1 : 0][tone].start();
        SystemClock.sleep(750);
    }
}
