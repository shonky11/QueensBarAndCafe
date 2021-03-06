package com.qads.queensbarandcafe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.models.CartModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter {
    private ArrayList<CartModel> cartSet;
    private int total_types;
    private OnDeleteClickListener mDeleteClickListener;
    private OnItemClickListener mItemClickListener;

    public CartAdapter(ArrayList<CartModel> list) {
        this.cartSet = list;
        total_types = cartSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(View v, int position);
    }

    public void setOnDeleteClickListener(CartAdapter.OnDeleteClickListener deleteListener) {
        mDeleteClickListener = deleteListener;
    }

    public void setOnItemClickListener(CartAdapter.OnItemClickListener itemListener){
        mItemClickListener = itemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        itemView = inflater.inflate(R.layout.cart_item, parent, false);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartModel cartItem = cartSet.get(position);
        ((ViewHolder) holder).cartName.setText(cartItem.getItemName());
        ((ViewHolder) holder).cartPrice.setText(cartItem.getItemPrice());
        ((ViewHolder) holder).cartDesc.setText(cartItem.getItemDescription());

    }

    @Override
    public int getItemCount() {
        return cartSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cartName, cartPrice, cartDesc;
        ImageView itemDelete;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.itemDelete = (ImageView) itemView.findViewById(R.id.delete);
            this.cartName = (TextView) itemView.findViewById(R.id.name);
            this.cartPrice = (TextView) itemView.findViewById(R.id.price);
            this.cartDesc = (TextView) itemView.findViewById(R.id.description);

            this.itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDeleteClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mDeleteClickListener.onDeleteClick(itemView, position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mItemClickListener.onItemClick(itemView, position);
                    }
                }
            });
        }
    }
}
