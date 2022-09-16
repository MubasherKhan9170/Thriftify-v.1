package com.example.thriftify.viewmodel;

import com.example.thriftify.R;
import com.example.thriftify.service.model.shelve_model.ShelveItemDetails;
import com.example.thriftify.view.adapters.ShelveListItemAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShelveCardDetailModelView extends ViewModel {
    // this is associated with repository Class
    MutableLiveData<List<ShelveItemDetails>> repoList;

    // this livedata object is associated with viewModel
    public MutableLiveData<List<ShelveItemDetails>> userLiveData;
    public List<ShelveItemDetails> finalList;
    private ShelveListItemAdapter adapter;

    public ShelveCardDetailModelView() {
        userLiveData = new MutableLiveData<>();

    }

/*    public ShelveCardDetailModelView() {
        userLiveData = new MutableLiveData<>();

        // call your Rest API in init method
        init();
    }*/

    public void init(){
        repoList = new MutableLiveData<>();
        adapter = new ShelveListItemAdapter(R.layout.layout_shelve_gem_attribute_detail_card, this);
        populateList();
    }




    // set the adapter to a adapterView in shelve activity XML
    public ShelveListItemAdapter getAdapter() {
        return adapter;
    }


    // update adapter value at observer methods
    public void setShelveItemObjectsInAdapter(List<ShelveItemDetails> items) {
        this.adapter.setShelveItems(items);
        this.adapter.notifyDataSetChanged();
    }
    // calling this method to observe the change
    public MutableLiveData<List<ShelveItemDetails>> getShelveItems() {
        return userLiveData = repoList;
    }

    public void populateList(){

        ShelveItemDetails item_1 = new ShelveItemDetails("ABC", "23444-78923497sdf89b", "112", "9.08", "Friday, 22 January 2021", "", "Paperback", "James clear", "Active");
        ShelveItemDetails item_2 = new ShelveItemDetails("DEF", "33444-78923497sdf89c", "112", "9.08", "Saturday, 22 January 2021", "1st", "Book", "James clear", "Active");
        ShelveItemDetails item_3 = new ShelveItemDetails("GHI", "43444-78923497sdf89a", "112", "9.08", "Sunday, 22 January 2021", "", "Dress", "", "Active");

        List<ShelveItemDetails> userList = new ArrayList<>();
        userList.add(item_1);
        userList.add(item_2);
        userList.add(item_3);
        finalList = userList;
        repoList.setValue(userList);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
