package com.ala.module.edms.camera;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;

import com.ala.hal.test.BaseActivity;
import com.ala.hal.test.R;

public class CardCertifyActivity extends BaseActivity {
    int times;
    int test_count;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        setContentView(R.layout.activity_certify);

    }

    /**
     * fragment 加到activity_certify
     * @param fragment
     */
    private void jumpFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_layout, fragment);
        ft.commitAllowingStateLoss();
    }



    public void mockOpenJump() {
        handler.postDelayed(new Runnable() {
            public void run() {
                if (times >= test_count) {

                    setEnabledAll(true);
                    return;
                }
                keepTest();
            }
        }, 2000);
    }

    public void setEnableAll(boolean enable) {
        setEnabledAll(enable);
    }
    
    private void keepTest() {
        times++;
        jumpFragment(new CardCertifyFragment());
    }

    @Override
    protected void gotestImpl() {
        test_count = parseInput(10);
        times = 0;

        super.setEnabledAll(false);
        keepTest();

    }

}
