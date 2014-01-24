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

package com.mobilevangelist.glass.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;

/**
 * Main activity.
 */
public class HelloWorldActivity extends Activity {

  private TextView _statusTextView;

  private TextToSpeech _speech;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Even though the text-to-speech engine is only used in response to a menu action, we
    // initialize it when the application starts so that we avoid delays that could occur
    // if we waited until it was needed to start it up.
    _speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        _speech.speak("Hello, World", TextToSpeech.QUEUE_FLUSH, null);
      }
    });

    // An alternative way to layout the UX
    setContentView(R.layout.layout_helloworld);
    _statusTextView = (TextView)findViewById(R.id.status);
  }

  /**
   * Handle the tap event from the touchpad.
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
    // Handle tap events.
    case KeyEvent.KEYCODE_DPAD_CENTER:
    case KeyEvent.KEYCODE_ENTER:

      // Status message below the main text in the alternative UX layout
      AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
      audio.playSoundEffect(Sounds.TAP);

      _statusTextView.setText(R.string.touchpad_touched);

      _speech.speak("Touchpad touched", TextToSpeech.QUEUE_FLUSH, null);

      // Start the scrollable intent
      Intent intent = new Intent("com.mobilevangelist.glass.helloworld.SCROLL_LIST");
      startActivity(intent);

      return true;
    default:
      return super.onKeyDown(keyCode, event);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
