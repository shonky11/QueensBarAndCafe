package com.qads.queensbarandcafe.helpers;

import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.qads.queensbarandcafe.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//public class MenuItemAdapter extends FirestoreRecyclerAdapter {

    class MenuItemHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemDescription, price;

        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
          //  itemName = itemView.findViewById(R.id.)
        }
    }
//}
