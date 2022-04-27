package com.example.expensemanager.utils;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class Utilities {

  private static final String DUPLICATE_IMAGE_PREFIX = "expenseManager_";
  private static final String RESIZED_IMAGE_PREFIX = "expenseManager_";
  private static final String IMAGE_SUFFIX = ".jpg";
  private static final String TAG = Utilities.class.toString();
  private static final int WIDTH_OF_IMAGEVIEW = 48;

  public static Bitmap getBitmapFromUri(Context context, Uri uri) {
    try {
      InputStream inputStream = context.getContentResolver().openInputStream(uri);
      if (inputStream == null) {
        return null;
      } else {
        return BitmapFactory.decodeStream(inputStream);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getDateInDDMMYYYYHHMMSS() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.DATE) + "" + (calendar.get(Calendar.MONTH) + 1) + "" + calendar.get(Calendar.YEAR) + "x" + calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND);
  }

  public static Uri getResizedImageUri(Context context, Uri originalFileUri, File directory) {

    Uri resizedImageUri = null;
    try {
      Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), originalFileUri);
      float heightMultiplier = 0;
      if (originalBitmap.getWidth() != 0 && originalBitmap.getHeight() != 0) {
        heightMultiplier = originalBitmap.getHeight() / (float) originalBitmap.getWidth();
      }
      Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, WIDTH_OF_IMAGEVIEW, (int) (WIDTH_OF_IMAGEVIEW * heightMultiplier), true);

      File resizedImageFile = File.createTempFile(RESIZED_IMAGE_PREFIX + "_" + getDateInDDMMYYYYHHMMSS() + "_", IMAGE_SUFFIX, directory);
      FileOutputStream resizedImageStream = new FileOutputStream(resizedImageFile);
      resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, resizedImageStream);
      resizedImageStream.flush();
      resizedImageStream.close();
      resizedImageUri = Uri.parse(resizedImageFile.toString());
    } catch (IOException e) {
      Log.e(TAG, "Error resizing" + e);
      e.printStackTrace();
    } finally {
      return resizedImageUri;
    }
  }

  public static float getPixelsFromDp(final Context context, final float dp) {
    return dp * context.getResources().getDisplayMetrics().density;
  }

  public static float getPixelsFromDp(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }


}
