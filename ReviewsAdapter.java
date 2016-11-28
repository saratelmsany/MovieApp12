package com.example.android.movieapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sara on 11/23/2016.
 */
public class ReviewsAdapter extends BaseAdapter{
    private Context mcontext;
    TextView reviewtxt;
    TextView authortxt;
    ArrayList<ResponseReview.ResultsBean>review;
    ResponseReview.ResultsBean response;
    String Author;
    String Review;

    public ReviewsAdapter(Context context,ArrayList<ResponseReview.ResultsBean>review) {
        mcontext = context;
        this.review=review;

    }

    public int getCount() {
        return review.size();
    }
    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final holder holder ;
        if (convertView == null) {
            holder=new holder();
            convertView = LayoutInflater.from(mcontext).inflate(
                    R.layout.review, null);
            holder. author=(TextView)convertView.findViewById(R.id.authortxt);
            holder.review =(TextView)convertView.findViewById(R.id.reviewtxt);



        } else {
            holder = (holder) convertView.getTag();
        }
       response=review.get(position);
        Author=response.getAuthor();
        Review=response.getContent();
        holder.author.setText(Author);
        holder.review.setText(Review);
        return convertView;
    }


}
class holder{
    TextView author;
    TextView review;
}
