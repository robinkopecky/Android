package com.example.robinkopecky.pizza;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PizzaDAOImplementation pizzaDAOImplementation;

    private List<Pizza> pizzaList = new ArrayList<>();

    private  PizzaAdapter pizzaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FavoriteActivity.getIntent(MainActivity.this, null));
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        pizzaDAOImplementation = new PizzaDAOImplementation(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder>{

        @NonNull
        @Override
        public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.row_pizza_list, viewGroup,false);
            return new PizzaViewHolder(itemView);
        }



        @Override
        public void onBindViewHolder(@NonNull final PizzaViewHolder pizzaViewHolder, final int i) {

            final Pizza pizza = pizzaList.get(i);

            pizzaViewHolder.pizzaName.setText(pizza.getName());
            pizzaViewHolder.pizzaDescription.setText(pizza.getDescription());
            pizzaViewHolder.pizzaPrice.setText(pizza.getPrice() + " Kč");

            pizzaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int truePosition = pizzaViewHolder.getAdapterPosition();
                    startActivity(PizzaDetailActivity.getIntent(MainActivity.this, null));

                }
            });


            pizzaViewHolder.pizzaLike.setChecked(pizza.isFavorite());

        pizzaViewHolder.pizzaLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pizza.setFavorite(isChecked);
                pizzaDAOImplementation.updatePizza(pizza);
                pizzaList.get(pizzaViewHolder.getAdapterPosition()).setFavorite(isChecked);

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(pizzaViewHolder.getAdapterPosition());
                    }
                });

            }
        });






        }

        @Override
        public int getItemCount() {
            return pizzaList.size();
        }

        public class PizzaViewHolder extends RecyclerView.ViewHolder{

            public TextView pizzaName;
            public TextView pizzaDescription;
            public TextView pizzaPrice;
            public CheckBox pizzaLike;

            public PizzaViewHolder(@NonNull View itemView) {
                super(itemView);
                pizzaLike = itemView.findViewById(R.id.fav);
                pizzaName = itemView.findViewById(R.id.pizza_name);
                pizzaDescription = itemView.findViewById(R.id.pizza_description);
                pizzaPrice = itemView.findViewById(R.id.pizza_price);

            }
        }
    }

    private void setList(){
        pizzaList.clear();
        pizzaList.addAll(pizzaDAOImplementation.getAllPizza());

        if (pizzaAdapter == null){
            pizzaAdapter = new PizzaAdapter();
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(pizzaAdapter);

        } else {
            pizzaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    private void showChangeLanguageDialog() {
        final String [] listItems = {"English", "Česky"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    setLocale("en");
                    recreate();
                }
                else if (which == 1){
                    setLocale("cs");
                    recreate();
                }

                dialog.dismiss();

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        //ulozi dat do shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

    }
    //nacte data z shared preferences

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
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
        if (id == R.id.language_settings){
            showChangeLanguageDialog();
        }
        if (id == R.id.add_pizza){
            startActivity(AddPizzaActivity.getIntent(MainActivity.this, null));
        }

        return super.onOptionsItemSelected(item);
    }


}
