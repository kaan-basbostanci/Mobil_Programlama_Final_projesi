package com.example.mobil_programlama_final_uygulama.ui.gallery;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mobil_programlama_final_uygulama.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.widget.Toast;

public class GalleryFragment extends Fragment {

    private Spinner dropdown;
    private LinearLayout linearLayout;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        dropdown = view.findViewById(R.id.dropdown);
        linearLayout = view.findViewById(R.id.linearLayout);

        db = FirebaseFirestore.getInstance();

        // Firestore'dan Labels koleksiyonundaki etiketleri çek
        loadLabels();

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Seçilen etiket değiştiğinde yapılacak işlemleri buraya ekleyin
                String selectedLabel = (String) parentView.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selectedLabel)) {
                    loadPhotos(selectedLabel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Bir şey seçilmediğinde yapılacak işlemleri buraya ekleyin
            }
        });

        return view;
    }

    private void loadLabels() {
        db.collection("Labels").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> labels = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String label = document.getString("Label");
                                if (!labels.contains(label)) {
                                    labels.add(label);
                                }
                            }


                                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                        android.R.layout.simple_spinner_dropdown_item, labels);
                                dropdown.setAdapter(adapter);

                                // Adapter güncellendi, bildirim yap
                                adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadPhotos(String selectedLabel) {
        linearLayout.removeAllViews(); // Önceki gösterimi temizle

        db.collection("Photos")
                .whereArrayContains("labels", selectedLabel)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String url = document.getString("url");
                                List<String> labels = (List<String>) document.get("labels");
                                showPhoto(url, labels);
                            }
                        }
                    }
                });
    }

    private void showPhoto(String url, List<String> labels) {
        // Yeni bir fotoğraf ve etiket gösterimi oluştur
        View photoView = LayoutInflater.from(requireContext()).inflate(R.layout.item_photo, null);

        TextView labelsTextView = photoView.findViewById(R.id.labelsTextView);
        ImageView photoImageView = photoView.findViewById(R.id.photoImageView);


        labelsTextView.setText(TextUtils.join(", ", labels));

        // Fotoğrafı Glide kullanarak yükle
        Glide.with(requireContext())
                .load(url)
                .into(photoImageView);

        // Fotoğraf ve etiket gösterimini LinearLayout'a ekle
        linearLayout.addView(photoView);
    }
}