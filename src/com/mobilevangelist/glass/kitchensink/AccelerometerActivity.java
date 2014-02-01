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

/**
 * Accelerometer activity.
 */
public class AccelerometerActivity extends Activity {
  private SensorManager _sensorManager;
  private Sensor _accelerometer;
  private AccelerometerListener _accelerometerListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
      double[] linear_acceleration = new double[3];

      gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
      gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
      gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

      linear_acceleration[0] = event.values[0] - gravity[0];
      linear_acceleration[1] = event.values[1] - gravity[1];
      linear_acceleration[2] = event.values[2] - gravity[2];

      Log.d("AccelerometerActivity", "--------------------------");
      Log.d("AccelerometerActivity", "gravity[0]: " + gravity[0]);
      Log.d("AccelerometerActivity", "gravity[1]: " + gravity[1]);
      Log.d("AccelerometerActivity", "gravity[2]: " + gravity[2]);
      Log.d("AccelerometerActivity", "linear_acceleration[0]: " + linear_acceleration[0]);
      Log.d("AccelerometerActivity", "linear_acceleration[1]: " + linear_acceleration[1]);
      Log.d("AccelerometerActivity", "linear_acceleration[2]: " + linear_acceleration[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
  }
}
