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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity.
 */
public class HelloWorldScrollActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Inflate the xml layout
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View layoutView = inflater.inflate(R.layout.layout_helloworld, null);

    TextView statusTextView = (TextView)layoutView.findViewById(R.id.status);
    statusTextView.setText(getResources().getString(R.string.swipe_instructions));

    // Glass Card class
    Card card = new Card(this);
    card.setText(R.string.app_name);
    card.setFootnote(getResources().getString(R.string.footnote));
    View cardView = card.toView();

    // Put both views in a list
    List<View> viewList = new ArrayList<View>();
    viewList.add(layoutView);
    viewList.add(cardView);

    // Populate the CardScrollView with the viewList
    CardScrollView cardScrollView = new CardScrollView(this);
    ScrollAdapter adapter = new ScrollAdapter(viewList);
    cardScrollView.setAdapter(adapter);
    cardScrollView.activate();
    setContentView(cardScrollView);
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

  public class ScrollAdapter extends CardScrollAdapter {
    private List<View> _cardList;

    public ScrollAdapter(List<View> cardList) {
      _cardList = cardList;
    }

    @Override
    public int findIdPosition(Object id) {
      return -1;
    }

    @Override
    public int findItemPosition(Object item) {
      return _cardList.indexOf(item);
    }

    @Override
    public int getCount() {
      return _cardList.size();
    }

    @Override
    public Object getItem(int position) {
      return _cardList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      return _cardList.get(position);
    }
  }
}
