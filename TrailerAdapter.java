package com.example.android.movieapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sara on 11/25/2016.
 */
public class TrailerAdapter extends BaseAdapter{
    private Context mcontext;
    ArrayList<TrailerResponse.ResultsBean> trailer;
    TrailerResponse.ResultsBean response;
    String link;
    String name;

    public TrailerAdapter(Context context, ArrayList<TrailerResponse.ResultsBean> trailer) {
        mcontext = context;
        this.trailer = trailer;
    }

    public int getCount() {
        return trailer.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View ConvertView, ViewGroup parent) {
        final ViewHolder holder;
        if (ConvertView == null) {
            holder = new ViewHolder();
            ConvertView = LayoutInflater.from(mcontext).inflate(
                    R.layout.trailer, null);
            holder.trailer = (ImageButton) ConvertView.findViewById(R.id.trailer);
            holder.trailertxt = (TextView) ConvertView.findViewById(R.id.trailer1);
        } else {
            holder = (ViewHolder) ConvertView.getTag();
        }
        response = trailer.get(position);
        link = response.getKey();
        name = response.getName();
        holder.trailertxt.setText(name);

        return ConvertView;
    }
}
class ViewHolder {
    ImageButton trailer;
    TextView trailertxt;

}


