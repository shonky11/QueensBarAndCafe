package com.qads.queensbarandcafe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.helpers.OptionsAdapter;
import com.qads.queensbarandcafe.helpers.OptionsModel;

import java.util.ArrayList;

public class ExpandedItemsFragment extends Fragment implements OptionsAdapter.OnNoteListener {

    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView recyclerView;
    private ArrayList<OptionsModel> optionsModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.expanded_items_fragment, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.options_list);
        recyclerView.setHasFixedSize(true);




        return v;
    }

    @Override
    public void onNoteClick(int position, CardView cardView, TextView textView) {

    }
}
