package com.example.expensemanager.interfaces;
import com.example.expensemanager.models.TransactionsModel;
import com.example.expensemanager.utils.ExpenseModel;

import java.util.List;

public interface FirebaseCallback {
  void onExpensesReceived(List<ExpenseModel> expenseModelList);
}
