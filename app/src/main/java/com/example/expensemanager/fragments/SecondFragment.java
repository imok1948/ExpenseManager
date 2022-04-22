package com.example.expensemanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentSecondBinding;
import com.example.expensemanager.utils.CategoryType;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class SecondFragment extends Fragment {

  private static final int CATEGORIES_PER_ROW = 4;
  private FragmentSecondBinding binding;
  private View rootView;
  private Calendar defaultCalendar;
  private View viewOfCalendarDialog;
  private AlertDialog calendarDialog, categoryDialog;
  private View viewOfCategoryDialog;
  private LinearLayout viewOfAddExpenseDialogRow;
  private View.OnClickListener dialogButtonClickListener;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSecondBinding.inflate(inflater, container, false);
    rootView = binding.getRoot();
    return rootView;
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    assignments();
    setupListeners();
    initiate();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private void initiate() {

    initDialogs();

    //Now setting up focus and keyboard for the amount view
    binding.edittextAddTransactionAmount.requestFocus();
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(binding.edittextAddTransactionAmount, 0);
  }

  private void initDialogs() {
    createDateDialog();
    createCategoryDialog();
  }

  //Done and working fine
  private void createCategoryDialog() {
    AlertDialog.Builder categoryDialogBuilder = new AlertDialog.Builder(getActivity());

    //Find category box
    LinearLayout linearLayoutBox = viewOfCategoryDialog.findViewById(R.id.dialog_category_picker_linear_layout_box);
    int totalCategories = 14;
    List<CategoryType> categories = getDummyCategories(totalCategories);
    int totalRows = (int) Math.ceil(totalCategories / (float) CATEGORIES_PER_ROW);
    for (int i = 0; i < totalRows; i++) {
      viewOfAddExpenseDialogRow = getCategoryDialogRow(R.layout.add_activity_dialog_row);
      for (int j = 0; j < CATEGORIES_PER_ROW && categories.size() > 0; j++) {
        viewOfAddExpenseDialogRow.addView(getLayoutForCategoryDialog(R.layout.add_expense_dialog_category, categories.get(0), dialogButtonClickListener, viewOfAddExpenseDialogRow));
        categories.remove(0);
      }
      linearLayoutBox.addView(viewOfAddExpenseDialogRow);
    }
    categoryDialogBuilder.setView(viewOfCategoryDialog);
    categoryDialog = categoryDialogBuilder.create();
  }

  private void createDateDialog() {
    AlertDialog.Builder calendarBuilder = new AlertDialog.Builder(getActivity());

    DatePicker datePicker = viewOfCalendarDialog.findViewById(R.id.datepicker_add_transactions_calendar);
    datePicker.init(defaultCalendar.get(Calendar.YEAR), defaultCalendar.get(Calendar.MONTH), defaultCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker datePicker1, int year, int month, int dayOfMonth) {
        setExpenseDate(year, month, dayOfMonth);
        calendarDialog.dismiss();
      }
    });

    calendarBuilder.setView(viewOfCalendarDialog);
    calendarDialog = calendarBuilder.create();
  }

  private void setExpenseDate(int year, int month, int dayOfMonth) {
    //Month is 0 based
    String[] monthStrings = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    binding.textviewAddTransactionDate.setText(dayOfMonth + " " + monthStrings[month] + " " + year + ",");
  }

  private void assignments() {
    //Initialize with their respective views
    viewOfCalendarDialog = getLayoutInflater().inflate(R.layout.dialog_select_date, null);
    viewOfCategoryDialog = getLayoutInflater().inflate(R.layout.dialog_category_picker, null);
    defaultCalendar = Calendar.getInstance();
    defaultCalendar.setTimeInMillis(System.currentTimeMillis());

  }

  private LinearLayout getLayoutForCategoryDialog(int id, Object tag, View.OnClickListener onClickListener, ViewGroup parent) {
    CategoryType categoryType = (CategoryType) tag;
    LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(id, parent, false);
    linearLayout.setTag(tag);
    linearLayout.setOnClickListener(onClickListener);
    ((ImageView) linearLayout.findViewById(R.id.imageview_add_expense_dialog_category)).setImageResource(categoryType.getImage());
    ((TextView) linearLayout.findViewById(R.id.textview_add_expense_dialog_category)).setText(categoryType.getName());
    return linearLayout;
  }

  private LinearLayout getCategoryDialogRow(int viewId) {
    return (LinearLayout) getLayoutInflater().inflate(R.layout.add_activity_dialog_row, null);
  }

  private void setupListeners() {
    dialogButtonClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getContext(), "Selected category : " + view.getTag(), Toast.LENGTH_SHORT).show();
      }
    };


    binding.linearlayoutAddTransactionCategory.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        categoryDialog.show();
      }
    });


    //ToDo : Currently the cursor on clicking the Layout after edittext doesn't bring it to end of edittext, do it
    binding.linearlayoutAddTransactionAmount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        binding.edittextAddTransactionAmount.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.edittextAddTransactionAmount, 0);
      }
    });


    binding.linearlayoutAddTransactionDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        calendarDialog.show();
      }
    });


    binding.linearlayoutAddTransactionPayment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(rootView, "I will show you available accounts", Snackbar.LENGTH_SHORT).show();
      }
    });
  }

  private List<CategoryType> getDummyCategories(int n) {
    String[] categoryNames = new String[]{"Foods", "Cloths", "Health", "Electronics", "Miscllenciuos", "Study", "Work", "Others"};
    String[] categoryIds = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
    int[] categoryImages = new int[]{R.drawable.foods, R.drawable.category_clothes, R.drawable.health, R.drawable.category_electronics, R.drawable.misclencious, R.drawable.study, R.drawable.category_work, R.drawable.category_others};


    List<CategoryType> categoryTypeList = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      categoryTypeList.add(new CategoryType(categoryIds[i % categoryIds.length], categoryNames[i % categoryNames.length], categoryImages[i % categoryImages.length]));
    }
    return categoryTypeList;
  }
}
















