package com.qads.queensbarandcafe.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        textView.setText(category.getCategory());
        ImageView imageView = holder.categoryPic;
        imageView.setImageResource(category.getImageRef());
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