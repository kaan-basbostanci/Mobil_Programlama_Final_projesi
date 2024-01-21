package com.example.mobil_programlama_final_uygulama.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Uygulamaya Hoş Geldiniz");
    }

    public LiveData<String> getText() {
        return mText;
    }
}