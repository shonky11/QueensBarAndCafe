package com.qads.queensbarandcafe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.activities.MainActivity;
import com.qads.queensbarandcafe.helpers.MenuItem;
import com.qads.queensbarandcafe.helpers.MenuItemAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

        adapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                onCategoryClickmeister(id);
                MenuItem clicked = (MenuItem) adapter.getItem(position); //e.g whole latte documentå
                //Toast.makeText(getContext(), "Position: " + position + " ID: " + clicked.getOptionsList().get(1).get("name"), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onCategoryClickmeister(String id){

        Fragment nextFragment = new ItemsFragment(); //change this to expanded fragment name
        Bundle bundle = new Bundle();
        bundle.putString("Current MenuItem", id); //the key is the "Current MenuItem
        nextFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, nextFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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