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
    private static final String KEY_OTURUM = "oturum";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Oturum kontrolü yap
        if (!isUserLoggedIn()) {
            // Oturum kapalıysa splash ekranını göster
            showSplashScreen();
        } else {
            // Oturum açıksa normal sayfa içeriğini göster
            showMainContent();
        }
    }

    // Kullanıcının oturum açık olup olmadığını kontrol et
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return preferences.getBoolean(KEY_OTURUM, false);
    }

    // Splash ekranını göster
    public void showSplashScreen() {
        setContentView(R.layout.splash_layout);

        // Burada splash ekranında gösterilecek içeriği ayarlayabilirsiniz
        // Splash ekranındaki butonu bul
        View btnSplashLogin = findViewById(R.id.btnSplashLogin);
        View btnSplashRegister = findViewById(R.id.btnSplashRegister);

        // Butona tıklama olayını ekle
        btnSplashLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Butona tıklandığında Login ekranına geç
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });
        btnSplashRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Butona tıklandığında Login ekranına geç
                startActivity(new Intent(MainActivity.this, Register.class));
                finish();
            }
        });

    }

    // Normal sayfa içeriğini göster
    private void showMainContent() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_logout) {
                    // Logout seçeneğine tıklandığında oturumu kapat ve splash ekranını göster
                    SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(KEY_OTURUM, false);
                    editor.apply();
                    showSplashScreen();
                    Toast.makeText(MainActivity.this, "Oturum kapatılması gerekir", Toast.LENGTH_SHORT).show();
                    drawer.closeDrawer(GravityCompat.START); // Seçeneklere tıkladıktan sonra Drawer'ı kapat
                    return true;
                }

                // Diğer seçenekleri buraya ekleyebilirsiniz

                drawer.closeDrawer(GravityCompat.START); // Seçeneklere tıkladıktan sonra Drawer'ı kapat
                return false;
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
}