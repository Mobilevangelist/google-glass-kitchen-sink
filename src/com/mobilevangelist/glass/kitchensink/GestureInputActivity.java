/*
 * Copyright (C) 2013 Mobilevangelist.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mobilevangelist.glass.kitchensink;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

/**
 * Main activity.
 */
public class GestureInputActivity extends Activity {
  private GestureDetector _gestureDetector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.layout_main);

    TextView titleTextView = (TextView)findViewById(R.id.title);
    titleTextView.setText(R.string.gesture_input);

    TextView instructionsTextView = (TextView)findViewById(R.id.status);
    instructionsTextView.setText(R.string.gesture_instructions);

    _gestureDetector = new GestureDetector(this);
    _gestureDetector.setBaseListener(new GestureListener());
    _gestureDetector.setFingerListener(new FingerGestureListener());
    _gestureDetector.setTwoFingerScrollListener(new TwoFingerGestureListener());
    _gestureDetector.setScrollListener(new ScrollListener());
  }

  /*
  * Send generic motion events to the gesture detector
  */
  @Override
  public boolean onGenericMotionEvent(MotionEvent event) {
    if (_gestureDetector != null) {
      return _gestureDetector.onMotionEvent(event);
    }
    return false;
  }

  private class GestureListener implements GestureDetector.BaseListener {
    @Override
    public boolean onGesture(Gesture gesture) {
      android.util.Log.d("GestureInputActivity", "BaseListener called");
      if (Gesture.TAP == gesture) {
        android.util.Log.d("GestureInputActivity", "TAP");
        return true;
      }
      else if (Gesture.TWO_TAP == gesture) {
        android.util.Log.d("GestureInputActivity", "TWO_TAP");
        return true;
      }
      else if (Gesture.THREE_TAP == gesture) {
        android.util.Log.d("GestureInputActivity", "THREE_TAP");
        return true;
      }
      else if (Gesture.LONG_PRESS == gesture) {
        android.util.Log.d("GestureInputActivity", "LONG_PRESS");
        return true;
      }
      else if (Gesture.TWO_LONG_PRESS == gesture) {
        android.util.Log.d("GestureInputActivity", "TWO_LONG_PRESS");
        return true;
      }
      else if (Gesture.THREE_LONG_PRESS == gesture) {
        android.util.Log.d("GestureInputActivity", "THREE_LONG_PRESS");
        return true;
      }
      else if (Gesture.SWIPE_LEFT == gesture) {
        android.util.Log.d("GestureInputActivity", "SWIPE_LEFT");
        return true;
      }
      else if (Gesture.SWIPE_RIGHT == gesture) {
        android.util.Log.d("GestureInputActivity", "SWIPE_RIGHT");
        return true;
      }
      else if (Gesture.SWIPE_UP == gesture) {
        android.util.Log.d("GestureInputActivity", "SWIPE_UP");
        return true;
      }
      else if (Gesture.SWIPE_DOWN == gesture) {
        android.util.Log.d("GestureInputActivity", "SWIPE_DOWN");
        return true;
      }
      else if (Gesture.TWO_SWIPE_LEFT == gesture) {
        android.util.Log.d("GestureInputActivity", "TWO_SWIPE_LEFT");
        return true;
      }
      else if (Gesture.TWO_SWIPE_RIGHT == gesture) {
        android.util.Log.d("GestureInputActivity", "TWO_SWIPE_RIGHT");
        return true;
      }
      else if (Gesture.TWO_SWIPE_UP == gesture) {
        android.util.Log.d("GestureInputActivity", "TWO_SWIPE_UP");
        return true;
      }
      else if (Gesture.TWO_SWIPE_DOWN == gesture) {
        android.util.Log.d("GestureInputActivity", "TWO_SWIPE_DOWN");
        return true;
      }
      else {
        android.util.Log.d("GestureInputActivity", "Unknown Gesture");
      }
      return false;
    }
  }

  private class FingerGestureListener implements GestureDetector.FingerListener {
    @Override
    public void onFingerCountChanged(int i, int i2) {
      android.util.Log.d("GestureInputActivity", "FingerListener called: i: " + i + ", i2: " + i2);
    }
  }

  private class TwoFingerGestureListener implements GestureDetector.TwoFingerScrollListener {
    @Override
    public boolean onTwoFingerScroll(float v, float v2, float v3) {
      android.util.Log.d("GestureInputActivity", "TwoFingerScrollListener called: v: " + v + ", v2: " + v2 + ", v3: " + v3);
      return false;
    }
  }

  private class ScrollListener implements GestureDetector.ScrollListener {
    @Override
    public boolean onScroll(float v, float v2, float v3) {
      android.util.Log.d("GestureInputActivity", "ScrollListener called: v: " + v + ", v2: " + v2 + ", v3: " + v3);
      return false;
    }
  }
}
