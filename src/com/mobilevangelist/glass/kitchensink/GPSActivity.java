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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Date;
import java.util.List;

/**
 * GPS activity.
 */
public class GPSActivity extends Activity {
  private final static long MIN_TIME = 1000;  // 1 sec
  private final static long MIN_DISTANCE = 1; // 1 meter

  LocationManager _locationManager;
  private List<String> _providers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initGPS();
    for (int i=0; i<20; i++) {
      getUpdate();
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void initGPS() {
    _locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);

    _providers = _locationManager.getProviders(criteria, true);
  }

  private void getUpdate() {
    for (String provider : _providers) {
      android.util.Log.d("GPSActivity", "Calling requestLocaitonUpdates from " + provider);
      _locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, new GetLocation());
    }
  }

  class GetLocation implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
      long locationTime = location.getTime();
      android.util.Log.d("GPSActivity", "location provider: " + location.getProvider());
      android.util.Log.d("GPSActivity", "location.latitude: " + location.getLatitude());
      android.util.Log.d("GPSActivity", "location.longitude: " + location.getLongitude());
      android.util.Log.d("GPSActivity", "location time: " + new Date(locationTime));
      android.util.Log.d("GPSActivity", "");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
  }
}
