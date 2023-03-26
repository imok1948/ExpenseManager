package com.example.expensemanager.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.AdapterViewPagerViewDetailsShowPhoto;
import com.example.expensemanager.databinding.FragmentShowTransactionDetailsBinding;
import com.example.expensemanager.utils.Utilities;

import java.util.List;

public class ShowTransactionDetailsFragment extends Fragment {
  private final String TAG = this.getClass().toString();
  private FragmentShowTransactionDetailsBinding binding;

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentShowTransactionDetailsBinding.inflate(inflater, container, false);

    List<Uri> randomImagesSrc = Utilities.getRandomImageUri(getContext(), 4);
    ViewPager viewPager = binding.viewpagerShowPhotoViewDetails;
    AdapterViewPagerViewDetailsShowPhoto adapterViewPagerViewDetailsShowPhoto = new AdapterViewPagerViewDetailsShowPhoto(getContext(), randomImagesSrc);
    viewPager.setAdapter(adapterViewPagerViewDetailsShowPhoto);

    return binding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}