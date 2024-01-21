package com.example.mobil_programlama_final_uygulama;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobil_programlama_final_uygulama.R;

import java.util.List;

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.LabelViewHolder> {

    private List<LabelModel> labelList;

    public LabelAdapter(List<LabelModel> labelList) {
        this.labelList = labelList;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label, parent, false);
        return new LabelViewHolder(view);
    }
    public void clear() {
        labelList.clear();
        notifyDataSetChanged();
    }
    public void add(LabelModel label) {
        labelList.add(label);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, int position) {
        LabelModel label = labelList.get(position);
        holder.tvLabel.setText(label.getLabel());
        holder.tvDescription.setText(label.getDescription());
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    public static class LabelViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel, tvDescription;

        public LabelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabelItem);
            tvDescription = itemView.findViewById(R.id.tvDescriptionItem);
        }
    }
}
