package com.example.ppokhrel.cookpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity {
    int pageNumber = 1;
    List<Recipe> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        //recipes = new ArrayList<Recipe>();
        //Recipe food = new Recipe();
        //recipes.add(food);
        recipes = new ArrayList<Recipe>();
        try {
            recipes = new RecipeFeed().execute().get();
        } catch (Exception e) {
            Log.d("Exception", "Error caught");
        }
        MyAdapter adapter = new MyAdapter(recipes);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this.getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        final Intent intent = new Intent(ListActivity.this, DetailActivity.class);
        //onClickListener
        rv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final View child = rv.findChildViewUnder(event.getX(), event.getY());
                if (child != null) {
                    child.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.d("  ", " " + recipes.get(rv.getChildLayoutPosition(v)).link);
                            intent.putExtra("url", recipes.get(rv.getChildLayoutPosition(v)).link);
                            startActivity(intent);
                        }
                    });
                }
                return false;
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

    class RecipeFeed extends AsyncTask<String, Void, List<Recipe>> {

        protected ArrayList<Recipe> doInBackground(String... urls) {
            //Log.d("ppdasf", "auiydfui");
            return this.populate();
        }

        protected void onPostExecute(List<Recipe> recipes){
            super.onPostExecute(recipes);
        }

        //returns a list of recipes after querying the site.
        //parameters are obtained after querying the url.
        private ArrayList<Recipe> populate(){
            ArrayList<Recipe> items = new ArrayList<Recipe>();
            //int pageNumber = 1;
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String ingredientsAPI = intent.getStringExtra("ingredients");
            ingredientsAPI.replaceAll(" ", "+");
            String myRecipe = intent.getStringExtra("recipe");
            //Log.d("tystdf", ingredientsAPI + myRecipe);
            String myurl = "http://www.recipepuppy.com/api/?i="+ ingredientsAPI + "&q="+ myRecipe + "&p=" + pageNumber;
            try {
                URL url = new URL
                        (myurl);
                //Log.d("kjhdfkjas", myurl);
                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // gets the server json data
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                String next;


                while ((next = bufferedReader.readLine()) != null) {
                    //JSONArray ja = new JSONArray(next);
                    JSONObject jas = new JSONObject(next);
                    //JSONArray ja = new JSONArray("results");
                    Log.d("uweyuri", jas.toString());

                    JSONArray ja = jas.getJSONArray("results");
                    Log.d("adfas", ja.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        //Log.d("adf", jo.toString());
                        //Log.d("adfas", "adfa");
                        //items.add(jo.getString("text"));
                        Recipe recipe = new Recipe();
                        String title = jo.getString("title");
                        //String description = jo.getString("description");
                        String myUrl = jo.getString("href");
                        String ingredients = jo.getString("ingredients");

                        recipe.title = title;
                        recipe.link = myUrl;
                        recipe.ingredients = ingredients;
                        recipe.imageUrl = jo.getString("thumbnail");
                        items.add(recipe);
                        //get all the jsonobjects, create different recipes from them and add to items array.

                    }
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return items;
        }
    }
}
