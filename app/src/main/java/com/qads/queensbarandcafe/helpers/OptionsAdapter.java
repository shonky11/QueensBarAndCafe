package com.qads.queensbarandcafe.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qads.queensbarandcafe.R;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<OptionsModel> optionSet;
    int total_types;
    Context mContext;

    //create the two views here

    public static class NotMultipleViewHolder extends RecyclerView.ViewHolder {

        TextView optionsName;
        CardView optionsCard;

        public NotMultipleViewHolder(View itemView) {
            super(itemView);

            this.optionsName = (TextView) itemView.findViewById(R.id.options_list);
            this.optionsCard = (CardView) itemView.findViewById(R.id.options_card);
        }
    }

    public static class MultipleViewHolder extends RecyclerView.ViewHolder {

        TextView optionsName;
        CardView optionsCard;
        TextView quantity;
        TextView optionsPrice;

        public MultipleViewHolder(View itemView) {
            super(itemView);

            this.optionsName = (TextView) itemView.findViewById(R.id.txtView_item1);
            this.optionsCard = (CardView) itemView.findViewById(R.id.options_card2);
            this.quantity = (TextView) itemView.findViewById(R.id.quantity);
            this.optionsPrice = (TextView) itemView.findViewById(R.id.final_pricetxt);
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
        switch (viewType) {
            case OptionsModel.NOT_MULTIPLE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.options_item, parent, false);
                return new NotMultipleViewHolder(itemView);
            case OptionsModel.MULTIPLE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.options_item_2, parent, false);
                return new MultipleViewHolder(itemView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        /*switch (optionSet.get(position).type) {
            case 0:
                return OptionsModel.NOT_MULTIPLE;
            case 1:
                return OptionsModel.MULTIPLE;
            default:
                return -1;
        }*/

        if (optionSet.get(position).getmCanHaveMultiple().equals("false")) {
            return OptionsModel.NOT_MULTIPLE;
        } else if (optionSet.get(position).getmCanHaveMultiple().equals("true")) {
            return OptionsModel.MULTIPLE;
        }
        else return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OptionsModel object = optionSet.get(position);
        if (object != null) {
            /*switch (object.type) {
                case OptionsModel.NOT_MULTIPLE:
                    ((NotMultipleViewHolder) holder).optionsName.setText(object.getmOptionName());
                    break;
                case OptionsModel.MULTIPLE:
                    ((MultipleViewHolder) holder).optionsName.setText(object.getmOptionName());
                    ((MultipleViewHolder) holder).optionsPrice.setText(object.getmExtraPrice());
            }*/
            if (object.getmCanHaveMultiple().equals("false")) {
                ((NotMultipleViewHolder) holder).optionsName.setText(object.getmOptionName());
            } else if (object.getmCanHaveMultiple().equals("true")) {
                ((MultipleViewHolder) holder).optionsName.setText(object.getmOptionName());
                ((MultipleViewHolder) holder).optionsPrice.setText(object.getmExtraPrice());
            }

        }
    }

    @Override
    public int getItemCount() {
        return optionSet.size();
    }
}
