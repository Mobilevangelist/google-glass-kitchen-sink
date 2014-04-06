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
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;

/**
 * DPad Input activity.
 */
public class DpadInputActivity extends Activity {
  private TextView _titleTextView;
  private TextView _statusTextView;

  private int _swipeDownCount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.layout_main);

    // Save the TextViews for updating later
    _titleTextView = (TextView)findViewById(R.id.title);
    _titleTextView.setText(R.string.dpad_input);
    _statusTextView = (TextView)findViewById(R.id.status);
    _statusTextView.setText(R.string.dpad_instructions);

    _swipeDownCount = 0;
  }

  /**
   * Handle the tap event from the touchpad.
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    switch (keyCode) {
      // Tap
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER: {

        // Status message below the main text in the alternative UX layout
        audio.playSoundEffect(Sounds.TAP);

        _titleTextView.setText(R.string.tap);
        _statusTextView.setText(R.string.empty_string);
        _swipeDownCount = 0;

        return true;
      }
      // Hardware camera button
      case KeyEvent.KEYCODE_CAMERA: {

        _titleTextView.setText(R.string.camera_button);
        _statusTextView.setText(R.string.empty_string);
        _swipeDownCount = 0;

        return true;
      }
      // Swipe down - print the capture the first time and perform the cancel the second time
      case KeyEvent.KEYCODE_BACK: {

        // Status message below the main text in the alternative UX layout
        audio.playSoundEffect(Sounds.DISMISSED);

        _titleTextView.setText(R.string.swipe_down);
        _statusTextView.setText(R.string.swipe_to_go_back);
        _swipeDownCount++;

        if (_swipeDownCount < 2) {
          return true;
        }
        // Else do the back action
      }
      default: {
        return super.onKeyDown(keyCode, event);
      }
    }
  }
}
