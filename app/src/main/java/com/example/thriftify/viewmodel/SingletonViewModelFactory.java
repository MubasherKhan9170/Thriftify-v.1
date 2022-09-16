package com.example.thriftify.viewmodel;

import android.util.Log;

import com.example.thriftify.service.respository.ProductDetailRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public final class SingletonViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static final String TAG = "SingletonViewModelFacto";

   // @SuppressLint("StaticFieldLeak")
    private static volatile SingletonViewModelFactory sInstance;


    private static ProductDetailRepository mTasksRepository = null;


    public static SingletonViewModelFactory getInstance(ProductDetailRepository repository) {

        if (sInstance == null) {
            synchronized (SingletonViewModelFactory.class) {
                if (sInstance == null) {
                    Log.e(TAG, "getInstance: "+ repository);
                    sInstance = new SingletonViewModelFactory(repository);
                }
            }
        }
        return sInstance;
    }

    private SingletonViewModelFactory(ProductDetailRepository repository) {
        this.mTasksRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductDetailViewModel.class)) {
            //noinspection unchecked
            return (T) ProductDetailViewModel.getInstance(mTasksRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
