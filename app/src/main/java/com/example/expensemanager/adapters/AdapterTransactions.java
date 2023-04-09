package com.example.expensemanager.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.models.TransactionsModel;

import java.util.List;

public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.TransactionsHolder> {

  private static final int TRANSACTIONS_ENDBLOCK = 0;
  private static final int TRANSACTIONS_TRANSACTION = 1;
  private static final int TRANSACTIONS_DATE = 2;

  private final String TAG = this.getClass().getSimpleName();
  private List<TransactionsModel> transactions;

  public AdapterTransactions(List<TransactionsModel> transactions) {
    this.transactions = transactions;
  }

  public AdapterTransactions() {
  }

  public List<TransactionsModel> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<TransactionsModel> transactions) {
    this.transactions = transactions;
  }

  @NonNull
  @Override
  public TransactionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    TransactionsHolder holder;

    if (viewType == TRANSACTIONS_DATE) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_transactions_date, parent, false);
      holder = new TransactionsHolder(view, viewType);
    }
    else if (viewType == TRANSACTIONS_TRANSACTION) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_transactions_detail, parent, false);
      holder = new TransactionsHolder(view, viewType);
    }
    else {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_transactions_endblock, parent, false);
      holder = new TransactionsHolder(view, viewType);
    }
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull TransactionsHolder holder, int position) {

    if (transactions.get(position).isEndBlock()) {

    }
    else {
      if (transactions.get(position).isDate()) {
        String dateDate, dateMonth, dateYear, dateDay;
        dateDate = transactions.get(position).getDateDate();
        dateMonth = transactions.get(position).getDateMonth();
        dateYear = transactions.get(position).getDateYear();
        dateDay = transactions.get(position).getDateDay();
        holder.setDate(dateDate, dateMonth, dateYear, dateDay);

      }
      else {
        int image = transactions.get(position).getTransactionImage();
        String description = transactions.get(position).getTransactionDescription();
        String category = transactions.get(position).getTransactionCategory();
        String amount = transactions.get(position).getTransactionAmount();
        String date = transactions.get(position).getTransactionDate();

        holder.setTransaction(image, description, category, amount, date);
      }
    }
  }

  @Override
  public int getItemCount() {
    if (transactions != null) {
      return transactions.size();
    }
    else {
      return 0;
    }
  }

  @Override
  public int getItemViewType(int position) {

    if (transactions.get(position).isEndBlock()) {
      return TRANSACTIONS_ENDBLOCK;
    }
    else if (transactions.get(position).isDate()) {
      return TRANSACTIONS_DATE;
    }
    else {
      return TRANSACTIONS_TRANSACTION;
    }
  }

  public class TransactionsHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView description, category, amount, date;
    private TextView dateDate, dateDay;

    public TransactionsHolder(@NonNull View itemView, int viewType) {
      super(itemView);

      if (viewType == TRANSACTIONS_DATE) {
        dateDate = itemView.findViewById(R.id.transactions_date_date);
        dateDay = itemView.findViewById(R.id.transactions_date_day);
      }
      else if (viewType == TRANSACTIONS_TRANSACTION) {
        image = itemView.findViewById(R.id.imageview_transactions_image);
        description = itemView.findViewById(R.id.textview_transactions_description);
        category = itemView.findViewById(R.id.textview_transactions_category);
        amount = itemView.findViewById(R.id.textview_transactions_amount);
        date = itemView.findViewById(R.id.textview_transactions_date);
      }
      else {
        itemView.findViewById(R.id.transactions_end_of_block).setVisibility(View.VISIBLE);
      }
    }


    public void setTransaction(int image, String description, String category, String amount, String date) {
      this.image.setImageResource(image);
      this.description.setText(description);
      this.category.setText(category);
      this.amount.setText(amount);
      this.date.setText(date);
    }

    public void setDate(String dateDate, String dateMonth, String dateYear, String dateDay) {
      this.dateDate.setText(dateDate + " " + dateMonth + " " + dateYear);
      this.dateDay.setText(dateDay);
    }
  }
}
