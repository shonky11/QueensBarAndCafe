package com.qads.queensbarandcafe.fragments;

import android.annotation.SuppressLint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.activities.MainActivity;
import com.qads.queensbarandcafe.helpers.Category;
import com.qads.queensbarandcafe.helpers.CategoryAdapter;
import com.qads.queensbarandcafe.helpers.MenuItem;
import com.qads.queensbarandcafe.helpers.MenuItemAdapter;
import com.qads.queensbarandcafe.helpers.OptionsAdapter;
import com.qads.queensbarandcafe.helpers.OptionsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpandedItemsFragment extends Fragment{
    private RecyclerView optionsView;
    private View rootView;
    private OptionsAdapter OptionsAdapter;
    private String id;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference menuItemRef = db.collection("menuitems");
    private DocumentReference itemRef;
    private ArrayList<OptionsModel> list= new ArrayList<>();
    private OptionsAdapter adapter = new OptionsAdapter(list);
    private TextView itemName, itemDesc;
    private Button cartButton;
    private Double itemPriceFinal = 0.00;
    private int itemQuantity = 1;
    private ImageView cartImageButton;
    private TextView itemQuantDisp;
    private ImageView itemInc, itemDec, bigpic;
    private Map<String, Object> outputMap = new HashMap<>();
    private Map<String, Object> outputOptions = new HashMap<>();
    private Map<String, Object> outputPrices = new HashMap<>();
    private String outputID;
    private String outputName;
    private String outputLocation;
    private Double totalPrice = 0.00;
    private SwipeRefreshLayout swipeContainer;
    private TextView noOps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.expanded_items_fragment, container, false);
        Bundle bundle = this.getArguments();
        id = bundle.getString("Current MenuItem");
        itemRef = menuItemRef.document(id);
        list.clear();
        adapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        final RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.options_recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        itemName = rootView.findViewById(R.id.ex_event_name_text_view);
        itemDesc = rootView.findViewById(R.id.ex_event_description_text_view);
        itemQuantDisp = rootView.findViewById(R.id.itemQuantity);
        itemInc = rootView.findViewById(R.id.item_plus);
        itemDec = rootView.findViewById(R.id.item_minus);
        bigpic = rootView.findViewById(R.id.expandedPic);
        noOps = rootView.findViewById(R.id.noops);

        adapter.setOnPlusClickListener(new OptionsAdapter.OnPlusClickListener() {
            @Override
            public void onPlusClick(View v, int position) {
                increment(v, position);
            }
        });

        adapter.setOnMinusClickListener(new OptionsAdapter.OnMinusClickListener() {
            @Override
            public void onMinusClick(View v, int position) {
                decrement(v, position);
            }
        });

        adapter.setOnCheckClickListener(new OptionsAdapter.onCheckClickListener() {
            @Override
            public void onCheckClick(View v, int position) {
                checkit(v, position);
            }
        });

        cartImageButton = (ImageView) rootView.findViewById(R.id.cart_button);
        cartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment nextFragment = new CartFragment(); //change this to expanded fragment name
                Bundle bundle = new Bundle();
                nextFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, nextFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        cartButton = (Button) rootView.findViewById(R.id.add_to_cart);


        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputID = id;
                int iter2 = list.size();
                for (int i = 0; i < iter2; i++) {
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("name", list.get(i).getmOptionName());
                    tempMap.put("quantity", list.get(i).getmQuantity());
                    outputOptions.put(list.get(i).getmOptionName(), tempMap);
                }
                outputMap.put("id", outputID);
                outputMap.put("name", outputName);
                outputMap.put("options", outputOptions);
                outputPrices.put(outputName, totalPrice);

                if (outputLocation.equals("Cafe")) {
                    for (int j = 0; j < itemQuantity; j++) {
                        MainActivity.cafeCart.add(outputMap);
                        MainActivity.cafePrices.add(outputPrices);
                    }
                }

                if (outputLocation.equals("Bar")) {
                    for (int j = 0; j < itemQuantity; j++) {
                        MainActivity.barCart.add(outputMap);
                        MainActivity.barPrices.add(outputPrices);
                    }
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                assert fragmentManager != null;
                fragmentManager.popBackStack(null, 0);
            }
        });

        itemInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemQuantity < 10) {
                    itemQuantity += 1;
                    buttonSetter();
                } else {
                    Toast.makeText(getContext(), "Maximum reached", Toast.LENGTH_SHORT).show();
                }
            }
        });

        itemDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemQuantity > 1) {
                    itemQuantity -= 1;
                    buttonSetter();
                }
            }
        });

        itemRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        list.clear();;
                        MenuItem menuItem = document.toObject(MenuItem.class);
                        itemPriceFinal = menuItem.getPrice();
                        outputName = menuItem.getName();
                        outputLocation = menuItem.getLocation();
                        buttonSetter();
                        itemName.setText(menuItem.getName());
                        itemDesc.setText(menuItem.getDescription());

                        FirebaseStorage eventStorage = FirebaseStorage.getInstance();
                        StorageReference eventStorageRef = eventStorage.getReference();
                        StorageReference eventPathReference = eventStorageRef.child("menuitems/" + menuItem.getName() + ".jpg");
                        Glide.with(getContext()).load(eventPathReference).fitCenter().into(bigpic);

                        int iterator = menuItem.getOptionsList().size();

                        if (iterator != 0) {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            noOps.setVisibility(View.GONE);
                            for (int i = 0; i < iterator; i++){
                                Map<String, Object> optionLoad= (Map<String, Object>) menuItem.getOptionsList().get(i);
                                Boolean can_have_multiple = (Boolean) optionLoad.get("can_have_multiple");
                                Number extra_price = (Number) optionLoad.get("extra_price");
                                String name = (String) optionLoad.get("name");

                                list.add(new OptionsModel(can_have_multiple, extra_price, name));
                            }
                            adapter.notifyDataSetChanged();
                        }else if (iterator == 0){
                            mRecyclerView.setVisibility(View.GONE);
                            noOps.setVisibility(View.VISIBLE);
                        }


                    } else {
                        Toast.makeText(getContext(), "This Item does not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSetter();

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void increment(View v, int position){
        if(list.get(position).getmQuantity() < 10) {
            list.get(position).setmQuantity(list.get(position).getmQuantity() + 1);
            int currentQuant = list.get(position).getmQuantity();
            double itemPrice = list.get(position).getmExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            list.get(position).setmTruePrice(total);
            adapter.notifyItemChanged(position);
            buttonSetter();
        } else {
            Toast.makeText(getContext(), "Maximum reached", Toast.LENGTH_SHORT).show();
        }
    }

    private void decrement(View v, int position){
        if(list.get(position).getmQuantity() > 0) {
            list.get(position).setmQuantity(list.get(position).getmQuantity() - 1);
            int currentQuant = list.get(position).getmQuantity();
            double itemPrice = list.get(position).getmExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            list.get(position).setmTruePrice(total);
            adapter.notifyItemChanged(position);
            buttonSetter();
        }
    }

    private void checkit(View v, int position){
        if(list.get(position).getmQuantity() > 0) {
            list.get(position).setmQuantity(0);
            int currentQuant = list.get(position).getmQuantity();
            double itemPrice = list.get(position).getmExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            list.get(position).setmTruePrice(total);
            adapter.notifyItemChanged(position);
            buttonSetter();
        } else {
            list.get(position).setmQuantity(1);
            int currentQuant = list.get(position).getmQuantity();
            double itemPrice = list.get(position).getmExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            list.get(position).setmTruePrice(total);
            adapter.notifyItemChanged(position);
            buttonSetter();
        }
    }

    private void buttonSetter(){
        Double optionsTotal = 0.00;

        for(OptionsModel optionTemp : list){
            optionsTotal = optionsTotal + optionTemp.getmTruePrice().doubleValue()/100;
        }
        Integer itemQuantitee = itemQuantity;
        itemQuantDisp.setText(itemQuantitee.toString());
        totalPrice = itemPriceFinal + optionsTotal;
        Double totalAdded = totalPrice * itemQuantity;
        String priceString = String.format("%.2f", totalAdded);
        String pricePounds= "ADD TO CART:       " + "+£" + priceString;

        cartButton.setText(pricePounds);
    }
}
