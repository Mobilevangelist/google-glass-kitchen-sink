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
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.glass.media.Sounds;

/**
 * Main activity.
 */
public class MainActivity extends Activity {

  public static final String VOICE_INPUT    = "com.mobilevangelist.glass.kitchensink.VOICE_INPUT";
  public static final String TEXT_TO_SPEECH = "com.mobilevangelist.glass.kitchensink.TEXT_TO_SPEECH";
  public static final String DPAD_INPUT     = "com.mobilevangelist.glass.kitchensink.DPAD_INPUT";
  public static final String GESTURE_INPUT  = "com.mobilevangelist.glass.kitchensink.GESTURE_INPUT";
  public static final String CAMERA         = "com.mobilevangelist.glass.kitchensink.CAMERA";
  public static final String ACCELEROMETER  = "com.mobilevangelist.glass.kitchensink.ACCELEROMETER";
  public static final String GPS            = "com.mobilevangelist.glass.kitchensink.GPS";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.layout_main);
  }

  /**
   * Handle the tap event from the touchpad.
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      // Handle tap events.
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER: {
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.playSoundEffect(Sounds.TAP);

        // Show the menu
        openOptionsMenu();
        return true;
      }
      default: {
        return super.onKeyDown(keyCode, event);
      }
    }
  }

  // Create the menu
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.feature_menu, menu);

    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.voiceInputMenuItem: {
        // Open new activity to do voice input
        Intent intent = new Intent(VOICE_INPUT);
        startActivity(intent);

        return true;
      }
      case R.id.textToSpeechMenuItem: {
        // Open new activity to show an example of TextToSpeech
        Intent intent = new Intent(TEXT_TO_SPEECH);
        startActivity(intent);

        return true;
      }
      case R.id.dpadInputMenuItem: {
        // Open new activity to do accept touchpad input
        Intent intent = new Intent(DPAD_INPUT);
        startActivity(intent);

        return true;
      }
      case R.id.gestureInputMenuItem: {
        // Open new activity to process touchpad gestures
        Intent intent = new Intent(GESTURE_INPUT);
        startActivity(intent);

        return true;
      }
      case R.id.cameraMenuItem: {
        // Open camera preview
        Intent intent = new Intent(CAMERA);
        startActivity(intent);

        return true;
      }
      case R.id.accelerometerMenuItem: {
        // Open new activity to show accelerometer readings
        Intent intent = new Intent(ACCELEROMETER);
        startActivity(intent);

        return true;
      }
      case R.id.gpsMenuItem: {
        // Show a LiveCard that gets GPS updates
        Intent intent = new Intent(GPS);
        startActivity(intent);

        return true;
      }
      default: {
        return super.onOptionsItemSelected(item);
      }
    }
  }
}
