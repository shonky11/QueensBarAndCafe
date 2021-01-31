package com.qads.queensbarandcafe.helpers;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import okio.Options;

import com.qads.queensbarandcafe.R;

import java.util.ArrayList;
import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<OptionsModel> optionSet;
    int total_types;
    Context mContext;
    private OnMinusClickListener mMinusClickListener;
    private OnPlusClickListener mPlusClickListener;
    private onCheckClickListener mCheckClickListener;

    public interface OnPlusClickListener {
        void onPlusClick(View v, int position);
    }

    public interface OnMinusClickListener {
        void onMinusClick(View v, int position);
    }

    public interface onCheckClickListener {
        void onCheckClick(View v, int position);
    }

    public void setOnPlusClickListener(OptionsAdapter.OnPlusClickListener listener) {
        mPlusClickListener = listener;
    }

    public void setOnMinusClickListener(OptionsAdapter.OnMinusClickListener listener) {
        mMinusClickListener = listener;
    }

    public void setOnCheckClickListener(OptionsAdapter.onCheckClickListener listener) {
        mCheckClickListener = listener;
    }


    public class NotMultipleViewHolder extends RecyclerView.ViewHolder {

        TextView optionsName;
        TextView optionsPrice;
        CheckBox checker;

        public NotMultipleViewHolder(final View itemView) {
            super(itemView);

            this.optionsName = (TextView) itemView.findViewById(R.id.options_list);
            this.optionsPrice = (TextView) itemView.findViewById(R.id.final_pricetxt);
            this.checker = (CheckBox) itemView.findViewById(R.id.checkbox);

            checker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCheckClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mCheckClickListener.onCheckClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    public class MultipleViewHolder extends RecyclerView.ViewHolder {

        TextView optionsName;
        TextView quantity;
        TextView optionsPrice;
        ImageView minus, plus;

        public MultipleViewHolder(final View itemView) {
            super(itemView);

            this.optionsName = (TextView) itemView.findViewById(R.id.txtView_item1);
            this.quantity = (TextView) itemView.findViewById(R.id.quantity);
            this.optionsPrice = (TextView) itemView.findViewById(R.id.final_pricetxt);
            this.minus = (ImageView) itemView.findViewById(R.id.btn_minus);
            this.plus = (ImageView) itemView.findViewById(R.id.btn_plus);

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlusClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mPlusClickListener.onPlusClick(itemView, position);
                        }
                    }
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMinusClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mMinusClickListener.onMinusClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    public OptionsAdapter(ArrayList<OptionsModel>data){
        this.optionSet = data;
        total_types = optionSet.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case OptionsModel.NOT_MULTIPLE:
                itemView = inflater.inflate(R.layout.options_item, parent, false);
                OptionsAdapter.NotMultipleViewHolder nmViewHolder = new OptionsAdapter.NotMultipleViewHolder(itemView);
                return nmViewHolder;
            case OptionsModel.MULTIPLE:
                itemView = inflater.inflate(R.layout.options_item_2, parent, false);
                OptionsAdapter.MultipleViewHolder mViewHolder = new OptionsAdapter.MultipleViewHolder(itemView);
                return mViewHolder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Boolean test = optionSet.get(position).getmCanHaveMultiple();
        if (!test) {
            return OptionsModel.NOT_MULTIPLE;
        } else if (test) {
            return OptionsModel.MULTIPLE;
        }
        else return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OptionsModel option = optionSet.get(position);
        Number extra = option.getmTruePrice();
        Number price = extra.doubleValue() / 100;
        String priceString = String.format("%.2f", price);
        String pricePounds= "+£" + priceString;
        switch(holder.getItemViewType()) {
            case OptionsModel.NOT_MULTIPLE:
                ((NotMultipleViewHolder) holder).optionsName.setText(option.getmOptionName());
                ((NotMultipleViewHolder) holder).optionsPrice.setText(pricePounds);
                Boolean checkeroo;
                if (option.getmQuantity() > 0){
                    checkeroo = true;
                }else{
                    checkeroo = false;
                }
                ((NotMultipleViewHolder) holder).checker.setChecked(checkeroo);
                break;
            case OptionsModel.MULTIPLE:
                ((MultipleViewHolder) holder).optionsName.setText(option.getmOptionName());
                ((MultipleViewHolder) holder).optionsPrice.setText(pricePounds);
                Integer quantint = option.getmQuantity();
                ((MultipleViewHolder) holder).quantity.setText(quantint.toString());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return optionSet.size();
    }
}
