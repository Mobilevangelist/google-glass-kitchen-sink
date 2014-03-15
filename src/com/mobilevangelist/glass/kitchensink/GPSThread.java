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

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

import java.util.List;

/**
 * GPS service to read GPS coordinates.
 */
public class GPSThread extends Thread {
  private final static long MIN_TIME = 500;  // 1/2 sec
  private final static long MIN_DISTANCE = 1; // 1 meter

  private LocationListener _listener;
  private LocationManager _locationManager;
  private List<String> _providers;

  public GPSThread(Context context, LocationListener listener) {
    _listener = listener;

    init(context);
  }

  @Override
  public void run() {
    Looper.prepare();
      getUpdates();
    Looper.loop();
  }

  private void init(Context context) {
    _locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);

    _providers = _locationManager.getProviders(criteria, true);
  }

  private void getUpdates() {
    for (String provider : _providers) {
      android.util.Log.d("GPSThread", "Calling requestLocationUpdates from " + provider);
      _locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, _listener);
    }
  }

  public void haltUpdates() {
    _locationManager.removeUpdates(_listener);
  }
}
