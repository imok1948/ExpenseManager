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
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.expensemanager.R;
import com.example.expensemanager.adapters.AdapterViewPagerShowPhoto;
import com.example.expensemanager.databinding.FragmentSecondBinding;
import com.example.expensemanager.utils.Account;
import com.example.expensemanager.utils.CategoryType;
import com.example.expensemanager.utils.ExpenseModel;
import com.example.expensemanager.utils.ExpensePhoto;
import com.example.expensemanager.utils.FirebaseThings;
import com.example.expensemanager.utils.Utilities;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class SecondFragment extends Fragment {

  private static final int CATEGORIES_PER_ROW = 4;
  private static final String TAG = Fragment.class.toString();
  private static final int SELECT_IMAGE_FROM_GALLERY_CODE = 101;
  private static final int EXPENSE_IMAGES_TO_BE_SHOWN_IN_LINEAR_LAYOUT = 4;
  private static final String EXPENSE_MODEL_PATH_ON_FIREBASE = "expenseManager/bob/transactions";

  private FragmentSecondBinding binding;
  private View rootView;
  private Calendar defaultCalendar;
  private View viewOfCalendarDialog, viewOfCategoryDialog, viewOfPaymentDialog;
  private AlertDialog calendarDialog, categoryDialog, paymentModeDialog, showPhotoDialog;
  private LinearLayout viewOfAddExpenseDialogRow;


  private ExpenseModel expenseModel;


  //Firebase things
  private FirebaseThings firebaseThings = null;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSecondBinding.inflate(inflater, container, false);
    return binding.getRoot();
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
    //TODO : Enable requestFocus after development completed
    //binding.edittextAddTransactionAmount.requestFocus();
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
    expenseModel = Utilities.createRandomExpenseModel();
    //new ExpenseModel(197F, "", new CategoryType("1", "Foods", R.drawable.foods), defaultCalendar.getTimeInMillis(), new Account("HDFC 6022", "hdfc_6022"), new LinkedHashMap<>());

    //Initialize with their respective views
    viewOfCalendarDialog = getLayoutInflater().inflate(R.layout.dialog_select_date, null);
    viewOfCategoryDialog = getLayoutInflater().inflate(R.layout.dialog_category_picker, null);
    viewOfPaymentDialog = getLayoutInflater().inflate(R.layout.dialog_payment_mode, null);

    //Firebase things
    firebaseThings = new FirebaseThings(getContext());
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SELECT_IMAGE_FROM_GALLERY_CODE && data != null) {
      Uri uri = data.getData();


      //ExpensePhoto expensePhoto = new ExpensePhoto(uri, uri, uri.toString());
      ExpensePhoto expensePhoto = new ExpensePhoto(uri, uri, System.currentTimeMillis() + "");
      addImageViewOfTransactionPhoto(expensePhoto, binding.linearlayoutAddTransactionPhoto);

      if (expenseModel.getExpensePhotosHashMap().size() == 0) {
        binding.textviewAddTransactionPhotoHint.setVisibility(View.VISIBLE);
      }
      else {
        binding.textviewAddTransactionPhotoHint.setVisibility(View.GONE);
      }
    }
  }

  private void addImageViewOfTransactionPhoto(ExpensePhoto expensePhoto, ViewGroup linearlayoutAddTransactionPhoto) {

    binding.textviewAddTransactionPhotoHint.setVisibility(View.GONE);
    expensePhoto.setThumbnailURI(Utilities.getResizedImageUri(getContext(), expensePhoto.getImageUri(), getActivity().getExternalCacheDir()));
    ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.element_imageview_add_transaction_photo, linearlayoutAddTransactionPhoto, false);
    imageView.setImageURI(expensePhoto.getThumbnailURI());
    imageView.setTag(expensePhoto);

    expenseModel.getExpensePhotosHashMap().put(expensePhoto.getImageId(), expensePhoto);
    //TODO : Save this image file in sdcard/SQL
    //TODO : Generate unique hash as image id

    //As of now let's keep the Linux timestamp in ms as hash id of image

    imageView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        ExpensePhoto expensePhotoTag = (ExpensePhoto) view.getTag();
        expenseModel.getExpensePhotosHashMap().remove(expensePhotoTag.getImageId());
        linearlayoutAddTransactionPhoto.removeView(view);
        Toast.makeText(getContext(), "Photo deleted", Toast.LENGTH_SHORT).show();

        if (expenseModel.getExpensePhotosHashMap().size() == 0) {
          binding.textviewAddTransactionPhotoHint.setVisibility(View.VISIBLE);
        }
        else {
          binding.textviewAddTransactionPhotoHint.setVisibility(View.GONE);
        }
        return true;
      }
    });


    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        int position = 0;
        int currentItem = 0;
        for (String key : expenseModel.getExpensePhotosHashMap().keySet()) {
          if (view.getTag() == expenseModel.getExpensePhotosHashMap().get(key)) {
            position = currentItem;
            break;
          }
          currentItem += 1;
        }

        createShowPhotoDialog(position);
        showPhotoDialog.show();
      }
    });

    linearlayoutAddTransactionPhoto.addView(imageView);
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
        // Toast.makeText(getContext(), expenseModel + "", Toast.LENGTH_LONG).show();
        Log.d("showTransaction", expenseModel + "");

        //Upload images to Storage
        //firebaseThings.firebaseGetTransactions(getContext(), null);
        //Add expense to Firestore
        firebaseThings.firebaseAddTransaction(expenseModel);
        Log.d("expense", expenseModel + "");

        expenseModel = Utilities.createRandomExpenseModel();
        updateViewFromModel();
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
    binding.arrowBackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Navigation.findNavController(binding.getRoot()).popBackStack();
      }
    });
  }

  private void updateViewFromModel() {
    float remainingValue = expenseModel.getAmount() - (int) expenseModel.getAmount();
    if (remainingValue != 0) {
      binding.edittextAddTransactionAmount.setText(expenseModel.getAmount() + "");
    }
    else {
      binding.edittextAddTransactionAmount.setText((int) expenseModel.getAmount() + "");
    }
    binding.edittextAddTransactionDescription.setText(expenseModel.getDescription());
    binding.edittextAddTransactionCategory.setText(expenseModel.getCategory().getName());
    binding.imageviewAddTransactionCategory.setImageResource(expenseModel.getCategory().getImage());

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(expenseModel.getTimestamp());
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
    List<CategoryType> categories = Utilities.getDummyCategories(totalCategories);
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

  private void createShowPhotoDialog(int position) {
    AlertDialog.Builder showPhotoBuilder = new AlertDialog.Builder(getActivity());
    View viewOfShowPhotoDialog = getLayoutInflater().inflate(R.layout.dialog_show_image_add_transaction, null);
    showPhotoBuilder.setView(viewOfShowPhotoDialog);
    showPhotoDialog = showPhotoBuilder.create();
    showPhotoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        updatePhotos();
      }
    });

    ViewPager viewPager = viewOfShowPhotoDialog.findViewById(R.id.viewpager_show_photo);
    AdapterViewPagerShowPhoto adapterViewPagerShowPhoto = new AdapterViewPagerShowPhoto(getContext(), expenseModel.getExpensePhotosHashMap(), showPhotoDialog);
    viewPager.setAdapter(adapterViewPagerShowPhoto);
    viewPager.setCurrentItem(position);
  }

  private void updatePhotos() {
    binding.linearlayoutAddTransactionPhoto.removeAllViews();
    binding.textviewAddTransactionPhotoHint.setVisibility(View.VISIBLE);

    for (String key : expenseModel.getExpensePhotosHashMap().keySet()) {
      ExpensePhoto expensePhoto = expenseModel.getExpensePhotosHashMap().get(key);
      addImageViewOfTransactionPhoto(expensePhoto, binding.linearlayoutAddTransactionPhoto);
    }
  }

  private void createDateDialog() {
    AlertDialog.Builder calendarBuilder = new AlertDialog.Builder(getActivity());

    DatePicker datePicker = viewOfCalendarDialog.findViewById(R.id.datepicker_add_transactions_calendar);
    datePicker.init(defaultCalendar.get(Calendar.YEAR), defaultCalendar.get(Calendar.MONTH), defaultCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker datePicker1, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(expenseModel.getTimestamp());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, dayOfMonth);
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
    List<Account> accountList = Utilities.getDummyAccounts(5);

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
}
















