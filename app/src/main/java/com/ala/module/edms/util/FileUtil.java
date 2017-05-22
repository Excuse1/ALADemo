package com.ala.module.edms.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {

    public static boolean mkdir(String path) {
        File dir = new File(path);
        if (!dir.exists())
            return dir.mkdirs();
        return true;
    }

    public static void rmDir(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
            } else {
                for (File f : childFile) {
                    rmfile(f);
                }
            }
        }

    }

    public static void rmfile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            rmfile(file);
        }
    }

    private static void rmfile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                rmfile(f);
            }
            file.delete();
        }
    }

    public static void copyAssetsDir(Context context, String ast, String desPath) {
        try {
            String subs[] = context.getAssets().list(ast);
            if (subs != null && subs.length > 0) {
                mkdir(desPath + File.separator + ast);
                for (String sub : subs) {
                    copyAssetsDir(context, ast + File.separator + sub, desPath);
                }
            } else {
                boolean isFile = true;
                try {
                    InputStream input = context.getAssets().open(ast);
                    input.close();
                } catch (FileNotFoundException e) {
                    isFile = false;
                }
                if (isFile) {
                    File file = new File(desPath + File.separator + ast);
                    if (!file.exists()) {
                        copyAssets(context, ast, desPath + File.separator + ast);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean copyAssets(Context context, String ast, String des) {
        InputStream input = null;
        OutputStream out = null;

        try {
            AssetManager manager = context.getAssets();
            input = manager.open(ast);
            out = new FileOutputStream(des);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            input.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static String readAssetsFileStr(Context context, String fileName) {
        StringBuffer sb = new StringBuffer();
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);

            while (true) {
                String line = bufReader.readLine();
                if (line == null)
                    break;

                sb.append(line);
                sb.append("\n");
            }
            inputReader.close();
            bufReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }
    }


    public static void writeFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);// true表示添加，false表示覆盖
            StringBuilder sb = new StringBuilder();
            sb.append(content);
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        BufferedReader bufReader = new BufferedReader(new FileReader(file));
        while (true) {
            String line = bufReader.readLine();
            if (line == null)
                break;

            sb.append(line);
            sb.append("\n");
        }
        bufReader.close();
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }
    }


    public static boolean isFileExistsFromAssets(Context context, String filename) {
        String[] names = null;
        AssetManager manager = context.getAssets();
        try {
            names = manager.list("");
            for (int i = 0; i < names.length; i++) {
                if (filename.equals(names[i]))
                    break;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
