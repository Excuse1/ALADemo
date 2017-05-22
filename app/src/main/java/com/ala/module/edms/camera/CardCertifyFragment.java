package com.ala.module.edms.camera;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ala.hal.test.R;
import com.ala.module.edms.constant.MsgCons;
import com.ala.module.edms.util.CertifyCameraManager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;

public class CardCertifyFragment extends Fragment implements IPictureCallback {

    private CameraSurfaceView cameraSurfaceView = null;
    private Camera camera;
    private LinearLayout image_a, image_b;
    private FrameLayout linearLayoutCamera;

    private TextView title;

    
    int count = 1;

    MyHandler handler = new MyHandler(this);



    private void mockOpenJump() {
        CardCertifyActivity cca = (CardCertifyActivity) this.getActivity();
        cca.mockOpenJump();
    }


    private void setEnabledAll(boolean enable) {
        CardCertifyActivity cca = (CardCertifyActivity) this.getActivity();
        cca.setEnableAll(enable);
    }

    private CertifyCameraManager mCameraManager;
    private SurfaceHolder holder = null;

    @SuppressLint({ "SimpleDateFormat" })
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.certify_camera, container, false);
        findView(view);
        initParam();
        return view;
    }

    private void findView(View view) {


        linearLayoutCamera = (FrameLayout) view.findViewById(R.id.preciew);

        //title = (TextView) view.findViewById(R.id.title_tv);

    }

    private void initParam() {
        mCameraManager = new CertifyCameraManager();
        camera = mCameraManager.getCamera(getActivity());
        mCameraManager.addCallback(this);
        if (camera == null) {

            setEnabledAll(true);
        } else {
            initSurafceView();
            handler.sendEmptyMessageDelayed(MsgCons.CAMERA_TIMEOUT, 15000);
        }
    }




    class CameraSurfaceView extends SurfaceView {

        public CameraSurfaceView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.addCallback(surfaceCallback);
        }
    }


    /**
     *
     */
    private void initSurafceView() {
        linearLayoutCamera.removeAllViews();
        cameraSurfaceView = new CameraSurfaceView(getActivity());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 576);
        linearLayoutCamera.addView(cameraSurfaceView, params);

        mCameraManager.initSurfaceView(cameraSurfaceView);
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        public void surfaceCreated(SurfaceHolder holder) {
            if (mCameraManager != null) {
                mCameraManager.startPreview(holder);
            }
            handler.sendEmptyMessageDelayed(TAKE_PHOTO, 2000L);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            closeCamera();
        }
    };

    @Override
    public void onStop() {
        closeCamera();
        super.onStop();
    }

    private void closeCamera() {
        if (mCameraManager != null) {
            mCameraManager.closeCamera();
            mCameraManager.delCallback();
            mCameraManager = null;
        }
        if (holder != null) {
            holder.removeCallback(surfaceCallback);
            holder = null;
        }
    }

    public static final int TAKE_PHOTO = 1;
    public static final int JUMP_FRAGMENT = 4;

    private static class MyHandler extends Handler {
        WeakReference<CardCertifyFragment> wr;

        Boolean isTimedOut = false, isPrepared = false;

        MyHandler(CardCertifyFragment fragment) {
            wr = new WeakReference<CardCertifyFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final CardCertifyFragment fragment = wr.get();

            if (fragment == null || !fragment.isResumed()) {
                return;
            }

            switch (msg.what) {
            case JUMP_FRAGMENT:
                if (fragment.count == 10) {
                    isPrepared = true;
                    removeMessages(MsgCons.CAMERA_TIMEOUT);
                    fragment.mCameraManager.delCallback();
                    fragment.mockOpenJump();
                } else {
                    fragment.takePicture();
                    fragment.count++;
                }
                break;
            case TAKE_PHOTO:
                fragment.takePicture();
                break;
            }
        }
    }

    @Override
    public void photoPrepared(Message msg) {
        switch (msg.what) {
        case MsgCons.CAMERA_PHOTO_PREPARED:
            String path = (String) msg.obj;
            if (!new File(path).exists()) {

            }
            handler.sendEmptyMessageDelayed(JUMP_FRAGMENT, 2000L);
            break;

        }
    }

    private void takePicture() {
        mCameraManager.takePhoto();
    }

}
