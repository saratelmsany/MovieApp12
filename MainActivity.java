package com.example.android.movieapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NameListner {
    private boolean mtwoPane=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityFragment mFragment=new MainActivityFragment();
        mFragment.setmNameListner(this);
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,mFragment,"").commit();
        if(null!=findViewById(R.id.container)){
            mtwoPane=true;
        }

    }

    @Override
    public void setSelectedName(String name,String path,String overview,String date,int id,double rate) {
        //one pane
        if(!mtwoPane) {
            Intent toDetail = new Intent(this, DetailActivity.class);
            toDetail.putExtra("posterPath", path);
            toDetail.putExtra("title", name);
            toDetail.putExtra("overView", overview);
            toDetail.putExtra("rating", rate);
            toDetail.putExtra("releaseDate", date);
            toDetail.putExtra("id", id);
            startActivity(toDetail);
        }else {
            //two pane
            DetailActivityFragment detailFragment=new DetailActivityFragment();
            Bundle extra=new Bundle();
            extra.putString("title",name);
            extra.putString("posterPath",path);
            extra.putString("overView", overview);
            extra.putString("rating", String.valueOf(rate));
            extra.putString("releaseDate", date);
            extra.putString("id", String.valueOf(id));
            detailFragment.setArguments(extra);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,detailFragment,"")
                    .commit();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
