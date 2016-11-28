package com.example.android.movieapp1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ImageView imageView;
    ArrayList<String> posterPath;

    //    public ImageAdapter(Context c) {
//        mContext = c;
//    }
    public ImageAdapter (Context c ,ArrayList<String> path ){
        mContext = c;
        posterPath = path;
    }
    public int getCount() {
        return posterPath.size();

    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 450));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }
        String baseURL="http://image.tmdb.org/t/p/w185";
//        for (String path:posterPath) {
        Picasso.with(mContext).load(baseURL+posterPath.get(position)).into(imageView);

//        }



        // imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }


}