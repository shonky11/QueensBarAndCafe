package com.qads.queensbarandcafe.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.helpers.MenuItem;
import com.qads.queensbarandcafe.helpers.MenuItemAdapter;
import com.qads.queensbarandcafe.helpers.OptionsAdapter;
import com.qads.queensbarandcafe.helpers.OptionsModel;

import java.util.ArrayList;

public class ExpandedItemsFragment extends Fragment{
    private RecyclerView optionsView;
    private View rootView;
    private OptionsAdapter OptionsAdapter;
    private String id;
    private MenuItemAdapter adapter;
    private MenuItem menuItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference menuItemRef = db.collection("menuitems");
    private DocumentReference itemRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.expanded_items_fragment, container, false);
        Bundle bundle = this.getArguments();

        final ArrayList<OptionsModel> list= new ArrayList(); //input all the lists of dictionaries
        id = bundle.getString("Current MenuItem"); //this is the id of the menuitem



        itemRef = menuItemRef.document(id);

        itemRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value != null && value.exists()) {
                    menuItem = value.toObject(MenuItem.class);
                    Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < 2; i++){
                        Boolean can_have_multiple = (Boolean) menuItem.getOptionsList().get(i).get("can_have_multiple");
                        Long extra_price = (Long) menuItem.getOptionsList().get(i).get("extra_price");
                        String name = (String) menuItem.getOptionsList().get(i).get("name");

                        list.add(new OptionsModel(can_have_multiple, extra_price, name));

                        /*Log.d("CANHAVEMULTIPLE", can_have_multiple.toString());
                        Log.d("EXTRAPRICE", extra_price.toString());
                        Log.d("NAME", name);*/

                    }

                    //Toast.makeText(getContext(), name.toString(), Toast.LENGTH_SHORT).show();
                    //MenuItem clicked = (MenuItem) adapter.getItem(0); //e.g whole latte documentå
                    //Toast.makeText(getContext(), "Position: " + 0 + " ID: " + clicked.getOptionsList().get(0).get("can_have_multiple"), Toast.LENGTH_SHORT).show();
                }
            }
        });

        OptionsAdapter adapter = new OptionsAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.options_recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        Log.d("LIST", list.toString()); //LIST IS EMPTY

        //list.add(new OptionsModel(canHaveMultiple,extraPrice,optionName));
        return rootView;
    }


}
