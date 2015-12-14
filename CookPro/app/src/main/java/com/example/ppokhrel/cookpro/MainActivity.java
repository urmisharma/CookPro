package com.example.ppokhrel.cookpro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View view = getLayoutInflater().inflate(R.layout.activity_main, null, false);

        final SearchView searchView = (SearchView)findViewById(R.id.search);
        //final TextView editText = (TextView) findViewById(R.id.textView);
        Log.d("Batman", "gore batman");
        //final EditText recipe = (EditText) findViewById(R.id.recipe);

        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredients;
                final Intent i = new Intent(MainActivity.this, ListActivity.class);
                //ingredients = editText.getText().toString().trim();
                ingredients = searchView.getQuery().toString();
                Log.d("pokhrel", ingredients);
                //Log.d("pujan", editText.getText().toString().trim());


                String myRecipe = " ";

                // myRecipe = recipe.getText().toString().trim();


                if (ingredients != null) {
                    i.putExtra("ingredients", ingredients);
                    i.putExtra("recipe", myRecipe);
                    //Log.d("ksjdkfja", ingredients);
                    //Log.d("kajdkfksj", myRecipe);
                    //Global.ingredients = ingredients;
                    //Global.recipe = myRecipe;
                    startActivity(i);
                }
            }
        });
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
