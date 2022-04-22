package com.example.expensemanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class SecondFragment extends Fragment {

  private static final int CATEGORIES_PER_ROW = 4;
  private FragmentSecondBinding binding;
  private Calendar defaultCalendar;
  private View viewCalendarDialog;
  private AlertDialog calendarDialog, categoryDialog;
  private View viewCategoryDialog;
  private LinearLayout viewAddActivityDialogRow;
  private LinearLayout viewAddExpenseDialogCategory;


  private View.OnClickListener dialogButtonClickListener;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSecondBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }


  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initiate();
    setupListeners();

  }


  private void initiate() {

    dialogButtonClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getContext(), "Selected : " + view.getTag(), Toast.LENGTH_SHORT).show();
      }
    };


    defaultCalendar = Calendar.getInstance();
    binding.edittextAddTransactionAmount.requestFocus();
    setupListeners();
    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(binding.edittextAddTransactionAmount, 0);
    initDialogs();
  }


  private LinearLayout getInflatedLinearLayout(int id, Object tag, View.OnClickListener onClickListener, ViewGroup parent) {
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


  private void initDialogs() {
    //Initialize with their respective views
    viewCalendarDialog = getLayoutInflater().inflate(R.layout.dialog_select_date, null);
    viewCategoryDialog = getLayoutInflater().inflate(R.layout.dialog_category_picker, null);
    viewAddActivityDialogRow = null;


    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Select Date");

    DatePicker datePicker = viewCalendarDialog.findViewById(R.id.datepicker_add_transactions_calendar);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());

    datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker datePicker1, int year, int month, int dayOfMonth) {
        Toast.makeText(getContext(), "Date selected " + datePicker1.getDayOfMonth(), Toast.LENGTH_SHORT).show();
      }
    });
    builder.setView(viewCalendarDialog);
    calendarDialog = builder.create();


    AlertDialog.Builder categoryDialogBuilder = new AlertDialog.Builder(getActivity());

    //Find box and add
    LinearLayout linearLayoutBox = viewCategoryDialog.findViewById(R.id.dialog_category_picker_linear_layout_box);


    int totalCategories = 14;
    List<CategoryType> categories = getDummyCategories(totalCategories);

    int totalRows = (int) Math.ceil(totalCategories / (float) CATEGORIES_PER_ROW);

    for (int i = 0; i < totalRows; i++) {
      viewAddActivityDialogRow = getCategoryDialogRow(R.layout.add_activity_dialog_row);
      for (int j = 0; j < CATEGORIES_PER_ROW && categories.size() > 0; j++) {
        viewAddActivityDialogRow.addView(getInflatedLinearLayout(R.layout.add_expense_dialog_category, categories.get(0), dialogButtonClickListener, viewAddActivityDialogRow));
        categories.remove(0);
      }
      linearLayoutBox.addView(viewAddActivityDialogRow);
    }

    categoryDialogBuilder.setView(viewCategoryDialog);
    categoryDialog = categoryDialogBuilder.create();
  }

  private void setupListeners() {
    binding.linearlayoutAddTransactionCategory.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        categoryDialog.show();
      }
    });

    //ToDo : Currently the cursor on clicking the Layout after edittext doesn't bring it to end of edittext, do it
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
    binding.buttonAddTransactionAmountDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        calendarDialog.show();
      }
    });
    binding.linearlayoutAddTransactionPayment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getContext(), "Hi, I am linear layout", Toast.LENGTH_SHORT).show();
      }
    });

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
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
















