package com.example.mobil_programlama_final_uygulama.ui.addLabel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobil_programlama_final_uygulama.LabelAdapter;
import com.example.mobil_programlama_final_uygulama.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mobil_programlama_final_uygulama.LabelModel;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AddLabelFragment extends Fragment {

    private View rootView;
    private EditText etLabel, etDescription;
    private Button btnAdd;
    private TextView tvLabel, tvDescription;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private RecyclerView recyclerViewLabels;
    private LabelAdapter labelAdapter;
    private List<LabelModel> labelList;

    public AddLabelFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_label, container, false);

        recyclerViewLabels = rootView.findViewById(R.id.recyclerViewLabels);
        recyclerViewLabels.setLayoutManager((new LinearLayoutManager(getActivity())));
        labelList = new ArrayList<>();
        labelAdapter = new LabelAdapter(labelList);
        recyclerViewLabels.setAdapter(labelAdapter);

        etLabel = rootView.findViewById(R.id.etLabel);
        etDescription = rootView.findViewById(R.id.etDescription);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        tvLabel= rootView.findViewById(R.id.tvLabel);
        tvDescription= rootView.findViewById(R.id.tvDescription);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String label = etLabel.getText().toString();
                String description = etDescription.getText().toString();
                addLabelToFirestore(label, description);
            }
        });
        getLabelsFromFirestore();
        return rootView;
    }
    private void getLabelsFromFirestore() {
        db.collection("Labels")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    labelAdapter.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> data = document.getData();
                        LabelModel label = LabelModel.toObject(data);
                        labelAdapter.add(label);
                    }
                })
                .addOnFailureListener(e -> {
                    // Firestore'dan veri çekme başarısız oldu
                    Toast.makeText(getActivity(), "Firestore'dan veri çekme başarısız", Toast.LENGTH_SHORT).show();
                });
    }

    private void addLabelToFirestore(String label, String description){
        Map<String, Object> labelMap = new HashMap<>();
        labelMap.put("Label", label);
        labelMap.put("Description", description);

        db.collection("Labels").add(labelMap).addOnSuccessListener(documentReference -> {
            Toast.makeText(getActivity(), "Firestore'a kayıt başarılı", Toast.LENGTH_SHORT).show();
            getLabelsFromFirestore();
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Firestore'a kayıt başarısız", Toast.LENGTH_SHORT).show();
        });
    }
}