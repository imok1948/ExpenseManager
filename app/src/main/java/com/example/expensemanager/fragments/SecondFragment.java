package com.example.expensemanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.AdapterViewPagerShowPhoto;
import com.example.expensemanager.databinding.FragmentSecondBinding;
import com.example.expensemanager.utils.Account;
import com.example.expensemanager.utils.CategoryType;
import com.example.expensemanager.utils.ExpenseModel;
import com.example.expensemanager.utils.ExpensePhoto;
import com.example.expensemanager.utils.Utilities;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class SecondFragment extends Fragment {

  private static final int CATEGORIES_PER_ROW = 4;
  private static final String TAG = Fragment.class.toString();
  private static final int SELECT_IMAGE_FROM_GALLERY_CODE = 101;
  private static final int EXPENSE_IMAGES_TO_BE_SHOWN_IN_LINEAR_LAYOUT = 4;

  private int currentPhotosOnScreen = 0;
  private FragmentSecondBinding binding;
  private View rootView;
  private Calendar defaultCalendar;
  private View viewOfCalendarDialog, viewOfCategoryDialog, viewOfPaymentDialog;
  private AlertDialog calendarDialog, categoryDialog, paymentModeDialog, showPhotoDialog;
  private LinearLayout viewOfAddExpenseDialogRow;

  private ExpenseModel expenseModel;


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
    updateViewFromModel();
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
    createPaymentModeDialog();
//    createDhowPhotoDialog();
  }

  //Done and working fine

  private void assignments() {
    defaultCalendar = Calendar.getInstance();
    defaultCalendar.setTimeInMillis(System.currentTimeMillis());
    expenseModel = new ExpenseModel(198F, "", new CategoryType("1", "Foods", R.drawable.foods), defaultCalendar, new Account("HDFC 6022", "hdfc_6022"), new LinkedHashMap<>());

    //Initialize with their respective views
    viewOfCalendarDialog = getLayoutInflater().inflate(R.layout.dialog_select_date, null);
    viewOfCategoryDialog = getLayoutInflater().inflate(R.layout.dialog_category_picker, null);
    viewOfPaymentDialog = getLayoutInflater().inflate(R.layout.dialog_payment_mode, null);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == SELECT_IMAGE_FROM_GALLERY_CODE) {
      Uri uri = data.getData();
      ExpensePhoto expensePhoto = new ExpensePhoto(uri, uri, uri.toString());
      binding.linearlayoutAddTransactionPhoto.addView(getImageViewOfTransactionPhoto(expensePhoto, binding.linearlayoutAddTransactionPhoto));

      if (expenseModel.getExpensePhotosHashMap().size() == 0) {
        binding.textviewAddTransactionPhotoHint.setVisibility(View.VISIBLE);
      } else {
        binding.textviewAddTransactionPhotoHint.setVisibility(View.GONE);
      }

    }
  }


  private ImageView getImageViewOfTransactionPhoto(ExpensePhoto expensePhoto, ViewGroup linearlayoutAddTransactionPhoto) {

    expensePhoto.setThumbnailURI(Utilities.getResizedImageUri(getContext(), expensePhoto.getImageUri(), getActivity().getExternalCacheDir()));
    ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.element_imageview_add_transaction_photo, linearlayoutAddTransactionPhoto, false);
    imageView.setImageURI(expensePhoto.getThumbnailURI());
    imageView.setTag(expensePhoto);

    expenseModel.getExpensePhotosHashMap().put(expensePhoto.getImageId(), expensePhoto);
    //TODO : Save this image file in sdcard/SQL
    //TODO : Resize image
    //TODO : Generate unique hash as image id

    //As of now let's keep the Linux timestamp in ms as hash id of image

    imageView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        ExpensePhoto expensePhotoTag = (ExpensePhoto) view.getTag();
        expenseModel.getExpensePhotosHashMap().remove(expensePhotoTag.getImageId());
        linearlayoutAddTransactionPhoto.removeView(view);

        if (expenseModel.getExpensePhotosHashMap().size() == 0) {
          binding.textviewAddTransactionPhotoHint.setVisibility(View.VISIBLE);
        } else {
          binding.textviewAddTransactionPhotoHint.setVisibility(View.GONE);
        }
        return true;
      }
    });


    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createShowPhotoDialog();
        showPhotoDialog.show();
      }
    });
    return imageView;
  }

  private void setupListeners() {

    binding.edittextAddTransactionAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean focusGain) {
        if (!focusGain) {
          String amount = ((EditText) view).getText() + "";
          expenseModel.setAmount(Float.parseFloat(amount));
        }
      }
    });


    binding.edittextAddTransactionDescription.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        expenseModel.setDescription(charSequence + "");
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });


    binding.linearlayoutAddTransactionCategory.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        categoryDialog.show();
      }
    });


    binding.linearlayoutAddTransactionAmount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        binding.edittextAddTransactionAmount.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.edittextAddTransactionAmount, 0);

        //ToDo : Move the cursor to left of decimal point if there any
        binding.edittextAddTransactionAmount.setSelection(binding.edittextAddTransactionAmount.getText().length());
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
//        Snackbar.make(rootView, "I will show you available accounts", Snackbar.LENGTH_SHORT).show();
        paymentModeDialog.show();
      }
    });

    binding.textviewAddExpenseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        expenseModel.setAmount(Float.parseFloat(binding.edittextAddTransactionAmount.getText() + ""));
        Toast.makeText(getContext(), expenseModel + "", Toast.LENGTH_LONG).show();
      }
    });


    binding.buttonAddExpenseAddPicture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "This is tilte"), SELECT_IMAGE_FROM_GALLERY_CODE);
      }
    });
  }

  private void updateViewFromModel() {
    float remainingValue = expenseModel.getAmount() - (int) expenseModel.getAmount();
    if (remainingValue != 0) {
      binding.edittextAddTransactionAmount.setText(expenseModel.getAmount() + "");
    } else {
      binding.edittextAddTransactionAmount.setText((int) expenseModel.getAmount() + "");
    }
    binding.edittextAddTransactionDescription.setText(expenseModel.getDescription());
    binding.edittextAddTransactionCategory.setText(expenseModel.getCategory().getName());
    binding.imageviewAddTransactionCategory.setImageResource(expenseModel.getCategory().getImage());
    Calendar calendar = expenseModel.getCalendar();
    setExpenseDate(calendar);
    binding.textviewAddTransactionPayment.setText(expenseModel.getAccount().getName());
  }

  private void setExpenseDate(Calendar calendar) {
    //Month is 0 based
    String[] weekdays = "Sunday Monday Tuesday Wednesday Thursday Friday Saturday".split(" ");
    String[] monthStrings = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    int year, month, dayOfMonth;
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH);
    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    binding.textviewAddTransactionDate.setText(dayOfMonth + " " + monthStrings[month] + " " + year + ",");
    binding.textviewAddTransactionDay.setText(weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
  }

  private void createCategoryDialog() {
    AlertDialog.Builder categoryDialogBuilder = new AlertDialog.Builder(getActivity());

    //Find category box
    LinearLayout linearLayoutBox = viewOfCategoryDialog.findViewById(R.id.dialog_category_picker_linear_layout_box);
    int totalCategories = 14;
    List<CategoryType> categories = getDummyCategories(totalCategories);
    int totalRows = (int) Math.ceil(totalCategories / (float) CATEGORIES_PER_ROW);
    for (int i = 0; i < totalRows; i++) {
      viewOfAddExpenseDialogRow = getCategoryDialogRow();
      for (int j = 0; j < CATEGORIES_PER_ROW && categories.size() > 0; j++) {
        viewOfAddExpenseDialogRow.addView(getLayoutWithImageForCategoryDialog(categories.get(0), viewOfAddExpenseDialogRow));
        categories.remove(0);
      }
      linearLayoutBox.addView(viewOfAddExpenseDialogRow);
    }
    categoryDialogBuilder.setView(viewOfCategoryDialog);
    categoryDialog = categoryDialogBuilder.create();

    categoryDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        updateViewFromModel();
      }
    });
  }

  private void createShowPhotoDialog() {
    AlertDialog.Builder showPhotoBuilder = new AlertDialog.Builder(getActivity());
    View viewOfShowPhotoDialog = getLayoutInflater().inflate(R.layout.dialog_show_image_add_transaction, null);

    showPhotoBuilder.setView(viewOfShowPhotoDialog);
    ViewPager viewPager = viewOfShowPhotoDialog.findViewById(R.id.viewpager_show_photo);

    AdapterViewPagerShowPhoto adapterViewPagerShowPhoto = new AdapterViewPagerShowPhoto(getContext(), expenseModel.getExpensePhotosHashMap());
    viewPager.setAdapter(adapterViewPagerShowPhoto);

    showPhotoDialog = showPhotoBuilder.create();

    showPhotoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        updatePhotos();
        Toast.makeText(getContext(), "Dismissed 2323", Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void updatePhotos() {
    binding.linearlayoutAddTransactionPhoto.removeAllViews();
    for (String key : expenseModel.getExpensePhotosHashMap().keySet()) {
      ExpensePhoto photo = expenseModel.getExpensePhotosHashMap().get(key);
      ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.element_imageview_add_transaction_photo, binding.linearlayoutAddTransactionPhoto, false);

    }
  }

  private void createDateDialog() {
    AlertDialog.Builder calendarBuilder = new AlertDialog.Builder(getActivity());

    DatePicker datePicker = viewOfCalendarDialog.findViewById(R.id.datepicker_add_transactions_calendar);
    datePicker.init(defaultCalendar.get(Calendar.YEAR), defaultCalendar.get(Calendar.MONTH), defaultCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker datePicker1, int year, int month, int dayOfMonth) {
        expenseModel.getCalendar().set(Calendar.YEAR, year);
        expenseModel.getCalendar().set(Calendar.MONTH, month);
        expenseModel.getCalendar().set(Calendar.DATE, dayOfMonth);
        calendarDialog.dismiss();
      }
    });

    calendarBuilder.setView(viewOfCalendarDialog);
    calendarDialog = calendarBuilder.create();
    calendarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        updateViewFromModel();
      }
    });
  }

  private void createPaymentModeDialog() {
    AlertDialog.Builder paymentBuilder = new AlertDialog.Builder(getActivity());
    LinearLayout linearLayoutBox = viewOfPaymentDialog.findViewById(R.id.linearlayout_box_add_expense_select_account);
    List<Account> accountList = getDummyAccounts(5);

    for (Account account : accountList) {
      linearLayoutBox.addView(getRowForPaymentModeDialog(account));
    }

    paymentBuilder.setView(viewOfPaymentDialog);
    paymentModeDialog = paymentBuilder.create();
    paymentModeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        updateViewFromModel();
      }
    });
  }

  private LinearLayout getRowForPaymentModeDialog(Account account) {

    View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Account account = (Account) view.getTag();
        expenseModel.setAccount(account);
        paymentModeDialog.dismiss();
//        Toast.makeText(getContext(), "Selected account : " + view.getTag(), Toast.LENGTH_SHORT).show();
      }
    };
    LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.row_add_expense_select_account, null);
    linearLayout.setOnClickListener(onClickListener);
    linearLayout.setTag(account);
    ((TextView) linearLayout.findViewById(R.id.textview_add_expense_select_account)).setText(account.getName());
    return linearLayout;
  }

  private LinearLayout getLayoutWithImageForCategoryDialog(CategoryType categoryType, ViewGroup parent) {
    View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        CategoryType categoryType = (CategoryType) view.getTag();
        expenseModel.setCategory(categoryType);
        categoryDialog.dismiss();
//        Toast.makeText(getContext(), "Selected category : " + view.getTag(), Toast.LENGTH_SHORT).show();
      }
    };
    LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.add_expense_dialog_category, parent, false);
    linearLayout.setTag(categoryType);
    linearLayout.setOnClickListener(onClickListener);
    ((ImageView) linearLayout.findViewById(R.id.imageview_add_expense_dialog_category)).setImageResource(categoryType.getImage());
    ((TextView) linearLayout.findViewById(R.id.textview_add_expense_dialog_category)).setText(categoryType.getName());
    return linearLayout;
  }

  private LinearLayout getCategoryDialogRow() {
    return (LinearLayout) getLayoutInflater().inflate(R.layout.add_activity_dialog_row, null);
  }

  private List<Account> getDummyAccounts(int n) {
    String accountNames[] = new String[]{"HDFC 6021", "SBI 5500", "Kotak 22231", "Airtel", "PayTm"};
    String accountIds[] = new String[]{"hdfc_6021", "sbi_5500", "kotak_22231", "airtel", "paytm"};

    List<Account> accounts = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      int k = FirstFragment.getRandomNumber(0, accountNames.length);
      accounts.add(new Account(accountNames[k], accountIds[k]));
    }
    return accounts;
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
















