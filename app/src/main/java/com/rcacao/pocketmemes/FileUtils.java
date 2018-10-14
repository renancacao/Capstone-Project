package com.rcacao.pocketmemes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileUtils {

    private static final String PNG_FILE = ".png";
    private static final String POCKET_MEMES_SUFIX = "pocketmemes_";

    public static String getFileNameWithPath(String fileName) {
        return Constants.IMAGE_PATH + POCKET_MEMES_SUFIX + String.valueOf(fileName) + PNG_FILE;
    }

    private static boolean fileAlredyExists(String filename) {
        File f = new File(getFileNameWithPath(filename));
        return f.exists();
    }

    public static String getFileName() {
        String fileName = getRandomChar();
        while (fileAlredyExists(fileName)) {
            fileName += getRandomChar();
        }
        return fileName;
    }

    private static String getRandomChar() {
        Random r = new Random();
        int ascii = r.nextInt(90 - 65 + 1) + 65;
        return String.valueOf((char) ascii);
    }

    public static boolean saveBitmap(String file, Bitmap image, Context ctx) {
        String filename = getFileNameWithPath(file);
        boolean result = true;
        FileOutputStream out = null;
        try {
            File dir = new File(Constants.IMAGE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File imageFile = new File(filename);
            if (imageFile.exists()) {
                imageFile.delete();
            }

            imageFile.createNewFile();
            out = new FileOutputStream(filename);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            updateImages(imageFile, ctx);

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void updateImages(File file, Context ctx) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    public static boolean hasStoragePermission(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }
}
