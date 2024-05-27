package com.example.mobil_programlama_final_uygulama.ui.logout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobil_programlama_final_uygulama.Login;
import com.example.mobil_programlama_final_uygulama.MainActivity;
import com.example.mobil_programlama_final_uygulama.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LogoutFragment extends Fragment {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_OTURUM = "oturum";

    private void redirectToLogin() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).goToLogin();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        // Logout butonunu bul
        Button btnLogout = view.findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Oturumu kapat ve splash ekranını göster
                SharedPreferences preferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_OTURUM, false);
                editor.apply();


                redirectToLogin();
            }
        });

        return view;
    }
}