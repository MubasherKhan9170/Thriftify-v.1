package com.example.thriftify.view.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.thriftify.R;
import com.example.thriftify.databinding.FragmentShelveBinding;
import com.example.thriftify.service.model.shelve_model.ShelveItemDetails;
import com.example.thriftify.utils.SharedPreferencesUtils;
import com.example.thriftify.view.ui.user_authentication.LoginActivity;
import com.example.thriftify.viewmodel.ShelveCardDetailModelView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ShelveFragment extends Fragment{


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShelveFragment() {
        // Required empty public constructor
    }

  /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShelveFragment.*/

    // TODO: Rename and change types and number of parameters
    public static ShelveFragment newInstance(String param1, String param2) {
        ShelveFragment fragment = new ShelveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }
    private LifecycleOwner viewLifecycleOwner;
    private FragmentShelveBinding binding;
    private ShelveCardDetailModelView viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(getActivity()).get(ShelveCardDetailModelView.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shelve, container, false);
        View view = this.binding.getRoot();
        binding.setModel(viewModel);
        if (savedInstanceState == null) {
            this.viewModel.init();
        }
        binding.setModel(this.viewModel);
        // LiveData needs the lifecycle owner
        binding.setLifecycleOwner(this);
        this.setupListUpdate();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.shelveFragment).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar_shelves);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        setHasOptionsMenu(true);

        }


    private void setupListUpdate() {
        //viewModel.loading.set(View.VISIBLE);
        // update the list by calling
        this.viewModel.getShelveItems().observe(this.getViewLifecycleOwner(), new Observer<List<ShelveItemDetails>>() {
            @Override
            public void onChanged(final List<ShelveItemDetails> items) {
                //viewModel.loading.set(View.GONE);
                if (items.size() == 0) {
                    //  viewModel.showEmpty.set(View.VISIBLE);
                } else {
                    //viewModel.showEmpty.set(View.GONE);
                    ShelveFragment.this.viewModel.setShelveItemObjectsInAdapter(items);

                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.shelve_fragment_menu, menu);

        // Get the SearchView and set the searchable configuration
        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.shelve_app_bar_search).getActionView();
        searchView.setQueryHint(this.getResources().getString(R.string.product_search_label));
        // Assumes current activity is the searchable activity
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setMaxWidth(android.R.attr.width);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.getAdapter().getFilter().filter(newText);
                return true;
            }
        });


        // Detect SearchView icon clicks

        final MenuItem menuItem = menu.findItem(R.id.shelve_app_bar_search);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(final MenuItem item) {
                menu.findItem(R.id.shelve_app_bar_logout).setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem item) {
                menu.findItem(R.id.shelve_app_bar_logout).setVisible(true);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* Handle item selection */
        final int itemId = item.getItemId();
        if (itemId == R.id.shelve_app_bar_logout) {
            SharedPreferencesUtils.get(getActivity()).setTokenSharedPreferences(null);
            Intent i = new Intent(getActivity(), LoginActivity.class);
            this.startActivity(i);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}