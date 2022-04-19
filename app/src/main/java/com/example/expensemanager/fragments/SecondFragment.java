package com.example.expensemanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

  private FragmentSecondBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentSecondBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.edittextAddTransactionAmount.requestFocus();

    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

    imm.showSoftInput(binding.edittextAddTransactionAmount, 0);


    binding.textviewAddTransactionAmount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        binding.edittextAddTransactionAmount.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        imm.showSoftInput(binding.edittextAddTransactionAmount, 0);
      }
    });


    binding.linearlayoutAddTransactionAmount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        binding.edittextAddTransactionAmount.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.edittextAddTransactionAmount, 0);
      }
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}