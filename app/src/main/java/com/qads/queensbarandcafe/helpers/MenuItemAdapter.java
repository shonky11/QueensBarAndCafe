package com.qads.queensbarandcafe.helpers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.fragments.ItemsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class MenuItemAdapter extends FirestoreRecyclerAdapter<MenuItem, MenuItemAdapter.MenuItemHolder> {

    private OnItemClickListener listener;

    public MenuItemAdapter(@NonNull FirestoreRecyclerOptions<MenuItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuItemHolder holder, int position, @NonNull MenuItem model) {
        holder.itemDescription.setText(model.getDescription());
        holder.itemName.setText(model.getName());
        holder.price.setText("£" + model.getPrice().toString());

        if(!model.getStock()){
            holder.stock.setVisibility(View.VISIBLE);
            holder.itemDescription.setVisibility(View.GONE);
        } else {
            holder.stock.setVisibility(View.GONE);
            holder.itemDescription.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_items, parent, false);
        return new MenuItemHolder(v);

    }

    class MenuItemHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription, price, stock;

        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.TxtView_item1);
            itemDescription = itemView.findViewById(R.id.TxtView_item2);
            price = itemView.findViewById(R.id.PriceTxt);
            stock = itemView.findViewById(R.id.OutofStock);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
