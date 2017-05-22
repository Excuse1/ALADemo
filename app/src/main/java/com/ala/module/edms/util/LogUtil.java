package com.ala.module.edms.util;

import com.ala.module.edms.constant.Settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class LogUtil {

    public static void error(String tag, String content) {
        if (true) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
            String fullName = ste.getClassName();
            String className = fullName.substring(fullName.lastIndexOf(".") + 1);
            tag = "[" + className + "." + ste.getMethodName() + ":Line No." + ste.getLineNumber() + " " + tag + "]";
            android.util.Log.e(tag, content);
            // Log.e(tag, content);
            // LogUtil.writeExceptionLog(tag+"+++++"+content);
        }
    }

    public static void info(String tag, String content) {
        if (true) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
            String fullName = ste.getClassName();
            String className = fullName.substring(fullName.lastIndexOf(".") + 1);
            tag = "[" + className + "." + ste.getMethodName() + ":Line No." + ste.getLineNumber() + " " + tag + "]";
            android.util.Log.i(tag, content);
            // Log.i(tag, content);
            // LogUtil.writeExceptionLog(tag+"+++++"+content);
        }
    }

    public static void info(String content) {
        if (true) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
            String fullName = ste.getClassName();
            String className = fullName.substring(fullName.lastIndexOf(".") + 1);
            String tag = "[" + className + "." + ste.getMethodName() + ":Line No." + ste.getLineNumber() + "]";
            android.util.Log.i(tag, content);
            // Log.i(tag, content);
            // LogUtil.writeExceptionLog(tag+"+++++"+content);
        }
    }

    public static void error(String content) {
        if (true) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
            String fullName = ste.getClassName();
            String className = fullName.substring(fullName.lastIndexOf(".") + 1);
            String tag = "[" + className + "." + ste.getMethodName() + ":Line No." + ste.getLineNumber() + "]";
            android.util.Log.e(tag, content);
            // Log.i(tag, content);
            // LogUtil.writeExceptionLog(tag+"+++++"+content);
        }
    }


    private static synchronized void write(String info, String level) {
        File logPath = new File(Settings.get().getLogPath());
        String temp = "";

        if (!logPath.exists()) {
            logPath.mkdir();
        }

        File log = new File(logPath.toString().concat(File.separator).concat(TimeUtil.getCurrentDay()).concat(".log"));
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (log.exists()) {
            FileWriter fw = null;
            try {
                StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
                fw = new FileWriter(log, true);// false
                StringBuilder sb = new StringBuilder();
                sb.append("\n");
                sb.append("[");
                sb.append(TimeUtil.getCurrentTime());
                sb.append(" ");
                sb.append(level);
                if (true) {
                    sb.append(" ");
                    sb.append(ste.getClassName());
                    sb.append(".");
                    sb.append(ste.getMethodName());
                    sb.append(":");
                    sb.append(ste.getLineNumber());
                }
                sb.append("]\n");
                sb.append(info);
                sb.append("\n");

                temp = sb.toString();

                fw.write(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (true) {
            if("ERROR".equals(level)) {
                android.util.Log.e("FILE-" + level, temp);
            } else {
                android.util.Log.i("FILE-" + level, temp);
            }
        }
    }

    public static void wInfo(String info) {
        write(info, "INFO");
    }


    public static void wError(String info) {
        //write(info, "ERROR");
    }


    public static void wError(Throwable e) {
        write(StringUtil.exception2Str(e), "ERROR");
    }
    
    

    public static void aInfo(String assist, String info) {
        //write(info, "INFO " + assist);
    }
    
    public static void aError(String assist, String info) {
        //write(info, "ERROR " + assist);
    }
    
    public static void aError(String assist, Throwable e) {
        //write(StringUtil.exception2Str(e), "ERROR " + assist);
    }
}
