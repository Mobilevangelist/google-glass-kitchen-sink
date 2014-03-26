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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.timeline.TimelineManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Camera preview
 */
public class CameraActivity extends Activity {
  private SurfaceHolder _surfaceHolder;
  private Camera _camera;

  Context _context = this;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.camera_preview);

    getWindow().setFormat(PixelFormat.UNKNOWN);
    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
    _surfaceHolder = surfaceView.getHolder();
    _surfaceHolder.addCallback(new SurfaceHolderCallback());
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    switch (keyCode) {
      // Handle tap events.
      case KeyEvent.KEYCODE_CAMERA:
        android.util.Log.d("CameraActivity", "Camera button pressed.");

        audio.playSoundEffect(Sounds.TAP);

        _camera.takePicture(null, null, new SavePicture());
        android.util.Log.d("CameraActivity", "Picture taken.");

        return true;
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER:
        android.util.Log.d("CameraActivity", "Tap.");

        audio.playSoundEffect(Sounds.SUCCESS);

        _camera.takePicture(null, null, new SavePicture());

        return true;
      default:
        return super.onKeyDown(keyCode, event);
    }
  }

  public String savePicture(Bitmap image) throws IOException {
    android.util.Log.d("CameraActivity", "Saving picture...");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
    String imageFilename = sdf.format(new Date()) + ".jpg";
    String imageFullPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera" + File.separator + imageFilename;
    //android.util.Log.d("CameraActivity", "external dcim dir: " + Environment.getExternalStoragePublicDirectory(Environment.));

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(imageFullPath);

      image.compress(Bitmap.CompressFormat.JPEG, 100, fos);

      android.util.Log.d("CameraActivity", "Picture saved.");
      return imageFullPath;
    }
    catch (IOException e) {
      e.printStackTrace();

      throw(e);
    }
    finally {
      if (null != fos) {
        try {
          fos.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
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

      try {
        // Save the image
        String imageFilename = savePicture(image);

        Uri uri = Uri.fromFile(new File(imageFilename));
        android.util.Log.d("CameraActivity", "imageFilename: " + imageFilename);
        android.util.Log.d("CameraActivity", "uri: " + uri);
        Card photoCard = new Card(_context);
        photoCard.setImageLayout(Card.ImageLayout.FULL);
        photoCard.addImage(uri);
        android.util.Log.d("CameraActivity", "Inserting into timeline.");
        TimelineManager.from(_context).insert(photoCard);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
