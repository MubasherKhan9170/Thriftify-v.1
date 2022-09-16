package com.example.thriftify.view.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.thriftify.R;
import com.example.thriftify.service.respository.ProductDetailRepository;
import com.example.thriftify.utils.SharedPreferencesUtils;
import com.example.thriftify.view.adapters.SectionsPagerAdapter;
import com.example.thriftify.view.ui.SearchBarcodeActivity;
import com.example.thriftify.view.ui.user_authentication.LoginActivity;
import com.example.thriftify.viewmodel.ProductDetailViewModel;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {

    protected ViewPager mViewPager;
    //Variables
    private ProductDetailViewModel model;
    @Inject
    ProductDetailRepository repository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //model = new ViewModelProvider(this, SingletonViewModelFactory.getInstance(repository)).get(ProductDetailViewModel.class);
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Add Product Detail ViewModel
       // model = new ViewModelProvider(this.requireActivity(), SingletonViewModelFactory.getInstance(repository)).get(ProductDetailViewModel.class);
        setupViewPager(view);
        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.scanFragment).build();
        Toolbar toolbar = view.findViewById(R.id.toolbar_scan);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        // Inflate the options menu from XML
        inflater.inflate(R.menu.scan_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /* Handle item selection */
        final int itemId = item.getItemId();
        if (R.id.scan_app_bar_search == itemId) {
            //ProductDetailViewModel model = new ViewModelProvider(this, SingletonViewModelFactory.getInstance(this.repository)).get(ProductDetailViewModel.class);
            //model.setErrProperty();
            final Intent intent = new Intent(getActivity(), SearchBarcodeActivity.class);
            this.startActivity(intent);
            return true;
        }
        if (itemId == R.id.scan_app_bar_logout) {
            SharedPreferencesUtils.get(getContext()).setTokenSharedPreferences(null);
            Intent i = new Intent(getActivity(), LoginActivity.class);
            this.startActivity(i);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Responsible for adding the 2 tabs: Barcoded Gems, Unbarcoded Gems.
     */
    private void setupViewPager(View view) {
        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(this.getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mSectionsPagerAdapter.addFragment(new BarCodeGemsFragment()); //index 0
        mSectionsPagerAdapter.addFragment(new UnbarCodeGemsFragment()); //index 1
        mViewPager = view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(" Find Barcoded Gems").setTag("BARCODE");
        tabLayout.getTabAt(1).setText("Add Unbarcoded Gems");
    }
}