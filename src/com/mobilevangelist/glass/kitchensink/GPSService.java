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

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.SurfaceHolder;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

/**
 * GPS service to read GPS coordinates.
 */
public class GPSService extends Service {
  private TimelineManager _timelineManager;
  private LiveCard _liveCard;
  private SurfaceHolder.Callback _renderer;

  @Override
  public void onCreate() {
    _timelineManager = TimelineManager.from(this);
    _renderer = new GPSRenderer(this);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    android.util.Log.d("GPSService", "In onStartCommand()");

    if (_liveCard == null) {
      _liveCard = _timelineManager.createLiveCard("KitchenSinkGPS");

      _liveCard.setDirectRenderingEnabled(true);
      _liveCard.getSurfaceHolder().addCallback(_renderer);

      // Display the options menu when the live card is tapped.
      Intent menuIntent = new Intent(this, GPSMenuActivity.class);
      menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

      _liveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));

      _liveCard.publish(LiveCard.PublishMode.REVEAL);
    }

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    android.util.Log.d("GPSService", "GPSService destroyed");

    if ((null != _liveCard) && (_liveCard.isPublished())) {
      _liveCard.unpublish();
      _liveCard.getSurfaceHolder().removeCallback(_renderer);
      _liveCard = null;
    }

    super.onDestroy();
  }
}
