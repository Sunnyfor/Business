package com.zuoyu.business.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 * Function：图片加载框架
 *
 * Created by JoannChen on 2017/4/20 14:18
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class ImageUtil {

    /**
     * 加载图片
     *
     * @param context   上下文对象
     * @param imageView 组件
     * @param url       图片地址
     */
    public static void load(Context context, ImageView imageView, String url) {
        if (context == null) {
            return;
        }
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }


    /**
     * 加载图片
     *
     * @param context   上下文对象
     * @param imageView 组件
     * @param url       图片地址
     * @param res       默认图片
     */
    public static void load(Context context, ImageView imageView, String url, int res) {
        if (context == null) {
            return;
        }
        Glide.with(context)
                .load(url)
                .placeholder(res)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载图片，实现圆角
     *
     * @param context   上下文对象
     * @param imageView 组件
     * @param url       图片地址
     */
    public static void loadRound(final Context context, final ImageView imageView, String url) {
        if (context == null) {
            return;
        }
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        //要实现圆角，只需要加上这句
                        RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        round.setCircular(true);
                        round.setCornerRadius(100L);
                        imageView.setImageDrawable(round);
                    }
                });
    }


    /**
     * 根据图片地址转换为base64编码字符串
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        return Base64.encode(data);
    }

    /**
     * 图片压缩
     *
     * @param file    原始图片
     * @param newPath 压缩后存储路径
     * @return
     */
    public static String saveBitmapToFile(File file, String newPath) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();


            File newFile = new File(newPath);

            FileOutputStream outputStream = new FileOutputStream(newFile);

            //choose another format if PNG doesn't suit you

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            String filepath = newFile.getAbsolutePath();
            LogUtil.e("getAbsolutePath" + newFile.getAbsolutePath());

            return filepath;
        } catch (Exception e) {
            return null;
        }
    }

}
