package com.example.registerandloginfirebase.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.registerandloginfirebase.R;
import com.example.registerandloginfirebase.databinding.ActivityMainBinding;
import com.example.registerandloginfirebase.databinding.ActivityRegisterBinding;
import com.example.registerandloginfirebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityRegisterBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);


        //user login
        if(auth.getCurrentUser()!=null){
         showMainActivity();
        }
        listnerButtonLogIn();
        listnerButtonSignUp();
    }

    private void showMainActivity() {
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
    }

    private void listnerButtonSignUp() {
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

    }
    private void listnerButtonLogIn() {
        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getData(){
        if (binding.name.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.email.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.passward.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.passward.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            creatNewUser(binding.email.getText().toString().trim(),binding.passward.getText().toString().trim());
        }
    }
    private void creatNewUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            User user = new User(binding.name.getText().toString().trim(),email,password);
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(auth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                  startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                }
                            });
                        }
                    }
                });
    }
}