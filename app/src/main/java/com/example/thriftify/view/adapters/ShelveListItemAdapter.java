package com.example.thriftify.view.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.thriftify.BR;
import com.example.thriftify.service.model.shelve_model.ShelveItemDetails;
import com.example.thriftify.viewmodel.ShelveCardDetailModelView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class ShelveListItemAdapter extends RecyclerView.Adapter<ShelveListItemAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "ShelveListItemAdapter";

    //this one
    private int layoutId;
    private List<ShelveItemDetails> shelveItems;
    private List<ShelveItemDetails> filterItem;
    private ShelveCardDetailModelView viewModel;

    //this one
    public ShelveListItemAdapter(@LayoutRes int layoutId, ShelveCardDetailModelView viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.filterItem = new ArrayList<>();
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this one
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //this one
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public void setShelveItems(List<ShelveItemDetails> items) {

        this.shelveItems = items;
        filterItem.addAll(shelveItems);
    }

    @Override
    public int getItemCount() {
        // this one
        return shelveItems == null ? 0 : shelveItems.size();
    }


    public static class ViewHolder  extends RecyclerView.ViewHolder {
        //this one
        final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ShelveCardDetailModelView viewModel, Integer position) {
            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }

    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ShelveItemDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filterItem);
                Log.e(TAG, "performFiltering: "+  filterItem.size());
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ShelveItemDetails item : filterItem) {
                    if (item.getProduct_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shelveItems.clear();
            shelveItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };




    //update the list
    public void updateList(List<ShelveItemDetails> newList){
        shelveItems = new ArrayList<>();
        shelveItems.addAll(newList);
        notifyDataSetChanged();
    }
}
