package com.qads.queensbarandcafe.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.qads.queensbarandcafe.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> mCategory;
    private OnCategoryClickListener mCategoryListener;

    public CategoryAdapter(List<Category> categories) {
        mCategory = categories;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(View v, int position);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        mCategoryListener = listener;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.category_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = mCategory.get(position);
        TextView textView = holder.categoryName;
        textView.setText(category.getName());
        ImageView imageView = holder.categoryPic;
        BaseRequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);

        FirebaseStorage eventStorage = FirebaseStorage.getInstance();
        StorageReference eventStorageRef = eventStorage.getReference();
        StorageReference eventPathReference = eventStorageRef.child("categories/" + category.getImage());
        Glide.with(holder.itemView.getContext()).load(eventPathReference).apply(requestOptions).fitCenter().into(holder.categoryPic);
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        public ImageView categoryPic;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            categoryPic = (ImageView) itemView.findViewById(R.id.category_picture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCategoryListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mCategoryListener.onCategoryClick(v, position);
                        }
                    }
                }
            });
        }
    }
}