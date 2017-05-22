package com.ala.hal.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public abstract class BaseActivity extends Activity {


    private Button  btn_go, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        btn_go = (Button) findViewById(R.id.btn_go);
        btn_back = (Button) findViewById(R.id.btn_back);


        btn_go.setOnClickListener(listener);
        btn_back.setOnClickListener(listener);

    }

    /**
     * 模板方法
     */
    protected abstract void gotestImpl();

    private void gotest() {
        gotestImpl();
    }

    OnClickListener listener = new OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btn_go:
                gotest();
                break;


            case R.id.btn_back:
                finish();
                break;
            default:
                break;
            }
        }
    };
    protected void setEnabledAll(boolean enabled) {
        btn_go.setEnabled(enabled);
        btn_back.setEnabled(enabled);
    }

    protected int parseInput( int defaultValue) {
            return defaultValue;
    }
}
