package com.example.expensemanager.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.models.TransactionsModel;
import com.example.expensemanager.adapters.AdapterTransactions;
import com.example.expensemanager.databinding.FragmentFirstBinding;

import java.util.LinkedList;
import java.util.List;

public class FirstFragment extends Fragment {

  private final String TAG = this.getClass().toString();
  private FragmentFirstBinding binding;

  private RecyclerView recyclerView;
  private LinearLayoutManager linearLayoutManager;
  List<TransactionsModel> transationList;
  AdapterTransactions adapterTransactions;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentFirstBinding.inflate(inflater, container, false);
    recyclerView = binding.recyclerViewTransactions;
    linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
    recyclerView.setLayoutManager(linearLayoutManager);

    transationList = getDummyData(40);

    adapterTransactions = new AdapterTransactions(transationList);

    recyclerView.setAdapter(adapterTransactions);
    adapterTransactions.notifyDataSetChanged();

    binding.buttonAddExpense.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
      }
    });
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private List<TransactionsModel> getDummyData(int n) {
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

  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }


}