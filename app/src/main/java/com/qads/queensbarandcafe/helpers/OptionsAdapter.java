package com.qads.queensbarandcafe.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.qads.queensbarandcafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder>{

    private ArrayList<OptionsModel> optionsModel;
    private OnNoteListener mOnNoteListener; //this sets the onNoteListener to the viewholder
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String itemid = FirebaseAuth.getInstance().getUid();
    private DocumentReference docRef = db.collection("menuitems").document(itemid);
    Map<String, String> options = new HashMap<String, String>();

    public OptionsAdapter(ArrayList<OptionsModel> data, OnNoteListener onNoteListener){
        this.optionsModel = data;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public OptionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.options_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view, mOnNoteListener);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull OptionsAdapter.MyViewHolder holder, int position) {

        TextView options_text = holder.options_text;
        options_text.setText(optionsModel.get(position).getOptions().get(options));

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return optionsModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView options_text;
        CardView options_card;
        CheckBox checkBox;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.options_text = (TextView) itemView.findViewById(R.id.options_list);
            this.options_card = (CardView) itemView.findViewById(R.id.options_card);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox1);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition(), options_card, options_text);
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position, CardView cardView, TextView textView); //will send the position of the clicked item
    }

}
