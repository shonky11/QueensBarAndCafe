package com.qads.queensbarandcafe.fragments;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.activities.MainActivity;
import com.qads.queensbarandcafe.adapters.CartAdapter;
import com.qads.queensbarandcafe.models.CartModel;
import com.qads.queensbarandcafe.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment {
    private View rootView;
    private List<Object> tempCafeCartList = new ArrayList<>();
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db;
    private List<Object> tempBarCartList = new ArrayList<>();
    private List<Object> tempCafePriceList = new ArrayList<>();
    private List<Object> tempBarPriceList = new ArrayList<>();
    private List<Object> tempButteryCartList = new ArrayList<>();
    private List<Object> tempButteryPriceList = new ArrayList<>();
    private ArrayList<CartModel> list= new ArrayList<>();
    private CartAdapter adapter = new CartAdapter(list);
    private Double cafeCartTotal = 0.00;
    private Double barCartTotal = 0.00;
    private Double butteryCartTotal = 0.00;
    private ImageView cartImageButton;
    private Button order;
    private Button next;
    private Button back;
    private LinearLayout linlay;
    private UserModel user = new UserModel();
    private String outputEmail = "";
    private String outputName = "";
    private String outputCrsid = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cart_fragment, container, false);
        final TextView name = rootView.findViewById(R.id.name);
        final TextView crsid = rootView.findViewById(R.id.crsid);
        final TextView email = rootView.findViewById(R.id.email);
        linlay = rootView.findViewById(R.id.UpayConf);
        order = rootView.findViewById(R.id.upayYes);
        next = rootView.findViewById(R.id.order);
        back = rootView.findViewById(R.id.upayNo);
        final EditText dietary = rootView.findViewById(R.id.dietary);
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        list.clear();
        adapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        final RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cart_recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

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


        adapter.setOnDeleteClickListener(new CartAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View v, int position) {
                deleteItem(position);
            }
        });

        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                expandItem(v, position);
            }
        });

        tempCafePriceList = MainActivity.cafePrices;
        tempCafeCartList = MainActivity.cafeCart;
        tempBarCartList = MainActivity.barCart;
        tempBarPriceList = MainActivity.barPrices;
        tempButteryCartList = MainActivity.butteryCart;
        tempButteryPriceList = MainActivity.butteryPrices;

        Integer size1 = tempCafeCartList.size();
        Integer size4 = tempCafePriceList.size();

        if(size1 > 0 && size4 > 0) {
            for (int i = 0; i < size1; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                Map<String, Object> tempPriceMap = new HashMap<>();
                tempMap = (Map<String, Object>) tempCafeCartList.get(i);
                tempPriceMap = (Map<String, Object>) tempCafePriceList.get(i);

                String tempName = (String) tempMap.get("name");

                Double tempPrice = (Double) tempPriceMap.get(tempName);
                String tempPriceString = String.format("%.2f", tempPrice);
                String tempPriceinPounds = "£" + tempPriceString;

                String tempDesc = "Details:" + "\n";

                Map<String, Map<String, Object>> tempAllTypes = new HashMap<String, Map<String, Object>>();
                tempAllTypes = (Map<String, Map<String, Object>>) tempMap.get("types");
                for (String key : tempAllTypes.keySet()) {
                    Map<String, Object> tempType = new HashMap<>();
                    tempType = (Map<String, Object>) tempAllTypes.get(key);
                    String tempTypeName = (String) key;
                    String tempTypeSelected = (String) tempType.get("name").toString();

                    tempDesc = tempDesc + tempTypeName + ":   " + tempTypeSelected + "\n";
                }

                Map<String, Object> tempAllOptions = new HashMap<>();
                tempAllOptions = (Map<String, Object>) tempMap.get("options");
                for (String key : tempAllOptions.keySet()) {
                    Map<String, Object> tempOption = new HashMap<>();
                    tempOption = (Map<String, Object>) tempAllOptions.get(key);
                    String tempOptionName = (String) tempOption.get("name");
                    String tempOptionQuantity = (String) tempOption.get("quantity").toString();

                    tempDesc = tempDesc + tempOptionName + ":   " + tempOptionQuantity + "\n";
                }

                tempDesc = tempDesc + "\n" + "Allergies:" + "\n";

                List<String> tempAllAllergies = new ArrayList<String>();
                tempAllAllergies = (List<String>) tempMap.get("allergies");
                for (int j = 0; j <  tempAllAllergies.size() - 1; j++){
                    tempDesc = tempDesc + tempAllAllergies.get(j) + "\n";
                }

                if(tempAllAllergies.size() != 0 && tempAllAllergies != null) {
                    tempDesc = tempDesc + tempAllAllergies.get(tempAllAllergies.size() - 1);
                } else {
                    tempDesc = tempDesc + "None";
                }

                list.add(new CartModel(tempName, tempPriceinPounds, tempDesc, "Cafe"));
                adapter.notifyDataSetChanged();
            }
        }

        Integer size2 = tempBarCartList.size();
        Integer size3 = tempBarPriceList.size();

        if(size2 > 0 && size3 > 0) {
            for (int i = 0; i < size2; i++) {

                Map<String, Object> tempMap = new HashMap<>();
                Map<String, Object> tempPriceMap = new HashMap<>();
                tempMap = (Map<String, Object>) tempBarCartList.get(i);
                tempPriceMap = (Map<String, Object>) tempBarPriceList.get(i);

                String tempName = (String) tempMap.get("name");

                Double tempPrice = (Double) tempPriceMap.get(tempName);
                String tempPriceString = String.format("%.2f", tempPrice);
                String tempPriceinPounds = "£" + tempPriceString;

                String tempDesc = "Details:" + "\n";

                Map<String, Map<String, Object>> tempAllTypes = new HashMap<String, Map<String, Object>>();
                tempAllTypes = (Map<String, Map<String, Object>>) tempMap.get("types");
                for (String key : tempAllTypes.keySet()) {
                    Map<String, Object> tempType = new HashMap<>();
                    tempType = (Map<String, Object>) tempAllTypes.get(key);
                    String tempTypeName = (String) key;
                    String tempTypeSelected = (String) tempType.get("name").toString();

                    tempDesc = tempDesc + tempTypeName + ":   " + tempTypeSelected + "\n";
                }

                Map<String, Object> tempAllOptions = new HashMap<>();
                tempAllOptions = (Map<String, Object>) tempMap.get("options");
                for (String key : tempAllOptions.keySet()) {
                    Map<String, Object> tempOption = new HashMap<>();
                    tempOption = (Map<String, Object>) tempAllOptions.get(key);
                    String tempOptionName = (String) tempOption.get("name");
                    String tempOptionQuantity = (String) tempOption.get("quantity").toString();

                    tempDesc = tempDesc + tempOptionName + ":   " + tempOptionQuantity + "\n";
                }

                tempDesc = tempDesc + "\n" + "Allergies:" + "\n";

                List<String> tempAllAllergies = new ArrayList<String>();
                tempAllAllergies = (List<String>) tempMap.get("allergies");
                for (int j = 0; j <  tempAllAllergies.size() - 1; j++){
                    tempDesc = tempDesc + tempAllAllergies.get(j) + "\n";
                }

                if(tempAllAllergies.size() != 0 && tempAllAllergies != null) {
                    tempDesc = tempDesc + tempAllAllergies.get(tempAllAllergies.size() - 1);
                } else {
                    tempDesc = tempDesc + "None";
                }

                list.add(new CartModel(tempName, tempPriceinPounds, tempDesc, "Bar"));
                adapter.notifyDataSetChanged();
            }
        }

        Integer size5 = tempButteryCartList.size();
        Integer size6 = tempButteryPriceList.size();

        if(size5 > 0 && size6 > 0) {
            for (int i = 0; i < size5; i++) {

                Map<String, Object> tempMap = new HashMap<>();
                Map<String, Object> tempPriceMap = new HashMap<>();
                tempMap = (Map<String, Object>) tempButteryCartList.get(i);
                tempPriceMap = (Map<String, Object>) tempButteryPriceList.get(i);

                String tempName = (String) tempMap.get("name");

                Double tempPrice = (Double) tempPriceMap.get(tempName);
                String tempPriceString = String.format("%.2f", tempPrice);
                String tempPriceinPounds = "£" + tempPriceString;

                String tempDesc = "Details:" + "\n";

                Map<String, Map<String, Object>> tempAllTypes = new HashMap<String, Map<String, Object>>();
                tempAllTypes = (Map<String, Map<String, Object>>) tempMap.get("types");
                for (String key : tempAllTypes.keySet()) {
                    Map<String, Object> tempType = new HashMap<>();
                    tempType = (Map<String, Object>) tempAllTypes.get(key);
                    String tempTypeName = (String) key;
                    String tempTypeSelected = (String) tempType.get("name").toString();

                    tempDesc = tempDesc + tempTypeName + ":   " + tempTypeSelected + "\n";
                }

                Map<String, Object> tempAllOptions = new HashMap<>();
                tempAllOptions = (Map<String, Object>) tempMap.get("options");
                for (String key : tempAllOptions.keySet()) {
                    Map<String, Object> tempOption = new HashMap<>();
                    tempOption = (Map<String, Object>) tempAllOptions.get(key);
                    String tempOptionName = (String) tempOption.get("name");
                    String tempOptionQuantity = (String) tempOption.get("quantity").toString();

                    tempDesc = tempDesc + tempOptionName + ":   " + tempOptionQuantity + "\n";
                }

                tempDesc = tempDesc + "\n" + "Allergies:" + "\n";

                List<String> tempAllAllergies = new ArrayList<String>();
                tempAllAllergies = (List<String>) tempMap.get("allergies");
                for (int j = 0; j <  tempAllAllergies.size() - 1; j++){
                    tempDesc = tempDesc + tempAllAllergies.get(j) + "\n";
                }

                if(tempAllAllergies.size() != 0 && tempAllAllergies != null) {
                    tempDesc = tempDesc + tempAllAllergies.get(tempAllAllergies.size() - 1);
                } else {
                    tempDesc = tempDesc + "None";
                }

                list.add(new CartModel(tempName, tempPriceinPounds, tempDesc, "Buttery"));
                adapter.notifyDataSetChanged();
            }
        }

        String value = dietary.getText().toString();

        adapter.notifyDataSetChanged();

        totalCalc();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer size1 = tempCafeCartList.size();
                Integer size2 = tempBarCartList.size();
                Integer size4 = tempCafePriceList.size();
                Integer size3 = tempBarPriceList.size();
                Integer size5 = tempButteryCartList.size();
                Integer size6 = tempButteryPriceList.size();

                if(size1 == 0 && size4 == 0 && size2 == 0 && size3 == 0 && size5 == 0 && size6 == 0) {
                    Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                } else {
                    next.setVisibility(View.GONE);
                    linlay.setVisibility(View.VISIBLE);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setVisibility(View.VISIBLE);
                linlay.setVisibility(View.GONE);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalCalc();
                String notes = dietary.getText().toString();
                Timestamp now = Timestamp.now();

                Integer size1 = tempCafeCartList.size();
                Integer size2 = tempBarCartList.size();
                Integer size4 = tempCafePriceList.size();
                Integer size3 = tempBarPriceList.size();
                Integer size5 = tempButteryCartList.size();
                Integer size6 = tempButteryPriceList.size();

                if(size1 > 0 && size4 > 0) {

                    Map<String, Object> cafe = new HashMap<>();
                    cafe.put("archived", false);
                    cafe.put("cancelled", false);
                    cafe.put("flagged", false);
                    cafe.put("email", outputEmail);
                    cafe.put("location", "Cafe");
                    cafe.put("order_datetime", now);
                    cafe.put("price", cafeCartTotal);
                    cafe.put("user", outputCrsid);
                    cafe.put("name", outputName);
                    cafe.put("items", MainActivity.cafeCart);
                    cafe.put("note", notes);

                    String newDoc = db.collection("orders").document().getId();
                    db.collection("orders").document(newDoc)
                            .set(cafe)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });

                }

                if(size2 > 0 && size3 > 0) {
                    Map<String, Object> bar = new HashMap<>();
                    bar.put("archived", false);
                    bar.put("cancelled", false);
                    bar.put("flagged", false);
                    bar.put("email", outputEmail);
                    bar.put("location", "Bar");
                    bar.put("order_datetime", now);
                    bar.put("price", barCartTotal);
                    bar.put("user", outputCrsid);
                    bar.put("name", outputName);
                    bar.put("items", MainActivity.barCart);
                    bar.put("note", notes);

                    String newDoc = db.collection("orders").document().getId();
                    db.collection("orders").document(newDoc)
                            .set(bar)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                }

                if(size5 > 0 && size6 > 0) {
                    Map<String, Object> buttery = new HashMap<>();
                    buttery.put("archived", false);
                    buttery.put("cancelled", false);
                    buttery.put("flagged", false);
                    buttery.put("email", outputEmail);
                    buttery.put("location", "Buttery");
                    buttery.put("order_datetime", now);
                    buttery.put("price", butteryCartTotal);
                    buttery.put("user", outputCrsid);
                    buttery.put("name", outputName);
                    buttery.put("items", MainActivity.butteryCart);
                    buttery.put("note", notes);

                    String newDoc = db.collection("orders").document().getId();
                    db.collection("orders").document(newDoc)
                            .set(buttery)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                }

                MainActivity.barPrices.clear();
                MainActivity.barCart.clear();
                MainActivity.cafePrices.clear();
                MainActivity.cafeCart.clear();
                MainActivity.butteryPrices.clear();
                MainActivity.butteryCart.clear();
                Fragment nextFragment = new EndFragment(); //change this to expanded fragment name
                Bundle bundle = new Bundle();
                nextFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, nextFragment);
                for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                    fragmentManager.popBackStack();
                }
                fragmentTransaction.commit();
            }
        });

        String UID = mFirebaseAuth.getUid();

        DocumentReference docRef = db.collection("users").document(UID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(UserModel.class);
                        outputEmail = user.getEmail();
                        outputName = user.getFirstname() + " " + user.getLastname();
                        outputCrsid = user.getCrsid();
                        name.setText(outputName);
                        crsid.setText(outputCrsid);
                        email.setText(outputEmail);
                    } else {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return(rootView);
    }

    private void expandItem(View v, int position){
        ImageView infoButton = (ImageView) v.findViewById(R.id.info);
        TextView description = (TextView) v.findViewById(R.id.description);

        if(description.getVisibility() == View.VISIBLE && infoButton.getVisibility() == View.GONE){
            description.setVisibility(View.GONE);
            infoButton.setVisibility(View.VISIBLE);
        } else if(description.getVisibility() == View.GONE && infoButton.getVisibility() == View.VISIBLE){
            description.setVisibility(View.VISIBLE);
            infoButton.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.GONE);
            infoButton.setVisibility(View.VISIBLE);
        }
    }

    private void deleteItem(int position){

        if (list.get(position).getItemLoc().equals("Cafe")){
            list.remove(position);
            adapter.notifyItemRemoved(position);
            MainActivity.cafeCart.remove(position);
            MainActivity.cafePrices.remove(position);
        } else if (list.get(position).getItemLoc().equals("Bar")){
            int size1 = tempCafeCartList.size();
            list.remove(position);
            adapter.notifyItemRemoved(position);
            MainActivity.barCart.remove(position - size1);
            MainActivity.barPrices.remove(position - size1);
        } else if (list.get(position).getItemLoc().equals("Buttery")) {
            int size1 = tempCafeCartList.size();
            int size2 = tempBarCartList.size();
            list.remove(position);
            adapter.notifyItemRemoved(position);
            MainActivity.butteryCart.remove(position - size1 - size2);
            MainActivity.butteryPrices.remove(position - size1 - size2);
        }

        totalCalc();
    }

    private void totalCalc(){
        cafeCartTotal = 0.00;
        barCartTotal = 0.00;
        butteryCartTotal = 0.00;
        Integer size1 = tempCafeCartList.size();
        Integer size2 = tempBarCartList.size();
        Integer size4 = tempCafePriceList.size();
        Integer size3 = tempBarPriceList.size();
        Integer size5 = tempButteryCartList.size();
        Integer size6 = tempButteryPriceList.size();
        if(size1 > 0 && size4 > 0) {
            for (int i = 0; i < size1; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap = (Map<String, Object>) tempCafeCartList.get(i);
                Map<String, Object> tempPriceMap = new HashMap<>();
                tempPriceMap = (Map<String, Object>) tempCafePriceList.get(i);
                String tempName = (String) tempMap.get("name");
                Double tempPrice = (Double) tempPriceMap.get(tempName);
                cafeCartTotal += tempPrice;
            }
        }

        if(size2 > 0 && size3 > 0) {
            for (int i = 0; i < size2; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap = (Map<String, Object>) tempBarCartList.get(i);
                Map<String, Object> tempPriceMap = new HashMap<>();
                tempPriceMap = (Map<String, Object>) tempBarPriceList.get(i);
                String tempName = (String) tempMap.get("name");
                Double tempPrice = (Double) tempPriceMap.get(tempName);
                barCartTotal += tempPrice;
            }
        }

        if(size5 > 0 && size6 > 0) {
            for (int i = 0; i < size5; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap = (Map<String, Object>) tempButteryCartList.get(i);
                Map<String, Object> tempPriceMap = new HashMap<>();
                tempPriceMap = (Map<String, Object>) tempButteryPriceList.get(i);
                String tempName = (String) tempMap.get("name");
                Double tempPrice = (Double) tempPriceMap.get(tempName);
                butteryCartTotal += tempPrice;
            }
        }

        Double cartTotal = barCartTotal + cafeCartTotal + butteryCartTotal;

        String priceString = String.format("%.2f", cartTotal);
        String orderText = "ORDER:       £" + priceString;
        next.setText(orderText);
    }
}
