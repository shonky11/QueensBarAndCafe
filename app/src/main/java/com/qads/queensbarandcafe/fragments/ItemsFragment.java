package com.qads.queensbarandcafe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.helpers.MenuItem;
import com.qads.queensbarandcafe.helpers.MenuItemAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsFragment extends Fragment {
    private View rootView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference menuItemRef = db.collection("menuitems");
    private MenuItemAdapter adapter;
    private String category;
    private String location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        rootView = inflater.inflate(R.layout.items_fragment, container, false);
        category = bundle.getString("Current Category");
        location = bundle.getString("Current Location");
        setUpRecyclerView();
        return rootView;

    }

    private void setUpRecyclerView() {

        Query query = menuItemRef.whereEqualTo("location", location).whereEqualTo("category", category).whereEqualTo("stock", true).orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<MenuItem> options = new FirestoreRecyclerOptions.Builder<MenuItem>()
                .setQuery(query, MenuItem.class).build();

        adapter = new MenuItemAdapter(options);
        RecyclerView recyclerView = rootView.findViewById(R.id.items_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}