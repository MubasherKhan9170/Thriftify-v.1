package com.example.thriftify.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.thriftify.R;
import com.example.thriftify.utils.SqaureImageView;
import com.example.thriftify.view.ui.fragments.UnbarCodeGemsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GridImageViewAdapter extends ArrayAdapter<Bitmap> {
    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private List<Bitmap> imgURLs;
    private Menu actionMenu;

    public GridImageViewAdapter(@NonNull Context context, List<Bitmap> imgURLs) {
        super(context, 0, imgURLs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.imgURLs = imgURLs;
    }
    private static class ViewHolder{
        SqaureImageView image;
        CheckBox checkBox;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*
        Viewholder build pattern (Similar to recyclerview)
         */
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.layout_grid_imageview, parent, false);
            holder = new ViewHolder();
            //holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.gridImageProgressbar);
            holder.image = (SqaureImageView) convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Bitmap imgURL = getItem(position);
        holder.image.setImageBitmap(imgURL);

        holder.checkBox = convertView.findViewById(R.id.checkbox);
        holder.checkBox.setTag(position);
        if(UnbarCodeGemsFragment.inActionMode){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = (int) compoundButton.getTag();

                if(UnbarCodeGemsFragment.userSelection.contains(imgURLs.get(position))){
                    UnbarCodeGemsFragment.userSelection.remove(imgURLs.get(position));
                }else{
                    UnbarCodeGemsFragment.userSelection.add(imgURLs.get(position));
                }
                setVisibility();
                UnbarCodeGemsFragment.mode.setTitle(UnbarCodeGemsFragment.userSelection.size() + " Items Selected");
            }
        });


        return convertView;
    }

    //To delete the items from the list
    public void removeItems(List<Bitmap> items){
        for (Bitmap item: items) {
            imgURLs.remove(item);
        }
        notifyDataSetChanged();
    }

    //To get the menu items
    public void setActionMenu(Menu menu){
        actionMenu = menu;
    }
    public Menu getActionMenu(){
        return actionMenu;
    }

    public void setVisibility(){
        MenuItem delete = getActionMenu().findItem(R.id.app_bar_delete);
        if(UnbarCodeGemsFragment.userSelection.size() == 0){
            delete.setEnabled(false);
        }else{
            delete.setEnabled(true);
        }
    }

}
