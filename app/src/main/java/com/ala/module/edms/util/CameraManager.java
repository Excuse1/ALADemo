package com.ala.module.edms.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.os.Message;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.ala.module.edms.camera.IPictureCallback;
import com.ala.module.edms.constant.MsgCons;
import com.ala.module.edms.constant.Settings;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CameraManager implements ErrorCallback {

	String TAG = "CameraManager";
    private Camera camera = null;
    private boolean mPreviewing = false;
    private Parameters cameraParameters = null;
    private SurfaceHolder holder;
    private Context context;
    private IPictureCallback iPictureCallback;

    @SuppressLint({ "SimpleDateFormat" })
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");


    public boolean init(Context context) {
        this.context = context;
        boolean result = false;
        if (camera == null) {
            LogUtil.wInfo("init Camera");
            result = openCamera();
        }
        return result;
    }

    private boolean openCamera() {
        boolean result = false;
        LogUtil.wInfo("openCamera");
        try {
            camera = findCamera();
            if (camera != null) {
                camera.setErrorCallback(this);
                setCameraParameters();
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            LogUtil.wError(e);
            closeCamera();
            result = false;
        }
        return result;
    }

    public Camera findCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 0) {
            return null;
        }
        LogUtil.wInfo("find Camera number:" + numberOfCameras);
        Log.i(TAG, "call findCamera ======= " + numberOfCameras);
        
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Log.e("jjjj",numberOfCameras+"dddd");
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                LogUtil.wInfo("FACING BACK CAMERA");
                return Camera.open(i);
            } else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                LogUtil.wInfo("FACING FRONT CAMERA");
                return Camera.open(i);
            }
        }
        return null;
    }

    public void closeCamera() {
        LogUtil.wInfo("closeCamera");

        Log.i(TAG,"call closeCamera ========================");
        if (holder != null) {
            holder.removeCallback(surfaceCallback);		
            holder = null;
        }

        if (camera == null) {
            return;
        } else {
            camera.setErrorCallback(null);
            if (mPreviewing) {
                stopPreview();
            }
            camera.release();
            camera = null;
        }

    }

    public void startPreview(SurfaceHolder holder) {
        LogUtil.wInfo("startPreview");
        if (!mPreviewing) {
            try {
                if (camera != null) {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                    mPreviewing = true;
                }
            } catch (IOException e) {
                closeCamera();
                e.printStackTrace();
                LogUtil.wError("startPreview failed.");
            }
        }

    }

    private void stopPreview() {
        LogUtil.wInfo("stopPreview");
        if (camera != null) {
            camera.stopPreview();
            mPreviewing = false;
        }
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, android.hardware.Camera arg1) {
            LogUtil.wInfo("save photo");
            String photo_name = "";
            Time t = new Time("GMT+8");
            t.setToNow();
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            String path = Settings.get().getPhotoPath();
            try {

                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                Date date = new Date();
                String save_name = (format.format(date) + ".jpg");
                File saveFile = new File(path + File.separator + save_name);
                LogUtil.wInfo("savedir:" + Settings.get().getPhotoPath() + "/" + save_name);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
                Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                int picWidth = mBitmap.getWidth();
                int picHeight = mBitmap.getHeight();

                Bitmap rBitmap = null;

                if (picWidth > picHeight) {
                    rBitmap = CommUtil.rotaingImageView(90, mBitmap);
                }
                rBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                stopPreview();
                photo_name = save_name;

            } catch (Exception e) {
                LogUtil.wError(e);
            } finally {
                if (iPictureCallback != null) {
                    Message message = Message.obtain();
                    message.obj = photo_name;
                    message.what = MsgCons.CAMERA_PHOTO_PREPARED;
                    iPictureCallback.photoPrepared(message);
                }
            }
        }
    };

    public void addCallback(IPictureCallback iPictureCallback) {
        this.iPictureCallback = iPictureCallback;
    }

    public void delCallback() {
        this.iPictureCallback = null;
    }

    public void initSurfaceView(SurfaceView surfaceView) {
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceCallback);
    }

    private void setCameraParameters() {
        cameraParameters = camera.getParameters();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        List<Camera.Size> pszize = cameraParameters.getSupportedPictureSizes();
        if (null != pszize && 0 < pszize.size()) {
            int height[] = new int[pszize.size()];
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            for (int i = 0; i < pszize.size(); i++) {
                Camera.Size size = (Camera.Size) pszize.get(i);
                int sizeheight = size.height;
                int sizewidth = size.width;
                height[i] = sizeheight;
                map.put(sizeheight, sizewidth);
            }
            Arrays.sort(height);
            cameraParameters.setPictureSize(720, 1280);
        } else {
            cameraParameters.setPictureSize(dm.widthPixels, dm.heightPixels);
            Log.e("jjjj","jjjjjjjjjjjjjjjjjjjjjjjjj");
        }
        cameraParameters.setPictureFormat(ImageFormat.JPEG);
        camera.setDisplayOrientation(90);
        cameraParameters.set("jpeg-quality", 100);
        camera.setParameters(cameraParameters);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        public void surfaceCreated(SurfaceHolder holder) {
            LogUtil.wInfo("surfaceCreated");
            if (camera != null) {
                startPreview(holder);
           /*     try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                camera.takePicture(null, null, pictureCallback);
            } else {
                LogUtil.wInfo("camera null");
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtil.wInfo("surfaceDestroyed");
            closeCamera();
        }
    };

    @Override
    public void onError(int error, Camera camera) {
        LogUtil.wInfo("Camera error:" + error);
        if (iPictureCallback != null) {
            Message message = Message.obtain();
            message.obj = "";
            iPictureCallback.photoPrepared(message);
        }
        closeCamera();
    }

}
