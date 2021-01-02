package com.qads.queensbarandcafe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.helpers.Category;
import com.qads.queensbarandcafe.helpers.CategoryAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BarFragment extends Fragment {

    ArrayList<Category> categories;
    private RecyclerView eventsView;
    private View rootView;
    private AppBarLayout appBarLayout1;
    private Toolbar toolbar, loadedToolbar;
    private RelativeLayout relativeLayout;
    private CardView loadedCardView, searchBar;
    private ImageView cartImageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.location_fragment, container, false);

        RecyclerView categoriesRecycler = (RecyclerView) rootView.findViewById(R.id.categories_rv);

        categories = createCategoriesList();
        CategoryAdapter adapter = new CategoryAdapter(categories);
        categoriesRecycler.setAdapter(adapter);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        setupCollapsingToolbar(rootView);

        adapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(View v, int position) {
                Category clickedCategory = (Category) categories.get(position);
                onCategoryClickeroo(v, clickedCategory);
            }
        });

        cartImageButton = (ImageView) rootView.findViewById(R.id.cart_button);
        cartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "hehe", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    private void setupCollapsingToolbar(View rootView){
        relativeLayout = rootView.findViewById(R.id.relative_layout);
        loadedCardView = rootView.findViewById(R.id.final_card_view);
        loadedToolbar = rootView.findViewById(R.id.final_toolbar);

    }

    public static ArrayList<Category> createCategoriesList() {
        ArrayList<Category> categories = new ArrayList<Category>();

        categories.add(new Category("Beers, Lagers and Ales", R.drawable.beer_template));
        categories.add(new Category("Cocktails", R.drawable.cocktails_template));
        categories.add(new Category("Spirits", R.drawable.spirits_template));
        categories.add(new Category("Soft Drinks and Mocktails", R.drawable.cold_drinks_template));
        categories.add(new Category("Food", R.drawable.food_template));
        categories.add(new Category("Snacks", R.drawable.snacks_template));

        return categories;
    }

    public void onCategoryClickeroo(View v, Category clickCat){

        Fragment nextFragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Current Category", clickCat.getCategory());
        bundle.putString("Current Location", "Bar");
        nextFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, nextFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
