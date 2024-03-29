package com.example.robinkopecky.pizza;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    public static List<Pizza> bagList = new ArrayList<>();


    private Pizza addPizza;


    private RecyclerView recyclerView;
    private PizzaDAOImplementation pizzaDAOImplementation;

    private List<Pizza> pizzaList = new ArrayList<>();

    private  PizzaAdapter pizzaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Italianno-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

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


        FloatingActionButton bag = (FloatingActionButton) findViewById(R.id.bag);
        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(BagActivity.getIntent(MainActivity.this, null));
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        pizzaDAOImplementation = new PizzaDAOImplementation(this);




    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public static Intent getIntent(BagActivity context, Long id) {
        Intent intent = new Intent(context,
                MainActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
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
                    int truePosition = pizzaViewHolder.getAdapterPosition();
                    startActivity(PizzaDetailActivity.getIntent(MainActivity.this, pizzaList.get(truePosition).getId()));

                }
            });

            pizzaViewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addPizza = new Pizza();
                    addPizza.setName(pizza.getName());
                    addPizza.setDescription(pizza.getDescription());
                    addPizza.setPrice(pizza.getPrice());


                    pizzaDAOImplementation.insertBag(addPizza);

                    Toast toast = Toast.makeText(MainActivity.this,getResources().getString(R.string.adding),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
            });


            pizzaViewHolder.pizzaLike.setChecked(pizza.isFavorite());

        pizzaViewHolder.pizzaLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pizza.setFavorite(isChecked);
                pizzaDAOImplementation.updatePizza(pizza);

                /*
                pizzaList.get(pizzaViewHolder.getAdapterPosition()).setFavorite(isChecked);

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(pizzaViewHolder.getAdapterPosition());
                    }
                });*/

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
            public Button buyButton;

            public PizzaViewHolder(@NonNull View itemView) {
                super(itemView);
                pizzaLike = itemView.findViewById(R.id.fav);
                pizzaName = itemView.findViewById(R.id.pizza_name);
                pizzaDescription = itemView.findViewById(R.id.pizza_description);
                pizzaPrice = itemView.findViewById(R.id.pizza_price);
                buyButton = itemView.findViewById(R.id.buy);

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

    private void showVersion() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setMessage(getResources().getString(R.string.version) + " beta 1.0");

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    private void showHelp() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setMessage(getResources().getString(R.string.helpText));

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
            showVersion();
        }
        if (id == R.id.language_settings){
            showChangeLanguageDialog();
        }
        if (id == R.id.add_pizza){
            startActivity(AddPizzaActivity.getIntent(MainActivity.this, null));
        }
        if (id == R.id.help){
            showHelp();
        }

        return super.onOptionsItemSelected(item);
    }




}
