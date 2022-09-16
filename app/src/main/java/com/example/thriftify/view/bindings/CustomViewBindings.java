package com.example.thriftify.view.bindings;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.thriftify.R;
import com.example.thriftify.view.ui.fragments.ShelveFragmentDirections;

import androidx.databinding.BindingAdapter;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewBindings {
    private static final String TAG = "CustomViewBindings";
    @BindingAdapter("setAdapter")
    public static void bindRecyclerViewAdapter(final RecyclerView recyclerView, final RecyclerView.Adapter<?> adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);

    }

    @BindingAdapter("onClickPosition")
    public static  void bindRecyclerViewAdapter(final Button button, int position){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //Log.e(TAG, "onClick: "+ position );
                final ShelveFragmentDirections.ActionShelveFragmentToShelveProductEditDialogFragment action = ShelveFragmentDirections.actionShelveFragmentToShelveProductEditDialogFragment();
                action.setItemId(position);
                Navigation.findNavController(view).navigate(action);

            }
        });

    }




    @BindingAdapter("imageItemUrl")
    public static void bindRecyclerViewAdapter(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            // If we don't do this, you'll see the old image appear briefly
            // before it's replaced with the current image
            if (imageView.getTag(R.id.gem_image_id) == null || !imageView.getTag(R.id.gem_image_id).equals(imageUrl)) {
                imageView.setImageBitmap(null);
                imageView.setTag(R.id.gem_image_id, imageUrl);
                Glide.with(imageView).load(imageUrl).into(imageView);
            }
        } else {
            imageView.setTag(R.id.gem_image_id, null);
            imageView.setImageBitmap(null);
        }
    }
}
