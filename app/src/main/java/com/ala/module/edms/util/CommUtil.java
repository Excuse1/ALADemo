package com.ala.module.edms.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class CommUtil {


    public static String getDeltaTimeSeconds(String startDate, String endDate) throws IllegalArgumentException {
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        timeFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        DateFormat df = timeFmt;
        try {
            Date d1 = df.parse(startDate);
            Date d2 = df.parse(endDate);
            long diff = d2.getTime() - d1.getTime();
            if (diff < 0) {
                throw new IllegalArgumentException("鏃堕棿宸负璐�");
            }
            SimpleDateFormat secondFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            secondFmt.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            return secondFmt.format(diff);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap resizedBitmap = bitmap;
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        if (picWidth > picHeight) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, picWidth, picHeight, matrix, true);
        }
        return resizedBitmap;
    }

}


