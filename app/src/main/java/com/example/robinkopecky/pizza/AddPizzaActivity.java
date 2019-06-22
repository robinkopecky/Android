package com.example.robinkopecky.pizza;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddPizzaActivity extends AppCompatActivity {
    private long id;
    private EditText pizzaName;
    private EditText pizzaDescription;
    private EditText pizzaPrice;

    private Pizza pizza;

    private PizzaDAOImplementation pizzaDAOImplementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pizza);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
/*
        id = getIntent().getLongExtra(IntentConstants.ID,-1);

        pizza = pizzaDAOImplementation.getPizzaByID(id);*/
        pizzaName = findViewById(R.id.pizza_name);
        pizzaDescription =findViewById(R.id.pizza_description);
        pizzaPrice =findViewById(R.id.pizza_price);

        pizzaDAOImplementation = new PizzaDAOImplementation(this);

    }

    public static Intent getIntent(MainActivity context, Long id) {
        Intent intent = new Intent(context,
                AddPizzaActivity.class);
        if (id != null){
            intent.putExtra(IntentConstants.ID, id);
        }
        return intent;
    }
    private void save(){

        String name = pizzaName.getText().toString();
        String description = pizzaDescription.getText().toString();
        String price = pizzaPrice.getText().toString();

        if (name.equals("")){
            pizzaName.setError("Vyplň název");
        } else {
                pizza = new Pizza();
                pizza.setName(name);
                pizza.setDescription(description);
                pizza.setPrice(price);
                pizzaDAOImplementation.insertPizza(pizza);


            finish();
            Toast.makeText(this, "ukládám", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_pizza, menu);
        return true;
    }

    public void save(MenuItem item) {
        save();
    }

}
