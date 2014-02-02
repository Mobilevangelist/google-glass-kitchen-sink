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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Accelerometer activity.
 */
public class AccelerometerActivity extends Activity {
  private final static int X = 0;
  private final static int Y = 1;
  private final static int Z = 2;

  private TextView _gravityXTextView;
  private TextView _gravityYTextView;
  private TextView _gravityZTextView;
  private TextView _linearAccelXTextView;
  private TextView _linearAccelYTextView;
  private TextView _linearAccelZTextView;

  private SensorManager _sensorManager;
  private Sensor _accelerometer;
  private AccelerometerListener _accelerometerListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.layout_accelerometer);
    _gravityXTextView = (TextView)findViewById(R.id.gravityXValueTextView);
    _gravityYTextView = (TextView)findViewById(R.id.gravityYValueTextView);
    _gravityZTextView = (TextView)findViewById(R.id.gravityZValueTextView);
    _linearAccelXTextView = (TextView)findViewById(R.id.linearAccelerationXValueTextView);
    _linearAccelYTextView = (TextView)findViewById(R.id.linearAccelerationYValueTextView);
    _linearAccelZTextView = (TextView)findViewById(R.id.linearAccelerationZValueTextView);

    _sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    _accelerometerListener = new AccelerometerListener();
  }

  @Override
  public void onResume() {
    super.onResume();

    _sensorManager.registerListener(_accelerometerListener, _accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  public void onPause() {
    super.onPause();

    _sensorManager.unregisterListener(_accelerometerListener);
  }

  private class AccelerometerListener implements SensorEventListener {
    @Override
    public void onSensorChanged(SensorEvent event) {
      // Example code pulled from Android API
      // See http://developer.android.com/reference/android/hardware/SensorEvent.html

      // alpha is calculated as t / (t + dT)
      // with t, the low-pass filter's time-constant
      // and dT, the event delivery rate

      final double alpha = 0.8;
      double[] gravity = new double[3];
      double[] linearAcceleration = new double[3];

      gravity[X] = alpha * gravity[X] + (1 - alpha) * event.values[X];
      gravity[Y] = alpha * gravity[Y] + (1 - alpha) * event.values[Y];
      gravity[Z] = alpha * gravity[Z] + (1 - alpha) * event.values[Z];

      linearAcceleration[X] = event.values[X] - gravity[X];
      linearAcceleration[Y] = event.values[Y] - gravity[Y];
      linearAcceleration[Z] = event.values[Z] - gravity[Z];

      Log.d("AccelerometerActivity", "--------------------------");
      Log.d("AccelerometerActivity", "gravity[X]: " + gravity[X]);
      Log.d("AccelerometerActivity", "gravity[Y]: " + gravity[Y]);
      Log.d("AccelerometerActivity", "gravity[Z]: " + gravity[Z]);
      Log.d("AccelerometerActivity", "linear acceleration[X]: " + linearAcceleration[X]);
      Log.d("AccelerometerActivity", "linear acceleration[Y]: " + linearAcceleration[Y]);
      Log.d("AccelerometerActivity", "linear acceleration[Z]: " + linearAcceleration[Z]);

      DecimalFormat df = new DecimalFormat("0.0");

      _gravityXTextView.setText(df.format(gravity[X]));
      _gravityYTextView.setText(df.format(gravity[Y]));
      _gravityZTextView.setText(df.format(gravity[Z]));
      _linearAccelXTextView.setText(df.format(linearAcceleration[X]));
      _linearAccelYTextView.setText(df.format(linearAcceleration[Y]));
      _linearAccelZTextView.setText(df.format(linearAcceleration[Z]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
  }
}
