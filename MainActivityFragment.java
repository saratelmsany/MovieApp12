package com.example.android.movieapp1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

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
public class MainActivityFragment extends Fragment {
    private ImageAdapter mMovieAdapter;
    private GridView gridView;
    View rootView;
    ArrayList<String>paths;
    ArrayList<Response>movies;
    JSONObject movieJson;
    JSONArray movieArray;
    private NameListner mNameListner;

    public MainActivityFragment() {
    }
    void setmNameListner(NameListner nameListner){
        this.mNameListner=nameListner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
   @Override
    public void onStart() {
       if(connected()==false){
            startActivity(new Intent(getActivity(), NO_INTERNET_CONNECTION.class));
            Toast.makeText(getActivity(), "NO INTERNET CONNECTION PLEASE CHECK THE CONNECTION", Toast.LENGTH_LONG).show();
        }else {

           updatePopular();
       }
           super.onStart();


    }
   private boolean connected(){
        ConnectivityManager cm=(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        return ni !=null && ni.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id== R.id.action_most_popular) {
            updatePopular();
            return true;
        }
        if(id== R.id.action_top_rated) {
            updateRate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void updatePopular()
    {
        Fetcher mostPopular = new Fetcher("http://api.themoviedb.org/3/movie/popular?api_key=25b49f6ca8b89ccc3b48fa2aaaef5da6");
        mostPopular.execute();
    }
    public void updateRate()
    {
        Fetcher topRated=new Fetcher("http://api.themoviedb.org/3/movie/top_rated?api_key=25b49f6ca8b89ccc3b48fa2aaaef5da6");
        topRated.execute();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // mMovieAdapter = new ImageAdapter(getActivity());
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView)rootView.findViewById(R.id.grid_view);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Response movie = movies.get(position);
               String name= movie.getTitle();
                String path=movie.getPoster_path();
                String overview=movie.getOverview();
                String date=movie.getRelease_date();
                double rate=movie.getVote_average();
                int id1=movie.getId();
               /* Intent toDetail = new Intent(getActivity(), DetailActivity.class);
                toDetail.putExtra("posterPath", movie.getPoster_path());
                toDetail.putExtra("title", movie.getTitle());
                toDetail.putExtra("overView", movie.getOverview());
                toDetail.putExtra("rating", movie.getVote_average());
                toDetail.putExtra("releaseDate", movie.getRelease_date());
                toDetail.putExtra("id", movie.getId());
                // toDetail.putExtra("reviews",movie.getId());

                startActivity(toDetail);*/
                mNameListner.setSelectedName(name,path,overview,date,id1,rate);
              /*  mNameListner.setSelectedPath(path);
                mNameListner.setSelectedOverview(overview);
                mNameListner.setSelectedDate(date);
                mNameListner.setSelectedRate(rate);
                mNameListner.setSelectedId(id1);*/
            }
        });

        return rootView;
    }


    class Fetcher extends AsyncTask<Void,Void,ArrayList<String>>{
        private final String LOG_TAG = Fetcher.class.getSimpleName();
        public   String SERVER_URL = "http://api.themoviedb.org/3/movie/popular?api_key=25b49f6ca8b89ccc3b48fa2aaaef5da6";

        public Fetcher(String url)
        {
            SERVER_URL=url;
        }
        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            HttpURLConnection urlConnection=null;
            BufferedReader reader = null;
            String JsonStr =null;

            try {
                URL url = new URL(SERVER_URL);
                Log.v(LOG_TAG, "builtUri" + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
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
            Gson gson=new Gson();
            paths=new ArrayList<String>();

            movies=new ArrayList<>();
            try {
                movieJson = new JSONObject(JsonStr);
                movieArray = movieJson.getJSONArray("results");
                for(int i=0;i<movieArray.length();i++){
                    JSONObject jsonObject = movieArray.getJSONObject(i);
                    Response response=gson.fromJson(jsonObject.toString(), Response.class);
                    paths.add(response.getPoster_path());
                    movies.add(response);
                }
                Log.v(LOG_TAG,"POSTER"+movieArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return paths;
        }
        @Override
        protected void onPostExecute(ArrayList<String> paths) {
            mMovieAdapter= new ImageAdapter(getActivity(),paths);

            gridView.setAdapter(mMovieAdapter);


        }
    }
}
