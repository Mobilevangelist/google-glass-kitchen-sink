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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Camera preview
 */
public class CameraPreview extends SurfaceView {
  private Camera _camera;

  public CameraPreview(Context context) {
    super(context);

    getHolder().addCallback(new SurfaceHolderCallback());
  }

  public Camera getCamera() {
    return _camera;
  }

  public CameraPreview(Context context, AttributeSet attrs) {
    super(context, attrs);

    getHolder().addCallback(new SurfaceHolderCallback());
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
          _camera.setPreviewDisplay(holder);
          _camera.startPreview();
          _camera.autoFocus(null);
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
