package com.example.thriftify.view.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.thriftify.R;
import com.example.thriftify.databinding.LayoutProductEditDialogBoxBinding;
import com.example.thriftify.viewmodel.ShelveCardDetailModelView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

public class ShelveProductEditDialogFragment extends BottomSheetDialogFragment {
    private static final String TAG = "ShelveProductEditDialog";
    private LifecycleOwner viewLifecycleOwner;
    private LayoutProductEditDialogBoxBinding binding;
    private  Integer position;

    public ShelveProductEditDialogFragment( final Integer position) {
    this.position = position;
    }

    public ShelveProductEditDialogFragment() {
    }

    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
       // return inflater.inflate(R.layout.layout_product_edit_dialog_box, container, false);
       // View rootView = inflater.inflate(R.layout.layout_product_edit_dialog_box, container, false);
        // Obtain binding
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_product_edit_dialog_box, container, false);
        View view = this.binding.getRoot();

        // LiveData needs the lifecycle owner
        this.viewLifecycleOwner = this.getViewLifecycleOwner();
        binding.setLifecycleOwner(this.viewLifecycleOwner);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_shelves_edit_dialog_fragment);
        toolbar.setTitle("Dialog title");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShelveCardDetailModelView viewModel = new ViewModelProvider(requireActivity()).get(ShelveCardDetailModelView.class);
        binding.setEditShelveModel(viewModel);

        binding.setPosition(ShelveProductEditDialogFragmentArgs.fromBundle(getArguments()).getItemId());
        //binding.executePendingBindings();


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.shelve_edit_dialog_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // handle confirmation button click here
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            this.dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
