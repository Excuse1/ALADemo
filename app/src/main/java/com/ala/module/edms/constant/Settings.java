package com.ala.module.edms.constant;

import android.os.Environment;

import com.ala.module.edms.util.FileUtil;

import java.io.File;

/**
 * 设置路径 去的路径
 */
public class Settings {

    private Settings() {
        FileUtil.mkdir(storagePath);
    }

    private String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath().concat(File.separator)
            .concat("hitester"); // /ExternalStorageDirectory/appName

    static Settings instance;

    public static Settings get() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getLogPath() {
        return storagePath + File.separator + "log";
    }

    public String getPhotoPath() {
        return storagePath + File.separator + "photo";
    }

    public String getCertifyPath() {
        return storagePath + File.separator + "certify";
    }


}
