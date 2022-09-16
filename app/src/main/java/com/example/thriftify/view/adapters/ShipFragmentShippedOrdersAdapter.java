package com.example.thriftify.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.thriftify.BR;
import com.example.thriftify.service.model.ship_model.ShippedOrdersModel;
import com.example.thriftify.viewmodel.ShipActivityViewModel;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class ShipFragmentShippedOrdersAdapter extends RecyclerView.Adapter<ShipFragmentShippedOrdersAdapter.ViewHolder> {
    //this one
    private int layoutId;
    private List<ShippedOrdersModel> items;
    private ShipActivityViewModel viewModel;

    //this one
    public ShipFragmentShippedOrdersAdapter(@LayoutRes int layoutId, ShipActivityViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public ShipFragmentShippedOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this one
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new ShipFragmentShippedOrdersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipFragmentShippedOrdersAdapter.ViewHolder holder, int position) {
        //this one
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public void setShippedOrderItems(List<ShippedOrdersModel> items) {
        this.items = items;
    }



    @Override
    public int getItemCount() {
        // this one
        return items == null ? 0 : items.size();
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder {
        //this one
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ShipActivityViewModel viewModel, Integer position) {
            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }

    }
}
