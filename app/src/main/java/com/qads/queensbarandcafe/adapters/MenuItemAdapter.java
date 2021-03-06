package com.qads.queensbarandcafe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.models.MenuModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuItemAdapter extends FirestoreRecyclerAdapter<MenuModel, MenuItemAdapter.MenuItemHolder> {

    private OnItemClickListener listener;

    public MenuItemAdapter(@NonNull FirestoreRecyclerOptions<MenuModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuItemHolder holder, int position, @NonNull MenuModel model) {
        holder.itemDescription.setText(model.getDescription());
        holder.itemName.setText(model.getName());
        String priceString = String.format("%.2f", model.getPrice());
        String pricePounds= "£" + priceString;
        holder.price.setText(pricePounds);

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
