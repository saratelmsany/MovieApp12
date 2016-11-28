package com.example.android.movieapp1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    String path,title,overView,date;
    double rate;
    String BASE_URL="http://image.tmdb.org/t/p/w185";
    ImageView imageView;
    TextView titletxt,datetxt,overViewtxt,ratetxt;
    int id;
    private ArrayList<ResponseReview.ResultsBean> reviews;
    ListView lv;
    ListView lv1;
    View rootView;
    JSONObject reviewJson;
    JSONObject trailerJson;
    JSONArray reviewArray;
    JSONArray trailerArray;
    ArrayList<TrailerResponse.ResultsBean>trailers;
    TrailerAdapter adapter;
    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle SentBundle=getArguments();
        Intent toDetail=getActivity().getIntent();
        if(toDetail!=null){
            path=toDetail.getStringExtra("posterPath");
            title=toDetail.getStringExtra("title");
            overView=toDetail.getStringExtra("overView");
            rate=toDetail.getDoubleExtra("rating", 0.0);
            date=toDetail.getStringExtra("releaseDate");
            id=toDetail.getIntExtra("id", 0);
        }
        rootView=inflater.inflate(R.layout.fragment_detail, container, false);
        lv1=(ListView)rootView.findViewById(R.id.list_trailers) ;
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrailerResponse.ResultsBean trailer = (TrailerResponse.ResultsBean) adapter.getItem(position);
                //String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
               // Log.v("cliccccccccccccccck", url);
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(in);
              //  Toast.makeText(getActivity(), "NO INTERNET CONNECTION PLEASE CHECK THE CONNECTION", Toast.LENGTH_LONG).show();
            }
        });
        imageView=(ImageView)rootView.findViewById(R.id.poster);
        titletxt=(TextView)rootView.findViewById(R.id.title);
        datetxt=(TextView)rootView.findViewById(R.id.date);
        overViewtxt=(TextView)rootView.findViewById(R.id.overView);
        ratetxt=(TextView)rootView.findViewById(R.id.rate);
        Picasso.with(getContext()).load(BASE_URL+path).into(imageView);
        titletxt.setText(title);
        datetxt.setText(date);
        String x = String.valueOf(rate);
        ratetxt.setText("Rated: "+x+"/10");
        overViewtxt.setText(overView);
  if(toDetail!=null) {
      ReviewTask review = new ReviewTask(id);
      review.execute();
      TrailerTask trailer = new TrailerTask(id);
      trailer.execute();
}
        return rootView;
    }


    class ReviewTask extends AsyncTask<Void, Void, ArrayList<ResponseReview.ResultsBean>> {
        private final String LOG_TAG = ReviewTask.class.getSimpleName();
        private int ID;
        public String SERVER_URL;

        public ReviewTask(int ID) {
            this.ID=ID;
            SERVER_URL = "http://api.themoviedb.org/3/movie/"+ID+"/reviews?api_key=25b49f6ca8b89ccc3b48fa2aaaef5da6";
        }
        @Override
        public ArrayList<ResponseReview.ResultsBean> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            try {
                URL url = new URL(SERVER_URL);
                Log.v(LOG_TAG, "builtUri" + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JsonStr = buffer.toString();
                Log.v(LOG_TAG, " Json Str" + JsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            Gson gson = new Gson();
            reviews = new ArrayList<>();
            try {
                reviewJson = new JSONObject(JsonStr);
                reviewArray = reviewJson.getJSONArray("results");
                for (int i = 0; i < reviewArray.length(); i++) {
                    JSONObject jsonObject = reviewArray.getJSONObject(i);
                     ResponseReview.ResultsBean response = gson.fromJson(jsonObject.toString(), ResponseReview.ResultsBean.class);
                    reviews.add(response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviews;
        }
        @Override
        protected void onPostExecute(ArrayList<ResponseReview.ResultsBean> reviews) {
            ReviewsAdapter adapter = new ReviewsAdapter(getActivity(), reviews);
            lv=(ListView)rootView.findViewById(R.id.list_reviews) ;
            lv.setAdapter(adapter);
        }
    }

    class TrailerTask extends AsyncTask<Void, Void, ArrayList<TrailerResponse.ResultsBean>> {
        private final String LOG_TAG = ReviewTask.class.getSimpleName();
        private int ID;
        public String SERVER_URL;

        public TrailerTask(int ID) {
            this.ID=ID;
            SERVER_URL = "http://api.themoviedb.org/3/movie/"+ID+"/videos?api_key=25b49f6ca8b89ccc3b48fa2aaaef5da6";
        }
        @Override
        public ArrayList<TrailerResponse.ResultsBean> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            try {
                URL url = new URL(SERVER_URL);
                Log.v(LOG_TAG, "builtUri" + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JsonStr = buffer.toString();
                Log.v(LOG_TAG, " Json Str" + JsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            Gson gson = new Gson();
            trailers = new ArrayList<>();
            try {
                trailerJson = new JSONObject(JsonStr);
                trailerArray = reviewJson.getJSONArray("results");
                for (int i = 0; i < trailerArray.length(); i++) {
                    JSONObject jsonObject = trailerArray.getJSONObject(i);
                    TrailerResponse.ResultsBean response = gson.fromJson(jsonObject.toString(), TrailerResponse.ResultsBean.class);
                    trailers.add(response);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trailers;
        }
        @Override
        protected void onPostExecute(ArrayList<TrailerResponse.ResultsBean> trailers) {
            adapter = new TrailerAdapter(getActivity(),trailers );

            lv1.setAdapter(adapter);

        }
    }
}
