package com.example.expensemanager.utils;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.expensemanager.R;
import com.example.expensemanager.models.TransactionsModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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


  public static List<TransactionsModel> getDummyData(int n) {
    int images[] = new int[]{R.drawable.foods, R.drawable.gifts, R.drawable.health, R.drawable.misclencious, R.drawable.study};
    String descriptions[] = new String[]{"Lunch at Tejas", "Dinner @ Ruchi Sagar", "Fruits", "Medicines of hair, by Dr Narashimhalu Chennai Tamil Nadu, India", "Flight to Bhopal", "Cloths", "Almonds"};
    String categories[] = new String[]{"Food", "Health", "Cloths", "Electronic", "Others", "Study"};
    String amounts[] = new String[]{"1233", "4378.47", "398", "978364", "34986", "4879", "43986", "4986534", "398565634"};
    String dates[] = new String[]{"22 Apr 2022", "17 Jun 2021", "8 May 2018", "19 Dec 2020", "3 Sep 1995"};


    int transactionsCount = 0;
    List<TransactionsModel> list = new LinkedList<>();


    for (; n != 0; n--, transactionsCount--) {

      if (transactionsCount != 0) {
        //Date only
        int image = images[getRandomNumber(0, images.length)];
        String description = descriptions[getRandomNumber(0, descriptions.length)];
        String category = categories[getRandomNumber(0, categories.length)];
        String amount = amounts[getRandomNumber(0, amounts.length)];
        String date = dates[getRandomNumber(0, dates.length)];
        list.add(new TransactionsModel(false, image, description, category, amount, date));

      } else {
        if (list.size() != 0) {
          //Not the first transaction, but end of block
          list.add(new TransactionsModel(true));
        }

        //First transaction and first transaction after endblock
        transactionsCount = getRandomNumber(2, 10);

        String dateDate, dateMonth, dateYear, dateDay;
        dateDate = "" + getRandomNumber(1, 31);
        dateMonth = new String[]{"January", "Feb", "3", "04", "May", "June", "Jul", "Aug", "September", "Oct", "Nov", "December"}[getRandomNumber(0, 12)];
        dateYear = "" + getRandomNumber(1000, 2050);
        dateDay = new String[]{"Sunday", "Monday", "Tue", "Wednesday", "Thursday", "Fri", "Sat"}[getRandomNumber(0, 7)];

        list.add(new TransactionsModel(true, dateDate, dateMonth, dateYear, dateDay));
      }
    }

    if (list.size() != 0 && list.get(list.size() - 1).isDate()) {
      list.remove(list.size() - 1);
    }
    Log.v(TAG, "" + list.get(list.size() - 1));
    return list;
  }

  public static int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  public static List<Account> getDummyAccounts(int n) {
    String accountNames[] = new String[]{"HDFC 6021", "SBI 5500", "Kotak 22231", "Airtel", "PayTm"};
    String accountIds[] = new String[]{"hdfc_6021", "sbi_5500", "kotak_22231", "airtel", "paytm"};

    List<Account> accounts = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      int k = Utilities.getRandomNumber(0, accountNames.length);
      accounts.add(new Account(accountNames[k], accountIds[k]));
    }
    return accounts;
  }

  public static List<CategoryType> getDummyCategories(int n) {
    String[] categoryNames = new String[]{"Foods", "Cloths", "Health", "Electronics", "Miscllenciuos", "Study", "Work", "Others"};
    String[] categoryIds = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
    int[] categoryImages = new int[]{R.drawable.foods, R.drawable.category_clothes, R.drawable.health, R.drawable.category_electronics, R.drawable.misclencious, R.drawable.study, R.drawable.category_work, R.drawable.category_others};


    List<CategoryType> categoryTypeList = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      categoryTypeList.add(new CategoryType(categoryIds[i % categoryIds.length], categoryNames[i % categoryNames.length], categoryImages[i % categoryImages.length]));
    }
    return categoryTypeList;
  }


  public static void tempFunction() {
  }


  public static List<Uri> getUriFromFiles(List<File> files) {
    List<Uri> uris = new LinkedList<>();
    for (File file : files) {
      uris.add(Uri.fromFile(file));
    }
    return uris;
  }


  public static List<Uri> getRandomImageUri(Context context, int n) {
    List<Uri> list = new LinkedList<>();
    File directory = context.getExternalFilesDir("/tempPhotos/");
    File[] files = directory.listFiles();

    if (files.length == 0) {
      return list;
    }

    for (int i = 0; i < n; i++) {
      list.add(Uri.fromFile(files[i % files.length]));
    }

    return list;
  }
}
