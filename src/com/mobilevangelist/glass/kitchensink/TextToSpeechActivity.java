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
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;

/**
 * TextToSpeech activity.
 */
public class TextToSpeechActivity extends Activity {
  private TextToSpeech _speech;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.layout_main);

    // Reuse the main layout and change the text
    TextView titleTextView = (TextView)findViewById(R.id.title);
    titleTextView.setText(R.string.star_wars);

    TextView instructionsTextView = (TextView)findViewById(R.id.status);
    instructionsTextView.setText(R.string.tap_to_read);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      // Handle tap events.
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER: {
        // Play a tap sound
        AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audio.playSoundEffect(Sounds.TAP);

        // Read the text aloud
        _speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
          @Override
          public void onInit(int status) {
            _speech.speak(getResources().getString(R.string.star_wars), TextToSpeech.QUEUE_FLUSH, null);
          }
        });

        return true;
      }
      default: {
        return super.onKeyDown(keyCode, event);
      }
    }
  }

}
