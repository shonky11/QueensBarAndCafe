package com.qads.queensbarandcafe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.activities.MainActivity;
import com.qads.queensbarandcafe.adapters.AllergiesAdapter;
import com.qads.queensbarandcafe.adapters.TypesAdapter;
import com.qads.queensbarandcafe.models.AllergiesModel;
import com.qads.queensbarandcafe.models.MenuModel;
import com.qads.queensbarandcafe.adapters.OptionsAdapter;
import com.qads.queensbarandcafe.models.OptionsModel;
import com.qads.queensbarandcafe.models.TypesModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandedItemsFragment extends Fragment{
    private RecyclerView optionsView;
    private RecyclerView typesView;
    private View rootView;
    private OptionsAdapter OptionsAdapter;
    private List<TypesModel> typesList = new ArrayList<>();
    private TypesAdapter typesAdapter = new TypesAdapter(typesList, getContext());
    private String id;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference menuItemRef = db.collection("menuitems");
    private DocumentReference itemRef;
    private ArrayList<OptionsModel> optionsList = new ArrayList<>();
    private ArrayList<AllergiesModel> allergiesList = new ArrayList<>();
    private OptionsAdapter optionsAdapter = new OptionsAdapter(optionsList);
    private AllergiesAdapter allergiesAdapter = new AllergiesAdapter(allergiesList);
    private TextView itemName, itemDesc;
    private Button cartButton;
    private TextView allergiesButton;
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
    private Map<String, Object> outputAllergies = new HashMap<>();
    private Double totalPrice = 0.00;
    private Double typesPrice = 0.00;
    private SwipeRefreshLayout swipeContainer;
    private TextView noOps;
    private TextView allergiesLister;
    private TextView itemAllergies;
    private Map<String, Map<String, Object>> itemTypes = new HashMap<String, Map<String, Object>>();
    private Map<String, Object> itemTypesSub = new HashMap<String, Object>();
    private Boolean allergiesExpanded = false;
    private List<String> allergiesSelected = new ArrayList<String>();
    private EditText allergiesEntered;
    private Map<String, Map<String, Object>> outputTypesMap = new HashMap<String, Map<String, Object>>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.expanded_items_fragment, container, false);
        Bundle bundle = this.getArguments();
        id = bundle.getString("Current MenuItem");
        itemRef = menuItemRef.document(id);
        optionsList.clear();
        typesList.clear();
        allergiesList.clear();

        optionsAdapter.notifyDataSetChanged();
        allergiesAdapter.notifyDataSetChanged();
        typesAdapter.notifyDataSetChanged();

        LinearLayoutManager optionsLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        LinearLayoutManager allergiesLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        LinearLayoutManager typesLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        final RecyclerView optionsView = (RecyclerView) rootView.findViewById(R.id.options_recyclerView);
        final RecyclerView allergiesView = (RecyclerView) rootView.findViewById(R.id.allergies_recyclerView);
        final RecyclerView typesView = (RecyclerView) rootView.findViewById(R.id.types_recyclerView);

        optionsView.setLayoutManager(optionsLayoutManager);
        optionsView.setItemAnimator(new DefaultItemAnimator());
        optionsView.setAdapter(optionsAdapter);

        allergiesView.setLayoutManager(allergiesLayoutManager);
        allergiesView.setItemAnimator(new DefaultItemAnimator());
        allergiesView.setAdapter(allergiesAdapter);

        typesView.setLayoutManager(typesLayoutManager);
        typesView.setItemAnimator(new DefaultItemAnimator());
        typesView.setAdapter(typesAdapter);

        itemName = rootView.findViewById(R.id.item_name);
        itemDesc = rootView.findViewById(R.id.item_description);
        itemQuantDisp = rootView.findViewById(R.id.itemQuantity);
        itemInc = rootView.findViewById(R.id.item_plus);
        itemDec = rootView.findViewById(R.id.item_minus);
        bigpic = rootView.findViewById(R.id.expandedPic);
        noOps = rootView.findViewById(R.id.noops);
        itemAllergies = rootView.findViewById(R.id.item_allergens);
        allergiesEntered = rootView.findViewById(R.id.dietary);

        optionsAdapter.setOnPlusClickListener(new OptionsAdapter.OnPlusClickListener() {
            @Override
            public void onPlusClick(View v, int position) {
                increment(v, position);
            }
        });

        optionsAdapter.setOnMinusClickListener(new OptionsAdapter.OnMinusClickListener() {
            @Override
            public void onMinusClick(View v, int position) {
                decrement(v, position);
            }
        });

        optionsAdapter.setOnCheckClickListener(new OptionsAdapter.onCheckClickListener() {
            @Override
            public void onCheckClick(View v, int position) {
                optionsCheck(v, position);
            }
        });

        allergiesAdapter.setOnAllergiesClickListener(new AllergiesAdapter.onAllergiesClickListener() {
            @Override
            public void onAllergiesClick(View v, int position) {
                allergyCheck(v, position);
            }
        });

        typesAdapter.setOnTypeListener(new TypesAdapter.OnTypeSelectedListener() {
            @Override
            public void OnOptionSelected(String name, Number price, int position, int place, Boolean checked) {
                typesChecker(name, price, position, place, checked);
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

        allergiesButton = (TextView) rootView.findViewById(R.id.allergies_adder);
        allergiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allergiesExpanded) {
                    allergiesView.setVisibility(View.GONE);
                    allergiesEntered.setVisibility(View.GONE);
                    allergiesLister.setVisibility(View.VISIBLE);
                    allergiesButton.setText("+ Add Allergies");
                    allergiesDisplayer();
                    allergiesExpanded = false;
                } else {
                    allergiesView.setVisibility(View.VISIBLE);
                    allergiesEntered.setVisibility(View.VISIBLE);
                    allergiesLister.setVisibility(View.GONE);
                    allergiesButton.setText("Done");
                    allergiesExpanded = true;
                }
            }
        });

        allergiesLister = (TextView) rootView.findViewById(R.id.allergies_lister);

        cartButton = (Button) rootView.findViewById(R.id.add_to_cart);


        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputID = id;
                int iter2 = optionsList.size();
                for (int i = 0; i < iter2; i++) {
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.put("name", optionsList.get(i).getOptionName());
                    tempMap.put("quantity", optionsList.get(i).getQuantity());
                    tempMap.put("price", optionsList.get(i).getTruePrice());
                    outputOptions.put(optionsList.get(i).getOptionName(), tempMap);
                }
                outputMap.put("id", outputID);
                outputMap.put("name", outputName);
                outputMap.put("options", outputOptions);
                outputMap.put("types", outputTypesMap);
                outputMap.put("allergies", allergiesSelected);
                outputMap.put("price", totalPrice);
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

                if (outputLocation.equals("Buttery")) {
                    for (int j = 0; j < itemQuantity; j++) {
                        MainActivity.butteryCart.add(outputMap);
                        MainActivity.butteryPrices.add(outputPrices);
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
                        optionsList.clear();;
                        MenuModel menuItem = document.toObject(MenuModel.class);
                        itemPriceFinal = menuItem.getPrice();
                        outputName = menuItem.getName();
                        outputLocation = menuItem.getLocation();

                        buttonSetter();
                        itemName.setText(menuItem.getName());
                        itemDesc.setText(menuItem.getDescription());
                        String tempAllergens = "Contains: ";
                        for (int i = 0; i < menuItem.getAllergens().size() - 1; i++){
                            tempAllergens = tempAllergens + menuItem.getAllergens().get(i) + ", ";
                        }
                        tempAllergens = tempAllergens + "and " + menuItem.getAllergens().get(menuItem.getAllergens().size() - 1);

                        itemAllergies.setText(tempAllergens);

                        FirebaseStorage eventStorage = FirebaseStorage.getInstance();
                        StorageReference eventStorageRef = eventStorage.getReference();
                        StorageReference eventPathReference = eventStorageRef.child("menuitems/" + document.getId() + ".jpg");
                        Glide.with(getContext()).load(eventPathReference).placeholder(R.drawable.placeholder).fitCenter().into(bigpic);

                        itemTypes = menuItem.getTypes();
                        List<String> namesList = new ArrayList<>(itemTypes.keySet());


                        int itertypes = namesList.size();

                        if (itertypes != 0) {
                            for (int i = 0; i < itertypes; i++){
                                Map<String, Object> itemTypesMap = (Map<String, Object>) itemTypes.get(namesList.get(i));

                                List<List<Object>> itemTypesList = new ArrayList<List<Object>>();

                                for (String name : itemTypesMap.keySet()) {
                                    List<Object> type = new ArrayList<Object>();
                                    type.add(0, name);
                                    type.add(1, (Number) itemTypesMap.get(name));
                                    type.add(2, false);
                                    itemTypesList.add(type);
                                }


                                typesList.add(new TypesModel(namesList.get(i), itemTypesList, false));
                            }
                            typesAdapter.notifyDataSetChanged();
                        }else if (itertypes == 0){
                            typesView.setVisibility(View.GONE);
                        }

                        int iterops = menuItem.getOptionsList().size();

                        if (iterops != 0) {
                            optionsView.setVisibility(View.VISIBLE);
                            noOps.setVisibility(View.GONE);
                            for (int i = 0; i < iterops; i++){
                                Map<String, Object> optionLoad= (Map<String, Object>) menuItem.getOptionsList().get(i);
                                Boolean can_have_multiple = (Boolean) optionLoad.get("can_have_multiple");
                                Number extra_price = (Number) optionLoad.get("extra_price");
                                String name = (String) optionLoad.get("name");

                                optionsList.add(new OptionsModel(can_have_multiple, extra_price, name));
                            }
                            optionsAdapter.notifyDataSetChanged();
                        }else if (iterops == 0){
                            optionsView.setVisibility(View.GONE);
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

        final DocumentReference allergiesRef = db.collection("settings").document("allergies");
        allergiesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot document = task.getResult();
                     if (document.exists()) {
                         allergiesList.clear();
                         ArrayList<String> tempAllergiesList = (ArrayList<String>) document.get("allergies");
                         for (int i = 0; i < tempAllergiesList.size(); i++){
                             AllergiesModel tempAllergy = new AllergiesModel(tempAllergiesList.get(i), false);
                             allergiesList.add(tempAllergy);
                         }
                         allergiesAdapter.notifyDataSetChanged();
                     }
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
        if(optionsList.get(position).getQuantity() < 10) {
            optionsList.get(position).setQuantity(optionsList.get(position).getQuantity() + 1);
            int currentQuant = optionsList.get(position).getQuantity();
            double itemPrice = optionsList.get(position).getExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            optionsList.get(position).setTruePrice(total);
            optionsAdapter.notifyItemChanged(position);
            buttonSetter();
        } else {
            Toast.makeText(getContext(), "Maximum reached", Toast.LENGTH_SHORT).show();
        }
    }

    private void decrement(View v, int position){
        if(optionsList.get(position).getQuantity() > 0) {
            optionsList.get(position).setQuantity(optionsList.get(position).getQuantity() - 1);
            int currentQuant = optionsList.get(position).getQuantity();
            double itemPrice = optionsList.get(position).getExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            optionsList.get(position).setTruePrice(total);
            optionsAdapter.notifyItemChanged(position);
            buttonSetter();
        }
    }

    private void optionsCheck(View v, int position){
        if(optionsList.get(position).getQuantity() > 0) {
            optionsList.get(position).setQuantity(0);
            int currentQuant = optionsList.get(position).getQuantity();
            double itemPrice = optionsList.get(position).getExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            optionsList.get(position).setTruePrice(total);
            optionsAdapter.notifyItemChanged(position);
            buttonSetter();
        } else {
            optionsList.get(position).setQuantity(1);
            int currentQuant = optionsList.get(position).getQuantity();
            double itemPrice = optionsList.get(position).getExtraPrice().doubleValue();
            double total = itemPrice * currentQuant;
            optionsList.get(position).setTruePrice(total);
            optionsAdapter.notifyItemChanged(position);
            buttonSetter();
        }
    }

    private void allergyCheck(View v, int position){

        if(allergiesList.get(position).getSelected()) {
            allergiesList.get(position).setSelected(false);
            allergiesAdapter.notifyItemChanged(position);
        } else {
            allergiesList.get(position).setSelected(true);
            allergiesAdapter.notifyItemChanged(position);
        }
    }

    void allergiesDisplayer(){

        allergiesSelected.clear();

        if(!allergiesEntered.getText().toString().equals("") && allergiesEntered.getText() != null) {
            allergiesSelected.add(allergiesEntered.getText().toString());
        }

        if(allergiesList.size() != 0 && allergiesList != null) {
            for (int i = 0; i < allergiesList.size(); i++) {
                AllergiesModel allergyTemp = allergiesList.get(i);
                if (allergyTemp.getSelected()) {
                    allergiesSelected.add(allergyTemp.getAllergy());
                }
            }
        }

        String tempString = "";

        if(allergiesSelected.size() != 0 && allergiesSelected != null) {
            for (int i = 0; i < allergiesSelected.size() - 1; i++) {
                tempString = tempString + allergiesSelected.get(i) + ", ";
            }

            tempString = tempString + allergiesSelected.get(allergiesSelected.size() - 1);
        } else {
            tempString = "No Allergies Selected";
        }

        allergiesLister.setText(tempString);

    }

    private void typesChecker(String name, Number price, int position, int place, Boolean checked){
        TypesModel tempType = typesList.get(position);
        List<List<Object>> tempTypesList = new ArrayList<>();
        tempTypesList = tempType.getTypesList();
        for (int i = 0; i < tempTypesList.size(); i++){
            tempTypesList.get(i).set(2, false);
        }
        tempTypesList.get(place).set(2, true);
        tempType.setTypesList(tempTypesList);
        tempType.setTypeChecked(true);
        typesList.remove(position);
        typesList.add(position, tempType);
        typesAdapter.notifyItemChanged(position);
        typesAdder();
        buttonSetter();
    }

    private void typesAdder(){
        outputTypesMap.clear();
        typesPrice = 0.00;

        if(typesList.size() != 0 && typesList != null) {
            for (int i = 0; i < typesList.size(); i++) {
                TypesModel tempType = typesList.get(i);
                Map<String, Object> subTypeMap = new HashMap<String, Object>();
                if (tempType.getTypeChecked()) {
                    for(List<Object> subType : tempType.getTypesList()){
                        if((Boolean) subType.get(2)){
                            subTypeMap.put("name", (String) subType.get(0));
                            subTypeMap.put("price", (Number) subType.get(1));
                            typesPrice = typesPrice + ((Number) subType.get(1)).doubleValue();
                        }
                    }
                }
                outputTypesMap.put(tempType.getTypeTitle(), subTypeMap);
            }
        }
    }

    private void buttonSetter(){
        Double optionsTotal = 0.00;

        for(OptionsModel optionTemp : optionsList){
            optionsTotal = optionsTotal + optionTemp.getTruePrice().doubleValue();
        }

        Integer itemQuantitee = itemQuantity;
        itemQuantDisp.setText(itemQuantitee.toString());
        totalPrice = itemPriceFinal + optionsTotal + typesPrice;
        Double totalAdded = totalPrice * itemQuantity;
        String priceString = String.format("%.2f", totalAdded);
        String pricePounds= "ADD TO CART:       " + "+£" + priceString;

        cartButton.setText(pricePounds);
    }
}
