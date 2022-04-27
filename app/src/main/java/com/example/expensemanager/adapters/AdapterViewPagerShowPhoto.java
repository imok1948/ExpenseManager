package com.example.expensemanager.adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.expensemanager.R;
import com.example.expensemanager.utils.ExpensePhoto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class AdapterViewPagerShowPhoto extends PagerAdapter {

  private Context context;
  private LayoutInflater layoutInflater;
  private HashMap<String, ExpensePhoto> imageHashMap;
  private LinkedList<String> imageListKeys;
  private AlertDialog dialog;

  public AdapterViewPagerShowPhoto(Context context, HashMap<String, ExpensePhoto> imageHashMap, AlertDialog dialog) {
    this.context = context;
    this.imageHashMap = imageHashMap;
    this.dialog = dialog;
    imageListKeys = new LinkedList<>();
    for (String key : imageHashMap.keySet()) {
      imageListKeys.add(key);
    }
    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public AdapterViewPagerShowPhoto(Context context, HashMap<String, ExpensePhoto> imageHashMap) {
    this.context = context;
    this.imageHashMap = imageHashMap;
    dialog = null;
    imageListKeys = new LinkedList<>();
    for (String key : imageHashMap.keySet()) {
      imageListKeys.add(key);
    }
    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }



  @Override
  public int getCount() {
    return imageListKeys.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View viewOfItemShowPhotos = layoutInflater.inflate(R.layout.item_view_show_photo, container, false);
    ((ImageView) viewOfItemShowPhotos.findViewById(R.id.imageview_dialog_show_photo)).setImageURI(imageHashMap.get(imageListKeys.get(position)).getImageUri());
    ((TextView) viewOfItemShowPhotos.findViewById(R.id.textview_count_show_photo_dialog)).setText((position + 1) + "/" + imageHashMap.size());
    ImageView deleteButton = viewOfItemShowPhotos.findViewById(R.id.delete_button_show_photo_dialog);
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        imageHashMap.remove(imageListKeys.remove(position));
        notifyDataSetChanged();
        Toast.makeText(context, "Image deleted", Toast.LENGTH_SHORT).show();

        if (imageHashMap.size() == 0 && dialog != null) {
          dialog.dismiss();
        }
      }
    });
    Objects.requireNonNull(container).addView(viewOfItemShowPhotos);
    return viewOfItemShowPhotos;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }

  @Override
  public int getItemPosition(@NonNull Object object) {
    return POSITION_NONE;
  }
}





