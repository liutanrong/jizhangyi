package com.liu.Account.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by deonte on 16-1-27.
 */
public class BitmapUtil {
    /**
     * 根据路径获得图片，返回bitmap用于显示
     * @param filePath 路径
     * @return Bitmap 获得的bitmap对象
     * 注意添加权限
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     */
    public static Bitmap getBitmapFromFile(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    /***
     * 将所给bitmap压缩至最大233kb保存至指定路径
     * @param image bitmap对象
     * @param outPath 保存路径
     * @param maxsize 保存的最大大小（kb）
     * */
    public static void saveImage(Bitmap image, String outPath,int maxsize) throws IOException {
        if(image==null||outPath==null||maxsize<0)
            return ;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while ( os.toByteArray().length / 1024 > maxsize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
    }
}
