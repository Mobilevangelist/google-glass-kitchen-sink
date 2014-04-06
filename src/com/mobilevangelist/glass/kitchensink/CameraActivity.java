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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;  // For option 2
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.CameraManager;
import com.google.android.glass.media.Sounds;  // For option 2
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
  private boolean _previewOn;

  Context _context = this;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.camera_preview);

    // Set up the camera preview UX
    getWindow().setFormat(PixelFormat.UNKNOWN);
    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
    _surfaceHolder = surfaceView.getHolder();
    _surfaceHolder.addCallback(new SurfaceHolderCallback());
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      // Hardware camera button
      case KeyEvent.KEYCODE_CAMERA: {
        android.util.Log.d("CameraActivity", "Camera button pressed.");

        // Release the camera before the picture is taken
        _camera.stopPreview();
        _camera.release();
        _previewOn = false;

        // Return false to allow the camera button to do its default action
        return false;
      }
      // Touchpad tap
      case KeyEvent.KEYCODE_DPAD_CENTER:  // Alternative way to take a picture
      case KeyEvent.KEYCODE_ENTER: {
        android.util.Log.d("CameraActivity", "Tap.");

        // Option 1: release the camera and use the ACTION_IMAGE_CAPTURE intent
        _camera.stopPreview();
        _camera.release();
        _previewOn = false;  // Don't release the camera in surfaceDestroyed()

        // Use the image capture intent to take the picture and process it with onActivityResult()
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);

        // Option 2: capture the picture yourself and process it with a Camera.PictureCallback

        // Play a sound to indicate the take picture action succeeded
        //AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //audio.playSoundEffect(Sounds.SUCCESS);

        // Take the picture
        //_camera.takePicture(null, null, new SavePicture());

        return true;
      }
      default: {
        return super.onKeyDown(keyCode, event);
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)  {
    if (RESULT_OK == resultCode) {
      // Insert the thumbnail into the timeline
      insertIntoTimeline(data.getStringExtra(CameraManager.EXTRA_THUMBNAIL_FILE_PATH));
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  // Used with option 2
  // Create the image fliename with the current timestamp
  private String getFilename(boolean isThumbnail) {
    android.util.Log.d("CameraActivity", "Saving picture...");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

    // Build the image filename
    StringBuilder imageFilename = new StringBuilder();
    imageFilename.append(sdf.format(new Date()));
    if (isThumbnail) {
      imageFilename.append("_tn");
    }
    imageFilename.append(".jpg");

    // Return the full path to the image - image is saved in the default picture directory for XE12
    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera" + File.separator + imageFilename;
  }

  // Used for Option 2
  // Scales an image to create a thumbnail
  public static Bitmap scaleImage(Bitmap source, float scale) {
    Matrix matrix = new Matrix();
    matrix.postScale(scale, scale);

    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
  }

  // Used for Option 2
  // Write the image to local storage
  public void savePicture(Bitmap image, String filename) throws IOException {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(filename);

      image.compress(Bitmap.CompressFormat.JPEG, 100, fos);

      android.util.Log.d("CameraActivity", "Picture saved.");
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

  // Insert the captured picture into the timeline
  private void insertIntoTimeline(String imageFilename) {
    // Create a URI from the file
    Uri uri = Uri.fromFile(new File(imageFilename));
    android.util.Log.d("CameraActivity", "imageFilename: " + imageFilename);
    android.util.Log.d("CameraActivity", "uri: " + uri);

    // Create the card
    Card photoCard = new Card(_context);
    photoCard.setImageLayout(Card.ImageLayout.FULL);
    photoCard.addImage(uri);

    // Insert the card into the timeline
    android.util.Log.d("CameraActivity", "Inserting picture into timeline.");
    TimelineManager.from(_context).insert(photoCard);
  }

  // Handling of the camera preview
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
          _previewOn = true;
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
      if (_previewOn) {
        // Stop the preview and release the camera
        _camera.stopPreview();
        _camera.release();
      }
    }
  }

  // Used for option 2
  // Callback that is called when the picture is taken
  class SavePicture implements Camera.PictureCallback {
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
      android.util.Log.d("CameraActivity", "Picture taken.");
      Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

      try {
        // Save the image
        String imageFilename = getFilename(false);
        savePicture(image, imageFilename);

        // Create a thumbnail for the timeline - anything bigger than this doesn't show up
        String thumbnailImageFilename = getFilename(true);
        savePicture(scaleImage(image, (float)0.85), thumbnailImageFilename);

        // This doesn't work unless you have a thumbnail
        insertIntoTimeline(thumbnailImageFilename);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
