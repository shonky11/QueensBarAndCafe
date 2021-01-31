package com.qads.queensbarandcafe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.activities.MainActivity;
import com.qads.queensbarandcafe.helpers.CartAdapter;
import com.qads.queensbarandcafe.helpers.CartItem;
import com.qads.queensbarandcafe.helpers.OptionsAdapter;
import com.qads.queensbarandcafe.helpers.OptionsModel;

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
    private List<Object> tempBarCartList = new ArrayList<>();
    private List<Object> tempCafePriceList = new ArrayList<>();
    private List<Object> tempBarPriceList = new ArrayList<>();
    private ArrayList<CartItem> list= new ArrayList<>();
    private CartAdapter adapter = new CartAdapter(list);
    private Double cafeCartTotal = 0.00;
    private Double barCartTotal = 0.00;
    private Button order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cart_fragment, container, false);
        TextView name = rootView.findViewById(R.id.name);
        TextView crsid = rootView.findViewById(R.id.crsid);
        TextView email = rootView.findViewById(R.id.email);
        order = rootView.findViewById(R.id.order);
        EditText dietary = rootView.findViewById(R.id.dietary);

        list.clear();
        adapter.notifyDataSetChanged();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cart_recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(new CartAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View v, int position) {
                deleteItem(position);
            }
        });

        tempCafePriceList = MainActivity.cafePrices;
        tempCafeCartList = MainActivity.cafeCart;
        tempBarCartList = MainActivity.barCart;
        tempBarPriceList = MainActivity.barPrices;
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

                Map<String, Object> tempAllOptions = new HashMap<>();
                String tempDesc = "";
                tempAllOptions = (Map<String, Object>) tempMap.get("options");
                for (String key : tempAllOptions.keySet()) {
                    Map<String, Object> tempOption = new HashMap<>();
                    tempOption = (Map<String, Object>) tempAllOptions.get(key);
                    String tempOptionName = (String) tempOption.get("name");
                    String tempOptionQuantity = (String) tempOption.get("quantity").toString();

                    tempDesc = tempDesc + tempOptionName + ":   " + tempOptionQuantity + "\n";
                }

                list.add(new CartItem(tempName, tempPriceinPounds, tempDesc, "Cafe"));
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

                Map<String, Object> tempAllOptions = new HashMap<>();
                String tempDesc = "";
                tempAllOptions = (Map<String, Object>) tempMap.get("options");
                for (String key : tempAllOptions.keySet()) {
                    Map<String, Object> tempOption = new HashMap<>();
                    tempOption = (Map<String, Object>) tempAllOptions.get(key);
                    String tempOptionName = (String) tempOption.get("name");
                    String tempOptionQuantity = (String) tempOption.get("quantity").toString();

                    tempDesc = tempDesc + tempOptionName + ":   " + tempOptionQuantity + "\n";
                }

                list.add(new CartItem(tempName, tempPriceinPounds, tempDesc, "Bar"));
                adapter.notifyDataSetChanged();
            }
        }

        String value = dietary.getText().toString();

        adapter.notifyDataSetChanged();

        totalCalc();

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.barPrices.clear();
                MainActivity.barCart.clear();
                MainActivity.cafePrices.clear();
                MainActivity.cafeCart.clear();
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

        return(rootView);
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
        }

        totalCalc();
    }

    private void totalCalc(){
        cafeCartTotal = 0.00;
        barCartTotal = 0.00;
        Integer size1 = tempCafeCartList.size();
        Integer size2 = tempBarCartList.size();
        Integer size4 = tempCafePriceList.size();
        Integer size3 = tempBarPriceList.size();
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

        Double cartTotal = barCartTotal + cafeCartTotal;

        Toast.makeText(getContext(), barCartTotal.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), cafeCartTotal.toString(), Toast.LENGTH_SHORT).show();

        String priceString = String.format("%.2f", cartTotal);
        String orderText = "ORDER:       £" + priceString;
        order.setText(orderText);
    }
}
