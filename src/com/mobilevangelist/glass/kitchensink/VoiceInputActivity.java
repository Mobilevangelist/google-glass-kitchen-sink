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
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import com.google.android.glass.app.Card;

import java.util.List;

/**
 * VoiceInput activity.
 */
public class VoiceInputActivity extends Activity {
  private final static int SPEECH_REQUEST = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getVoiceInput();
  }

  public void getVoiceInput() {
    // Start the intent to ask the user for voice input
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
    startActivityForResult(intent, SPEECH_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode,
                                  Intent data) {
    // When the voice input intent returns and is ok
    if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
      // Get the text spoken
      List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      String spokenText = results.get(0);
      android.util.Log.d("VoiceInputActivity", "customization text: " + spokenText);

      // Add the text to the view so the user knows we retrieved it correctly
      Card card = new Card(this);
      card.setText(spokenText);
      View cardView = card.toView();
      setContentView(cardView);
    }

    super.onActivityResult(requestCode, resultCode, data);
  }
}
