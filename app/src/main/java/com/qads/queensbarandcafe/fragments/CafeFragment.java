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

public class CafeFragment extends Fragment {

    ArrayList<Category> categories;
    private RecyclerView eventsView;
    private View rootView;
    private AppBarLayout appBarLayout1;
    private Toolbar toolbar, loadedToolbar;
    private RelativeLayout relativeLayout;
    private CardView loadedCardView, searchBar;
    private ImageView notificationsImageBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cafe_fragment, container, false);
        setupCollapsingToolbar(rootView);

        RecyclerView categoriesRecycler = (RecyclerView) rootView.findViewById(R.id.categories_rv);

        categories = createCategoriesList();
        CategoryAdapter adapter = new CategoryAdapter(categories);
        categoriesRecycler.setAdapter(adapter);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
           @Override
           public void onCategoryClick(View v, int position) {
               Category clickedCategory = (Category) categories.get(position);
               onCategoryClickeroo(v, clickedCategory);
           }
        });

        notificationsImageBtn = (ImageView) rootView.findViewById(R.id.notifications_btn);
        notificationsImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "hehe", Toast.LENGTH_LONG).show();
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
        appBarLayout1 = (AppBarLayout) rootView.findViewById(R.id.appBarLayout);
        relativeLayout = rootView.findViewById(R.id.relative_layout);
        loadedCardView = rootView.findViewById(R.id.final_card_view);
        loadedToolbar = rootView.findViewById(R.id.final_toolbar);

    }

    public static ArrayList<Category> createCategoriesList() {
            ArrayList<Category> categories = new ArrayList<Category>();

            categories.add(new Category("Hot Drinks", R.drawable.coffee_template));
            categories.add(new Category("Cold Drinks", R.drawable.cold_drinks_template));
            categories.add(new Category("Snacks", R.drawable.snacks_template));
            categories.add(new Category("Sandwiches and Pasties", R.drawable.sandwiches_paninis_template));

            return categories;
    }

    public void onCategoryClickeroo(View v, Category clickCat){
        Fragment nextFragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Current Category", clickCat.getCategory());
        nextFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, nextFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
