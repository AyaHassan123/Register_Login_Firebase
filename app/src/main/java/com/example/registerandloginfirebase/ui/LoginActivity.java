
package com.example.registerandloginfirebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.registerandloginfirebase.R;
import com.example.registerandloginfirebase.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityLoginBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        listnerButtonLogIn();
        listnerButtonSignUp();
    }

    private void listnerButtonLogIn() {
        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }
    private void listnerButtonSignUp() {
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void getData(){
        String email = binding.emaillogin.getText().toString().trim();
        String password = binding.passlogin.getText().toString().trim();
        

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        authenticateUser(email,password);
    }
    private void authenticateUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            if (!(email.equals(auth.getCurrentUser().getEmail()))) {
                                Toast.makeText(LoginActivity.this, "Email isn,t correct",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Password isn,t correct",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                    }
                });
    }
}
