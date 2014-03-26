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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.IOException;

/**
 * Camera preview
 */
public class CameraActivity extends Activity {
  private SurfaceHolder _surfaceHolder;
  private Camera _camera;

  private GestureDetector _gestureDetector;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.camera_preview);

    getWindow().setFormat(PixelFormat.UNKNOWN);
    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
    _surfaceHolder = surfaceView.getHolder();
    _surfaceHolder.addCallback(new SurfaceHolderCallback());

    _gestureDetector = new GestureDetector(this).setBaseListener(new GestureListener());
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      // Handle tap events.
      case KeyEvent.KEYCODE_CAMERA:
        android.util.Log.d("CameraActivity", "Camera button pressed.");
        _camera.takePicture(null, null, new SavePicture());
        android.util.Log.d("CameraActivity", "Picture taken.");

        return true;
      /*case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER:
        android.util.Log.d("CameraActivity", "Tap.");
        _camera.takePicture(null, null, new SavePicture());

        return true;*/
      default:
        return super.onKeyDown(keyCode, event);
    }
  }

  @Override
  public boolean onGenericMotionEvent(MotionEvent event) {
    return _gestureDetector.onMotionEvent(event);
  }

  class GestureListener implements GestureDetector.BaseListener {
    @Override
    public boolean onGesture(Gesture gesture) {
      if (gesture == Gesture.TAP) {
        android.util.Log.d("CameraActivity", "Gesture Tap");

        _camera.takePicture(null, null, new SavePicture());
        return true;
      }
      return false;
    }
  }

  class SurfaceHolderCallback implements SurfaceHolder.Callback {
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      if (null != _camera) {

        try {
          // This camera parameter is set to fix a bug in XE12 that garbles the preview
          Camera.Parameters params = _camera.getParameters();
          params.setPreviewFpsRange(5000, 5000);
          _camera.setParameters(params);

          // Start the preview
          _camera.setPreviewDisplay(_surfaceHolder);
          _camera.startPreview();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      _camera = Camera.open();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
      // Stop the preview and release the camera
      _camera.stopPreview();
      _camera.release();
      android.util.Log.d("CameraActivity", "surfaceDestroyed.");
    }
  }

  class SavePicture implements Camera.PictureCallback {
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
      android.util.Log.d("CameraActivity", "In onPictureTaken().");
      Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

      // Save the image rotated counter-clockwise 90 degrees
      //ImageUtil.savePicture(ImageUtil.rotateImage(image, -90), FrameImage.NO_FRAME_FILENAME);
    }
  }
}
