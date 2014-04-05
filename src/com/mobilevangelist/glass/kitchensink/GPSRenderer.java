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
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.glass.timeline.DirectRenderingCallback;

import java.util.Date;

/**
 * Render the GPS view.
 */
public class GPSRenderer implements DirectRenderingCallback, LocationListener {
  private SurfaceHolder _holder;

  private RelativeLayout _gpsLayout;
  private TextView _waitingTextView;
  private RelativeLayout _coordinateLayout;
  private TextView _latitudeTextView;
  private TextView _longitudeTextView;
  private GPSThread _gps;

  private Context _context;

  public GPSRenderer(Context context) {
    android.util.Log.d("GPSRenderer", "In GPSRenderer constructor()");

    LayoutInflater inflater = LayoutInflater.from(context);
    _gpsLayout = (RelativeLayout) inflater.inflate(R.layout.layout_gps, null);

    _waitingTextView = (TextView) _gpsLayout.findViewById(R.id.waitingTextView);
    _coordinateLayout = (RelativeLayout) _gpsLayout.findViewById(R.id.coordinateLayout);
    _coordinateLayout.setVisibility(View.INVISIBLE);

    _latitudeTextView = (TextView) _gpsLayout.findViewById(R.id.latitudeTextView);
    _longitudeTextView = (TextView) _gpsLayout.findViewById(R.id.longitudeTextView);

    _context = context;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    android.util.Log.d("GPSRenderer", "In surfaceCreated()");

    _holder = holder;

    _gps = new GPSThread(_context, this);
    _gps.start();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    android.util.Log.d("GPSRenderer", "In surfaceChanged()");

    int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
    int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

    _gpsLayout.measure(measuredWidth, measuredHeight);
    _gpsLayout.layout(0, 0, _gpsLayout.getMeasuredWidth(), _gpsLayout.getMeasuredHeight());

    draw();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    _gps.haltUpdates();
  }


  @Override
  public void renderingPaused(SurfaceHolder surfaceHolder, boolean b) {

  }

  @Override
  public void onLocationChanged(Location location) {
    long locationTime = location.getTime();
    android.util.Log.d("GPSRenderer", "location provider: " + location.getProvider());
    android.util.Log.d("GPSRenderer", "location.latitude: " + location.getLatitude());
    android.util.Log.d("GPSRenderer", "location.longitude: " + location.getLongitude());
    android.util.Log.d("GPSRenderer", "location time: " + new Date(locationTime));
    android.util.Log.d("GPSRenderer", "");

    _waitingTextView.setVisibility(View.INVISIBLE);
    _coordinateLayout.setVisibility(View.VISIBLE);

    _latitudeTextView.setText(String.valueOf(location.getLatitude()));
    _longitudeTextView.setText(String.valueOf(location.getLongitude()));

    draw();
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

  private  void draw() {
    Canvas canvas = null;

    try {
      canvas = _holder.lockCanvas();
    }
    catch (RuntimeException e) {
      android.util.Log.d("GPSRenderer", "RuntimeException", e);
    }

    if (canvas != null) {
      canvas.drawColor(Color.BLACK);
      _gpsLayout.draw(canvas);

      try {
        _holder.unlockCanvasAndPost(canvas);
      }
      catch (RuntimeException e) {
        Log.d("GPSRenderer", "RuntimeException: ", e);
      }
    }
  }
}
