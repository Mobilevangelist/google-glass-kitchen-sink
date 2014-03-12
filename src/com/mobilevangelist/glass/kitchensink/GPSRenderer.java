package com.mobilevangelist.glass.kitchensink;

import android.content.Context;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

/**
 * Render the GPS view.
 */
public class GPSRenderer implements SurfaceHolder.Callback, LocationListener {
  private SurfaceHolder _holder;

  private RelativeLayout _gpsLayout;
  private TextView _waitingTextView;
  private RelativeLayout _coordinateLayout;
  private TextView _longitudeTextView;
  private TextView _latitudeTextView;

  private Context _context;

  public GPSRenderer(Context context) {
    LayoutInflater inflater = LayoutInflater.from(context);
    _gpsLayout = (RelativeLayout) inflater.inflate(R.layout.layout_gps, null);

    _waitingTextView = (TextView) _gpsLayout.findViewById(R.id.waitingTextView);

    _coordinateLayout = (RelativeLayout) _gpsLayout.findViewById(R.id.coordinateLayout);
    _longitudeTextView = (TextView) _gpsLayout.findViewById(R.id.longitudeTextView);
    _latitudeTextView = (TextView) _gpsLayout.findViewById(R.id.latitudeTextView);
    _coordinateLayout.setVisibility(View.GONE);

    _context = context;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    _holder = holder;

    GPSThread gps = new GPSThread(_context, this);
    gps.start();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
    int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

    _gpsLayout.measure(measuredWidth, measuredHeight);
    _gpsLayout.layout(0, 0, _gpsLayout.getMeasuredWidth(), _gpsLayout.getMeasuredHeight());

    draw();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {

  }

  @Override
  public void onLocationChanged(Location location) {
    long locationTime = location.getTime();
    android.util.Log.d("GPSActivity", "location provider: " + location.getProvider());
    android.util.Log.d("GPSActivity", "location.latitude: " + location.getLatitude());
    android.util.Log.d("GPSActivity", "location.longitude: " + location.getLongitude());
    android.util.Log.d("GPSActivity", "location time: " + new Date(locationTime));
    android.util.Log.d("GPSActivity", "");

    //_waitingTextView.setVisibility(View.GONE);
    //_coordinateLayout.setVisibility(View.VISIBLE);

    //_longitudeTextView.setText(String.valueOf(location.getLongitude()));
    //_latitudeTextView.setText(String.valueOf(location.getLatitude()));

    //draw();
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
