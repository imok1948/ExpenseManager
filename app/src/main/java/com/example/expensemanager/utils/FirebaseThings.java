package com.example.expensemanager.utils;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.expensemanager.MainActivity;
import com.example.expensemanager.adapters.AdapterTransactions;
import com.example.expensemanager.fragments.FirstFragment;
import com.example.expensemanager.interfaces.FirebaseCallback;
import com.example.expensemanager.models.TransactionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseThings {
  private static final String TAG = "FIREBASE_THINGS";

  //Firebase things
  private FirebaseFirestore transactionsFirestore, accountsFirestore, categoriesFirestore;
  private FirebaseStorage expenseModelPhotosStorage, categoriesPhotosStorage;

  private static final String EXPENSE_MODEL_PATH_ON_FIREBASE = "expenseManager/bob/transactions";
  private static final String EXPENSE_MODEL_PHOTO_PATH_ON_STORAGE = "/expenseManager/bob/transactions";


  //Communication with activities and other things
  private FirebaseCallback firebaseCallback;


  public FirebaseCallback getFirebaseCallback() {
    return firebaseCallback;
  }

  public void setFirebaseCallback(FirebaseCallback firebaseCallback) {
    this.firebaseCallback = firebaseCallback;
  }

  public FirebaseThings(Context context) {
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
    FirebaseApp.initializeApp(context);
    transactionsFirestore = FirebaseFirestore.getInstance();
    transactionsFirestore.setFirestoreSettings(settings);

    expenseModelPhotosStorage = FirebaseStorage.getInstance();
  }

  //TODO : Try if the previous context can work here
  public void firebaseAddTransaction(ExpenseModel expenseModel) {
    firebaseUploadImages(expenseModel.getExpensePhotosHashMap());

    CollectionReference transactionCollectionReference = transactionsFirestore.collection(EXPENSE_MODEL_PATH_ON_FIREBASE);
    transactionCollectionReference.add(expenseModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
      @Override
      public void onComplete(@NonNull Task<DocumentReference> task) {
        if (task.isSuccessful()) {
          Log.d(TAG, "Document : " + task.getResult().getId() + " added to Firebase");
        }
        else {
        }
      }
    });
  }

  private void firebaseUploadImages(HashMap<String, ExpensePhoto> expensePhotosHashMap) {
    for (ExpensePhoto expensePhoto : expensePhotosHashMap.values()) {
      StorageReference storageReference = expenseModelPhotosStorage.getReference().child(EXPENSE_MODEL_PHOTO_PATH_ON_STORAGE + "/" + expensePhoto.getImageId() + ".jpg");

      UploadTask uploadTask = storageReference.putFile(expensePhoto.getImageUri());

      uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
          if (task.isSuccessful()) {
            Log.d(TAG, "Document : " + task.getResult().getUploadSessionUri() + " uploaded to Storage");
          }
          else {
          }
        }
      });
    }
  }


  //Firebase get all transactions

  public List<TransactionsModel> firebaseGetTransactions(Context context, AdapterTransactions adapterTransactions) {

    List<TransactionsModel> transactionsModels = new ArrayList<>();
    transactionsFirestore.collection(EXPENSE_MODEL_PATH_ON_FIREBASE).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          Toast.makeText(context, "Retrieving data from Firestore success : " + task.getResult().size(), Toast.LENGTH_SHORT).show();

          float totalIncome = 0;
          float totalExpense = 0;

          for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            ExpenseModel expenseModel = documentSnapshot.toObject(ExpenseModel.class);
            //Toast.makeText(context, expenseModel.toString(), Toast.LENGTH_SHORT).show();
            transactionsModels.add(Utilities.convertExpenseModelToTransactionsModel(expenseModel));
          }
          Log.d("data", transactionsModels.size() + "");
          adapterTransactions.setTransactions(transactionsModels);
          adapterTransactions.notifyDataSetChanged();
        }
        else {
          Toast.makeText(context, "Retrieving data from Firestore failed", Toast.LENGTH_SHORT).show();
        }
      }
    });
    return transactionsModels;
  }

  public List<ExpenseModel> firebaseGetTransactions() {
    List<ExpenseModel> expenseModelList = new ArrayList<>();
    transactionsFirestore.collection(EXPENSE_MODEL_PATH_ON_FIREBASE).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
          for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
            ExpenseModel expenseModel = documentSnapshot.toObject(ExpenseModel.class);
            expenseModelList.add(expenseModel);
          }
          if (firebaseCallback != null) {
            firebaseCallback.onExpensesReceived(expenseModelList);
          }
          else {
            Log.d(TAG, "firebaseCallback == null");
          }
        }
        else {
          Log.d(TAG, "Error while retrieving data ");
        }
      }
    });
    return expenseModelList;
  }

}






















