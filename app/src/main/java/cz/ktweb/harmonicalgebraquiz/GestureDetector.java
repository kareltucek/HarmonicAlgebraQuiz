package cz.ktweb.harmonicalgebraquiz;


import android.view.MotionEvent;

import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.ACTION_DOWN;
import static cz.ktweb.harmonicalgebraquiz.GestureDetector.GestureState.Finished;
import static cz.ktweb.harmonicalgebraquiz.GestureDetector.GestureState.Started;

/*
TODO:
- implement adaptive mode
- make the gesture correspond to the layout instead of scale type
 */
public class GestureDetector {
    public enum GestureState {
        Listening,
        Started,
        Finished
        ;
    }

    GestureState state = GestureState.Listening;
    float startx = 0, starty = 0;
    float farthestLeft = 0, farthestRight = 0;
    float endx = 0, endy = 0;
    int lastGuess = 0;
    boolean returning = false;

    final int validityOffset = 40;
    final int chromaticMinimum = 40;

    public void HandleEvent(MotionEvent e) {
        switch(state) {
            case Listening:
            case Finished:
                if(e.getPointerCount() == 1 && e.getAction() == ACTION_DOWN) {
                    startx = e.getX(0);
                    starty = e.getY(0);
                    farthestLeft = startx;
                    farthestRight = startx + 1;
                    state = Started;
                    lastGuess = -1000;
                }
                break;
            case Started:
                farthestLeft = Math.min(farthestLeft, e.getX(0));
                farthestRight = Math.max(farthestRight, e.getX(0));
                endx = e.getX(0);
                endy = e.getY(0);
                returning |= endx < farthestRight - validityOffset && endx > farthestLeft + validityOffset;
                if(e.getAction() == ACTION_UP) {
                    state = Finished;
                }
                break;
        }
    }

    public boolean Valid() {
        return farthestRight - farthestLeft > validityOffset;
    }

    public int GetInput() {
        return lastGuess;
    }

    public boolean UpdateInput(int tonic, ScaleType type) {
        boolean toRight = farthestRight - startx > startx - farthestLeft;
        float range = (farthestRight - farthestLeft + 1);
        float relativeEndPos = (endx - farthestLeft + range*1/8) / (range*5/4) *4;
        relativeEndPos = relativeEndPos + (toRight ? (relativeEndPos < 1 ? 0.5f : 0) : (relativeEndPos > 3 ? -0.5f : 0));
        boolean ischromatic = (starty - endy) >  (farthestRight - farthestLeft) / 4 && (starty - endy) > chromaticMinimum;

        int relativeTone = (toRight ? 0 : 4) + (int)(relativeEndPos);
        int tone = 0 +
                tonic +
                type.GetNthTone(relativeTone) +
                (ischromatic ? (toRight ? 1 : -1) : 0);

        if(tone != lastGuess && Valid()) {
            lastGuess = tone;
            return true;
        } else
        {
            return false;
        }
    }

    public boolean Finished() {
        return state == Finished;
    }
}
