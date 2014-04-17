/*
 * Copyright (C) 2014 Mobilevangelist.
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
import android.view.MotionEvent;
import android.widget.TextView;
import android.util.Log;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.HashMap;

/**
 * Gesture Input activity.
 */
public class GestureInputActivity extends Activity {
  private GestureDetector _gestureDetector;

  private TextView _titleTextView;
  private TextView _instructionsTextView;

  private int _swipeDownCount;

  // Create a map that converts the gesture to a string
  private static HashMap<Gesture, Integer> _gestureMap = new HashMap<Gesture, Integer>();
  static {
    _gestureMap.put(Gesture.TAP, R.string.tap);
    _gestureMap.put(Gesture.TWO_TAP, R.string.two_tap);
    _gestureMap.put(Gesture.THREE_TAP, R.string.three_tap);
    _gestureMap.put(Gesture.LONG_PRESS, R.string.long_press);
    _gestureMap.put(Gesture.TWO_LONG_PRESS, R.string.two_long_press);
    _gestureMap.put(Gesture.THREE_LONG_PRESS, R.string.three_long_press);
    _gestureMap.put(Gesture.SWIPE_LEFT, R.string.swipe_left);
    _gestureMap.put(Gesture.TWO_SWIPE_LEFT, R.string.two_swipe_left);
    _gestureMap.put(Gesture.SWIPE_RIGHT, R.string.swipe_right);
    _gestureMap.put(Gesture.TWO_SWIPE_RIGHT, R.string.two_swipe_right);
    _gestureMap.put(Gesture.SWIPE_UP, R.string.swipe_up);
    _gestureMap.put(Gesture.TWO_SWIPE_UP, R.string.two_swipe_up);
    _gestureMap.put(Gesture.SWIPE_DOWN, R.string.swipe_down);
    _gestureMap.put(Gesture.TWO_SWIPE_DOWN, R.string.two_swipe_down);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.layout_main);

    // Save the TextViews for updating later
    _titleTextView = (TextView)findViewById(R.id.title);
    _titleTextView.setText(R.string.gesture_input);

    _instructionsTextView = (TextView)findViewById(R.id.status);
    _instructionsTextView.setText(R.string.gesture_instructions);

    // Initialze the GestureDetector
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
    // Call the GestureDetector on any motion event
    if (null != _gestureDetector) {
      return _gestureDetector.onMotionEvent(event);
    }

    return false;
  }

  // Captures gestures and prints them out
  private class GestureListener implements GestureDetector.BaseListener {
    @Override
    public boolean onGesture(Gesture gesture) {
      Log.d("GestureInputActivity", "BaseListener called");
      Log.d("GestureInputActivity", getResources().getString(_gestureMap.get(gesture)));

      // Show the gesture in the Glass interface
      _titleTextView.setText(_gestureMap.get(gesture));
      _instructionsTextView.setText(R.string.empty_string);

      if (Gesture.SWIPE_DOWN == gesture) {
        _instructionsTextView.setText(R.string.swipe_to_go_back);
        _swipeDownCount++;

        return (_swipeDownCount < 2);
      }
      else {
        _swipeDownCount = 0;
      }

      return true;
    }
  }

  // Example of a listener for a finger gesture
  private class FingerGestureListener implements GestureDetector.FingerListener {
    @Override
    public void onFingerCountChanged(int i, int i2) {
      Log.d("GestureInputActivity", "FingerListener called: i: " + i + ", i2: " + i2);
    }
  }

  // Example of a listener for a two finger gesture
  private class TwoFingerGestureListener implements GestureDetector.TwoFingerScrollListener {
    @Override
    public boolean onTwoFingerScroll(float v, float v2, float v3) {
      Log.d("GestureInputActivity", "TwoFingerScrollListener called: v: " + v + ", v2: " + v2 + ", v3: " + v3);
      return false;
    }
  }

  // Example of a listener for a scroll gesture
  private class ScrollListener implements GestureDetector.ScrollListener {
    @Override
    public boolean onScroll(float v, float v2, float v3) {
      Log.d("GestureInputActivity", "ScrollListener called: v: " + v + ", v2: " + v2 + ", v3: " + v3);
      return false;
    }
  }
}
