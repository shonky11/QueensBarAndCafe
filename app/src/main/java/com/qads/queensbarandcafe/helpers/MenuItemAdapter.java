package com.qads.queensbarandcafe.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.qads.queensbarandcafe.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuItemAdapter extends FirestoreRecyclerAdapter<MenuItem, MenuItemAdapter.MenuItemHolder> {

    public MenuItemAdapter(@NonNull FirestoreRecyclerOptions<MenuItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuItemHolder holder, int position, @NonNull MenuItem model) {
        holder.itemDescription.setText(model.getDescription());
        holder.itemName.setText(model.getName());
        holder.price.setText(model.getPrice().toString());

    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_items, parent, false);
        return new MenuItemHolder(v);

    }

    class MenuItemHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription, price;

        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.TxtView_item1);
            itemDescription = itemView.findViewById(R.id.TxtView_item2);
            price = itemView.findViewById(R.id.PriceTxt);

        }
    }
}
