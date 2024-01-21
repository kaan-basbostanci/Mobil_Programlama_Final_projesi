package com.example.mobil_programlama_final_uygulama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
 EditText etEmail, etPassword;
 Button btnLogin;
 TextView tvRegister;
 FirebaseAuth auth;
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_OTURUM = "oturum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view ){
         login();
        }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }

    private void login() {
      String email= etEmail.getText().toString();
      String password = etPassword.getText().toString();

      auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
              new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(Login.this, "Login Başarılı", Toast.LENGTH_SHORT).show();
                          // Kullanıcının oturum açık olduğunu SharedPreferences'e kaydet
                          SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                          SharedPreferences.Editor editor = preferences.edit();
                          editor.putBoolean(KEY_OTURUM, true);
                          editor.apply();
                          Intent intent = new Intent (Login.this, MainActivity.class);
                          startActivity(intent);
                      }else {
                          Toast.makeText(Login.this, "Email ya da parola hatalı", Toast.LENGTH_SHORT).show();
                      }
                  }
              }
      );
    }

}

