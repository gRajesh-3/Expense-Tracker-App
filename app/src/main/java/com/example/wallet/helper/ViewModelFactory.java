package com.example.wallet.helper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory<T extends ViewModel> implements ViewModelProvider.Factory {
    private final Context context;
    private final Class<T> viewModelClass;

    public ViewModelFactory(Context context, Class<T> viewModelClass) {
        this.context = context;
        this.viewModelClass = viewModelClass;
    }

    @NonNull
    @Override
    public <U extends ViewModel> U create(@NonNull Class<U> modelClass) {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            try {
                return (U) viewModelClass.getConstructor(Context.class).newInstance(context);
            } catch (Exception e) {
                throw new RuntimeException("Error creating ViewModel instance", e);
            }
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}


