/**
 * 
 */
package com.cabi.driver.utils;

import android.graphics.Bitmap;

import com.cabi.driver.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * interface is for download the image from server.
 */
public interface ImageDownloader
{
	DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loadingimage).showImageForEmptyUri(R.drawable.noimage).showImageOnFail(R.drawable.noimage).cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).cacheOnDisc(true).build();
	DisplayImageOptions roundedImageoptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loadingimage).showImageForEmptyUri(R.drawable.noimage).showImageOnFail(R.drawable.noimage).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300)).cacheOnDisc(true).cacheInMemory(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(8)).build();
	// Image Loader
	ImageLoader imageLoader = ImageLoader.getInstance();
}
