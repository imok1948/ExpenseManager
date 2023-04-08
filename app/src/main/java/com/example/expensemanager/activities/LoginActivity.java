package com.example.expensemanager.activities;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
  private FirebaseAuth mAuth;
  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;
  private FirebaseFirestore db;
  private EditText courseNameEdt, courseDurationEdt, courseDescriptionEdt;
  private Button submitCourseBtn;
  private String courseName, courseDuration, courseDescription;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    FirebaseApp.initializeApp(getApplicationContext());
    db = FirebaseFirestore.getInstance();

    courseNameEdt = findViewById(R.id.idEdtCourseName);
    courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
    courseDurationEdt = findViewById(R.id.idEdtCourseDuration);
    submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);

    submitCourseBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        courseName = courseNameEdt.getText().toString();
        courseDescription = courseDescriptionEdt.getText().toString();
        courseDuration = courseDurationEdt.getText().toString();

        if (TextUtils.isEmpty(courseName)) {
          courseNameEdt.setError("Please enter Course Name");
        }
        else if (TextUtils.isEmpty(courseDescription)) {
          courseDescriptionEdt.setError("Please enter Course Description");
        }
        else if (TextUtils.isEmpty(courseDuration)) {
          courseDurationEdt.setError("Please enter Course Duration");
        }
        else {
          addDataToFirestore(courseName, courseDescription, courseDuration);
        }
      }
    });
  }

  private void addDataToFirestore(String courseName, String courseDescription, String courseDuration) {
    CollectionReference dbCourses = db.collection("Courses");
    Courses courses = new Courses(courseName, courseDescription, courseDuration);
    dbCourses.add(courses).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
      @Override
      public void onSuccess(DocumentReference documentReference) {
        Toast.makeText(LoginActivity.this, "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(LoginActivity.this, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
  }
}
