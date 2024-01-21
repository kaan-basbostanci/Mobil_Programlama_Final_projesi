package com.example.mobil_programlama_final_uygulama.ui.addPhoto;

import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobil_programlama_final_uygulama.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class AddPhotoFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private View rootView;
    private ImageView imageView;
    private LinearLayout labelContainer;
    private Button btnCamera, btnAdd;

    private FirebaseFirestore db;

    private String currentPhotoPath;
    private List<String> labels;
    private File currentPhotoFile;

    public AddPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        labels = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_photo, container, false);

        imageView = rootView.findViewById(R.id.imageView);
        labelContainer = rootView.findViewById(R.id.labelContainer);
        btnCamera = rootView.findViewById(R.id.btnCamera);
        btnAdd = rootView.findViewById(R.id.btnAdd);

        btnCamera.setOnClickListener(view -> dispatchTakePictureIntent());
        btnAdd.setOnClickListener(view -> uploadPhotoToFirestore());

        // Firestore'dan labelleri çek
        getLabelsFromFirestore();

        return rootView;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.example.mobil_programlama_final_uygulama.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // 1. createImageFile metodundan dönen dosya nesnesini kullanarak currentPhotoFile adlı bir değişkeni güncelleyin
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        currentPhotoFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return currentPhotoFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Çekilen fotoğrafı görüntüleme
            displayCapturedPhoto();
        }
    }

    private void displayCapturedPhoto() {
        if (currentPhotoFile != null) {
            // Burada bir Uri oluşturuyoruz
            Uri photoUri = Uri.fromFile(currentPhotoFile);

            // Oluşturduğumuz Uri'yi kullanarak ImageView'a fotoğrafı yükleyebiliriz
            Glide.with(this)
                    .load(photoUri)
                    .into(imageView);
        } else {
            Toast.makeText(requireContext(), "Fotoğraf bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLabelsFromFirestore() {
        db.collection("Labels")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    labels.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String label = document.getString("Label");
                        labels.add(label);
                    }
                    updateLabelUI();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Firestore'dan veri çekme başarısız", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateLabelUI() {
        labelContainer.removeAllViews();
        for (String label : labels) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(label);
            labelContainer.addView(checkBox);
        }
    }

    private List<String> getSelectedLabels() {
        List<String> selectedLabels = new ArrayList<>();
        for (int i = 0; i < labelContainer.getChildCount(); i++) {
            View view = labelContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    selectedLabels.add(checkBox.getText().toString());
                }
            }
        }
        return selectedLabels;
    }

    private void uploadPhotoToFirestore() {
        if (currentPhotoFile != null) {
            uploadImageToFirestore(currentPhotoFile);
        } else {
            Toast.makeText(requireContext(), "Dosya yolu bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    // 2. uploadImageToFirestore metodunu aşağıdaki gibi güncelleyin
    private void uploadImageToFirestore(File imageFile) {
        if (imageFile != null) {
            Uri imageUri = Uri.fromFile(imageFile);
            StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("photos/" + UUID.randomUUID() + ".jpg");

            photoRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        photoRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String imageURL = uri.toString();
                                    savePhotoToFirestore(imageURL);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "URL alınamadı", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Fotoğraf yüklenemedi", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(requireContext(), "Dosya yolu bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePhotoToFirestore(String imageURL) {
        List<String> selectedLabels = getSelectedLabels();

        Map<String, Object> photoMap = new HashMap<>();
        photoMap.put("url", imageURL);
        photoMap.put("labels", selectedLabels);

        db.collection("Photos")
                .add(photoMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(requireContext(), "Fotoğraf Firestore'a eklendi", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Firestore'a kayıt başarısız", Toast.LENGTH_SHORT).show();
                });
    }
}