package com.example.collegeapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorAdapter extends RecyclerView.Adapter<CoordinatorAdapter.ViewHolder> {

    private List<Coordinator> coordinatorList;
    private List<Coordinator> selectedCoordinators;

    public CoordinatorAdapter() {
        coordinatorList = new ArrayList<>();
        selectedCoordinators = new ArrayList<>();
    }

    public void setCoordinatorList(List<Coordinator> coordinatorList) {
        this.coordinatorList = coordinatorList;
    }

    public List<Coordinator> getSelectedCoordinators() {
        return selectedCoordinators;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coordinator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coordinator coordinator = coordinatorList.get(position);
        holder.textViewName.setText(coordinator.getName());

        // Set the checkbox listener
        holder.checkBox.setOnCheckedChangeListener(null); // Reset the listener to avoid recycled view issue
        holder.checkBox.setChecked(selectedCoordinators.contains(coordinator));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedCoordinators.add(coordinator);
            } else {
                selectedCoordinators.remove(coordinator);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coordinatorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
