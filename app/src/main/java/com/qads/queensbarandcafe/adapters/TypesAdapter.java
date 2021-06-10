package com.qads.queensbarandcafe.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.Distribution;
import com.google.gson.TypeAdapter;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.models.TypesModel;

import java.util.List;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.ViewHolder> {

    private List<TypesModel> typeList;
    private Context context;
    private OnTypeSelectedListener mTypeListener;

    public interface OnTypeSelectedListener {
        void OnOptionSelected(String name, Number price, int position, int place, Boolean checked);
    }

    public void setOnTypeListener(TypesAdapter.OnTypeSelectedListener listener){
        mTypeListener = listener;
    }

    public TypesAdapter(List<TypesModel> TypesList, Context ctx) {
        typeList = TypesList;
        context = ctx;
    }

    @Override
    public TypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item, parent, false);
        context = parent.getContext();
        TypesAdapter.ViewHolder viewHolder = new TypesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(TypesAdapter.ViewHolder holder, final int position) {
        TypesModel typesModel = typeList.get(position);
        holder.typesName.setText("Please Select " + typesModel.getTypeTitle());
        holder.typesGroup.removeAllViews();
        int id = (position+1)*100;
        for(int i = 0; i < typesModel.getTypesList().size(); i++){

            final String name = (String) typesModel.getTypesList().get(i).get(0);
            final Number price = ((Number) typesModel.getTypesList().get(i).get(1)).doubleValue();
            final Boolean checked = (Boolean) typesModel.getTypesList().get(i).get(2);
            final int finalI = i;

            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lparams);

            String priceString = String.format("%.2f", price);
            String pricePounds= "£" + priceString;

            TableRow.LayoutParams paramsExample = new TableRow.LayoutParams(0 ,ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            paramsExample.setMargins((int) context.getResources().getDimension(R.dimen.marginboi),0,(int) context.getResources().getDimension(R.dimen.marginboiRight),0);

            TextView tv = new TextView(TypesAdapter.this.context);
            tv.setTextAppearance(context, R.attr.textAppearanceListItemSmall);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            tv.setMinimumHeight(180);
            tv.setLayoutParams(paramsExample);
            tv.setText(name);
            tv.setTextSize(16);

            AppCompatRadioButton rb = new AppCompatRadioButton(TypesAdapter.this.context);
            rb.setId(id++);

//
//            String priceString = String.format("%.2f", price);
//            String pricePounds= "<font color = #bfbfbf>  (+£" + priceString + ") </font>";
//            String nameHtml = "<font color = #000000>" + name + "</font>";
            rb.setText(pricePounds);
            rb.setClickable(false);
            rb.setTextColor(ColorStateList.valueOf(Color.parseColor("#BFBFBF")));
            rb.setPadding(0, 0, (int) context.getResources().getDimension(R.dimen.marginboiRight), 0);
            rb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#014A04")));
            rb.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            rb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            rb.setMinimumHeight(180);
            rb.setTypeface(rb.getTypeface(), Typeface.BOLD);
            RadioGroup.LayoutParams rgLPara = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgLPara.setMargins(0, 0, (int) context.getResources().getDimension(R.dimen.marginboi), 0);
            rb.setLayoutParams(rgLPara);
            rb.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            rb.setChecked(checked);

            ll.addView(tv);
            ll.addView(rb);

            ll.setOnClickListener(new LinearLayout.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mTypeListener.OnOptionSelected(name, price, position, finalI, checked);
                }
            });

            holder.typesGroup.addView(ll);
        }
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView typesName;
        public LinearLayout typesGroup;

        public ViewHolder(View view) {
            super(view);
            typesName = (TextView) view.findViewById(R.id.types_name);
            typesGroup = (LinearLayout) view.findViewById(R.id.types_group);

//            typesGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                    mTypeListener.OnOptionSelected(radioGroup, i);
//                }
//            });
        }
    }
}
