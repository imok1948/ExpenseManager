package com.example.expensemanager.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.models.TransactionsModel;
import com.example.expensemanager.adapters.AdapterTransactions;
import com.example.expensemanager.databinding.FragmentFirstBinding;
import com.example.expensemanager.utils.Utilities;

import java.util.LinkedList;
import java.util.List;

public class FirstFragment extends Fragment {

  private final String TAG = this.getClass().toString();
  private FragmentFirstBinding binding;

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
//    Toast.makeText(getContext(), "Destroying FirstFragment", Toast.LENGTH_SHORT).show();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentFirstBinding.inflate(inflater, container, false);
    init();
    setupListeners();
//    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_ShowTransactionDetailsFragment);
    return binding.getRoot();
  }

  private void init() {
    initiateTransactionsRecyclerView();
  }

  private void initiateTransactionsRecyclerView() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
    binding.recyclerViewTransactions.setLayoutManager(linearLayoutManager);
    List<TransactionsModel> transationList = Utilities.getDummyData(40);
    AdapterTransactions adapterTransactions = new AdapterTransactions(transationList);
    binding.recyclerViewTransactions.setAdapter(adapterTransactions);
    adapterTransactions.notifyDataSetChanged();
  }

  private void setupListeners() {
    binding.buttonAddExpense.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
      }
    });
    binding.buttonAddIncome.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_ShowTransactionDetailsFragment);
      }
    });
  }

}