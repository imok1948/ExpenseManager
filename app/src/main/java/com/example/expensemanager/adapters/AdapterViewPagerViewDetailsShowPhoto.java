package com.example.expensemanager.adapters;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.expensemanager.R;

import java.util.List;
import java.util.Objects;

public class AdapterViewPagerViewDetailsShowPhoto extends PagerAdapter {
  private List<Uri> imageList;
  private LayoutInflater layoutInflater;

  public AdapterViewPagerViewDetailsShowPhoto(Context context, List<Uri> imageList) {
    this.imageList = imageList;
    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return imageList.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View viewOfItemShowPhotos = layoutInflater.inflate(R.layout.item_view_view_details_show_photo, container, false);
    ((ImageView) viewOfItemShowPhotos.findViewById(R.id.imageview_view_details_show_photo)).setImageURI(imageList.get(position));
    ((TextView) viewOfItemShowPhotos.findViewById(R.id.textview_count_show_photo_view_details)).setText((position + 1) + "/" + imageList.size());

    Objects.requireNonNull(container).addView(viewOfItemShowPhotos);
    return viewOfItemShowPhotos;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }
}
