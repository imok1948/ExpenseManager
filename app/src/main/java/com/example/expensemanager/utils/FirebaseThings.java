package com.example.expensemanager.utils;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.expensemanager.adapters.AdapterTransactions;
import com.example.expensemanager.models.TransactionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
  private Context context = null;


  //Firebase things
  private FirebaseFirestore transactionsFirestore, accountsFirestore, categoriesFirestore;
  private FirebaseStorage expenseModelPhotosStorage, categoriesPhotosStorage;

  private static final String EXPENSE_MODEL_PATH_ON_FIREBASE = "expenseManager/bob/transactions";
  private static final String EXPENSE_MODEL_PHOTO_PATH_ON_STORAGE = "/expenseManager/bob/transactions";


  public FirebaseThings(Context context) {
    this.context = context;
    FirebaseApp.initializeApp(context);
    transactionsFirestore = FirebaseFirestore.getInstance();
    expenseModelPhotosStorage = FirebaseStorage.getInstance();
  }

  //TODO : Try if the previous context can work here
  public void firebaseAddTransaction(ExpenseModel expenseModel, Context context) {
    firebaseUploadImages(expenseModel.getExpensePhotosHashMap(), context);

    CollectionReference transactionCollectionReference = transactionsFirestore.collection(EXPENSE_MODEL_PATH_ON_FIREBASE);
    transactionCollectionReference.add(expenseModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
      @Override
      public void onComplete(@NonNull Task<DocumentReference> task) {
        if (task.isSuccessful()) {
          Toast.makeText(context, "Adding model to Firebase success", Toast.LENGTH_SHORT).show();
        }
        else {
          Toast.makeText(context, "Adding model to Firebase failed", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void firebaseUploadImages(HashMap<String, ExpensePhoto> expensePhotosHashMap, Context context) {
    for (ExpensePhoto expensePhoto : expensePhotosHashMap.values()) {
      StorageReference storageReference = expenseModelPhotosStorage.getReference().child(EXPENSE_MODEL_PHOTO_PATH_ON_STORAGE + "/" + expensePhoto.getImageId() + ".jpg");


      UploadTask uploadTask = storageReference.putFile(expensePhoto.getImageUri());

      uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
          if (task.isSuccessful()) {
            Toast.makeText(context, "uploading image success => " + expensePhoto.getImageId(), Toast.LENGTH_SHORT).show();
          }
          else {
            Toast.makeText(context, "uploading image failed => " + expensePhoto.getImageId(), Toast.LENGTH_SHORT).show();
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
}






















