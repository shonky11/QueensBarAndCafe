package com.qads.queensbarandcafe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.qads.queensbarandcafe.R;
import com.qads.queensbarandcafe.activities.LoginActivity;
import com.qads.queensbarandcafe.activities.SignUpActivity;
import com.qads.queensbarandcafe.models.CategoryModel;
import com.qads.queensbarandcafe.adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BarFragment extends Fragment {

    ImageView signout_btn;

    ArrayList<CategoryModel> categories;
    private RecyclerView eventsView;
    private View rootView;
    private AppBarLayout appBarLayout1;
    private Toolbar toolbar, loadedToolbar;
    private RelativeLayout relativeLayout;
    private CardView loadedCardView, searchBar;
    private ImageView cartImageButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference categoryRef = db.collection("categories");
    private List<CategoryModel> categoriesList = new ArrayList<>();
    private CategoryAdapter adapter = new CategoryAdapter(categoriesList);
    private SwipeRefreshLayout swipeContainer;
    private DocumentReference locRef = db.collection("locations").document("bar");
    private ListenerRegistration catsReg;
    public static Boolean barOpen = false;
    private ListenerRegistration locReg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.location_fragment, container, false);

        RecyclerView categoriesRecycler = (RecyclerView) rootView.findViewById(R.id.categories_rv);
        TextView closed = (TextView) rootView.findViewById(R.id.closed);
        categoriesList.clear();
        createCategoriesList(closed);
        adapter = new CategoryAdapter(categoriesList);
        categoriesRecycler.setAdapter(adapter);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(View v, int position) {
                CategoryModel clickedCategory = (CategoryModel) categoriesList.get(position);
                onCategoryClickeroo(v, clickedCategory);
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


        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
            }
        });

        signOut(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        catsReg.remove();
        locReg.remove();
    }

    public void signOut(View v) {
        //SIGN OUT METHOD
        signout_btn = v.findViewById(R.id.sign_out);
        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mFirebaseAuth.getInstance().signOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


    public void createCategoriesList(final TextView closed) {

        Query query = categoryRef.whereEqualTo("location", "Bar").orderBy("name", Query.Direction.ASCENDING);
        locReg = locRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    barOpen = (Boolean) snapshot.getData().get("open");
                    if(!barOpen){
                        closed.setText("Bar Closed");
                        closed.setVisibility(View.VISIBLE);
                    } else {
                        closed.setVisibility(View.GONE);
                    }
                } else {

                }
            }
        });

        catsReg = query .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            final CategoryModel cardAdded = dc.getDocument().toObject(CategoryModel.class);
                            categoriesList.add(cardAdded);
                            adapter.notifyItemInserted(dc.getNewIndex());
                            break;
                        case MODIFIED:
                            categoriesList.remove(dc.getOldIndex());
                            adapter.notifyItemRemoved(dc.getOldIndex());
                            final CategoryModel cardChanged = dc.getDocument().toObject(CategoryModel.class);
                            categoriesList.add(dc.getNewIndex(), cardChanged);
                            adapter.notifyItemInserted(dc.getNewIndex());
                            break;
                        case REMOVED:
                            categoriesList.remove(dc.getOldIndex());
                            adapter.notifyItemRemoved(dc.getOldIndex());
                            break;
                        default:
                            break;
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    public void onCategoryClickeroo(View v, CategoryModel clickCat){
        if(barOpen) {
            Fragment nextFragment = new ItemsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Current Category", clickCat.getName());
            bundle.putString("Current Location", "Bar");
            nextFragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, nextFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else {
            Toast.makeText(getActivity(), "The QBar is currently closed", Toast.LENGTH_SHORT).show();
        }

    }
}
