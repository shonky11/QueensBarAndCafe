package com.qads.queensbarandcafe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.models.AllergiesModel;

import java.util.ArrayList;

public class AllergiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AllergiesModel> allergiesSet;
    private onAllergiesClickListener mAllergiesClickListener;
    private int total_types;

    public interface onAllergiesClickListener {
        void onAllergiesClick(View v, int position);
    }

    public void setOnAllergiesClickListener(AllergiesAdapter.onAllergiesClickListener listener) {
        mAllergiesClickListener = listener;
    }


    public class AllergiesViewHolder extends RecyclerView.ViewHolder {

        TextView AllergyName;
        CheckBox AllergyChecker;
        LinearLayout AllergyClicker;
        TextView Button;

        public AllergiesViewHolder(final View itemView) {
            super(itemView);

            this.AllergyName = (TextView) itemView.findViewById(R.id.allergy_name);
            this.AllergyChecker = (CheckBox) itemView.findViewById(R.id.allergy_radio);
            this.AllergyClicker = (LinearLayout) itemView.findViewById(R.id.allergy_click);

            AllergyClicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAllergiesClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mAllergiesClickListener.onAllergiesClick(itemView, position);
                        }
                    }
                }
            });
        }
    }


    public AllergiesAdapter(ArrayList<AllergiesModel>data){
        this.allergiesSet = data;
        total_types = allergiesSet.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        itemView = inflater.inflate(R.layout.allergies_item, parent, false);
        AllergiesAdapter.AllergiesViewHolder allergiesViewHolder = new AllergiesAdapter.AllergiesViewHolder(itemView);
        return allergiesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AllergiesModel allergy = allergiesSet.get(position);
        String allergyName = allergy.getAllergy();
        Boolean allergySelected = allergy.getSelected();

        ((AllergiesViewHolder) holder).AllergyName.setText(allergyName);
        ((AllergiesViewHolder) holder).AllergyChecker.setChecked(allergySelected);

    }

    @Override
    public int getItemCount() {
        return allergiesSet.size();
    }
}
