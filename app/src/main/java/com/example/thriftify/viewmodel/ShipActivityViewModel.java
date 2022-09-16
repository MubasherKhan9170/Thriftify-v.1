package com.example.thriftify.viewmodel;

import com.example.thriftify.R;
import com.example.thriftify.service.model.ship_model.PendingOrdersModel;
import com.example.thriftify.service.model.ship_model.ShippedOrdersModel;
import com.example.thriftify.service.respository.ProductDetailRepository;
import com.example.thriftify.view.adapters.ShipFragmentPendingOrdersAdapter;
import com.example.thriftify.view.adapters.ShipFragmentShippedOrdersAdapter;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ShipActivityViewModel extends ViewModel {
    private final ProductDetailRepository repository;
    public MutableLiveData<List<PendingOrdersModel>> pendingOrder_liveData = new MutableLiveData<>();
    public MutableLiveData<List<ShippedOrdersModel>> shippedOrder_liveData = new MutableLiveData<>();
    private static ShipFragmentPendingOrdersAdapter mPendingAdapter = null;
    private static ShipFragmentShippedOrdersAdapter mShippedAdapter = null;

    @Inject
    public ShipActivityViewModel(ProductDetailRepository repository) {
        this.repository = repository;
        init();

    }
    public void init(){
        mPendingAdapter = new ShipFragmentPendingOrdersAdapter(R.layout.layout_ship_pedding_gem_attribute_card, this);
        mShippedAdapter = new ShipFragmentShippedOrdersAdapter(R.layout.layout_ship_shipped_gem_attribute_card, this);
    }

    // set the adapter to a adapterView in pending adapter of ship activity XML
    @NonNull
    public final ShipFragmentPendingOrdersAdapter getPendingAdapter() {
        return mPendingAdapter;
    }

    // update adapter value at observer methods in pending orders fragment
    public void setPendingItemObjectsInAdapter(List<PendingOrdersModel> items) {
        this.mPendingAdapter.setPendingOrderItems(items);
        this.mPendingAdapter.notifyDataSetChanged();
    }

    public LiveData<List<PendingOrdersModel>> getPendingProductListItems(){
        pendingOrder_liveData = repository.getPendingOrdersList();
        return  pendingOrder_liveData;
    }

    // set the adapter to a adapterView in pending adapter of ship activity XML
    @NonNull
    public ShipFragmentShippedOrdersAdapter getShippedAdapter() {
        return mShippedAdapter;
    }

    // update adapter value at observer methods in Shipped orders fragment

    public void setShippedItemObjectsInAdapter(List<ShippedOrdersModel> items) {
        this.mShippedAdapter.setShippedOrderItems(items);
        this.mShippedAdapter.notifyDataSetChanged();
    }

    public LiveData<List<ShippedOrdersModel>> getShippedProductListItems(){
        shippedOrder_liveData = repository.getShippedOrdersList();
        return  shippedOrder_liveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


}
