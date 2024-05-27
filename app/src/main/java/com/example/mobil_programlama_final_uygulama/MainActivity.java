package com.example.mobil_programlama_final_uygulama;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobil_programlama_final_uygulama.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_OTURUM= "oturum";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //oturum kontrolü yapan kod
        if (!isUserLoggedIn()){
            showSplashScreen();
        }else {
            showMainContent();
        }
    }

    private boolean isUserLoggedIn(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(KEY_OTURUM,false);
    }

    public void showSplashScreen(){
        setContentView(R.layout.splash_layout);

        View btnSplashLogin = findViewById(R.id.btnSplashLogin);
        View btnSplashRegister= findViewById(R.id.btnSplashRegister);

        btnSplashLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }
        });
        btnSplashRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    private void showMainContent(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add_label,
                R.id.nav_add_photo,
                R.id.nav_gallery,
                R.id.nav_about,
                R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                int id = item.getItemId();

                if (id== R.id.nav_logout){
                    SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(KEY_OTURUM,false);
                    editor.apply();
                    showSplashScreen();
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController,mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }
}

