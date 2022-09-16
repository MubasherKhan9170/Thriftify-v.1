package com.example.thriftify.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thriftify.R;
import com.example.thriftify.databinding.FragmentShippedOrdersBinding;
import com.example.thriftify.service.model.ship_model.ShippedOrdersModel;
import com.example.thriftify.viewmodel.ShipActivityViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ShippedOrdersFragment extends Fragment {
    private ShipActivityViewModel viewModel;
    private LifecycleOwner viewLifecycleOwner;
    private FragmentShippedOrdersBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Obtain binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shipped_orders, container, false);
        final View view = binding.getRoot();

        // LiveData needs the lifecycle owner
        this.viewLifecycleOwner = this.getViewLifecycleOwner();
        binding.setLifecycleOwner(this.viewLifecycleOwner);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ShipActivityViewModel.class);
        // Bind layout with ViewModel
        binding.setShippedModel(viewModel);

        viewModel.getShippedProductListItems().observe(getActivity(), new Observer<List<ShippedOrdersModel>>() {
            @Override
            public void onChanged(List<ShippedOrdersModel> items) {
                //viewModel.loading.set(View.GONE);
                if (items.size() == 0) {
                    //  viewModel.showEmpty.set(View.VISIBLE);
                } else {
                    //viewModel.showEmpty.set(View.GONE);
                    viewModel.setShippedItemObjectsInAdapter(items);
                }
            }
        });

    }
}
